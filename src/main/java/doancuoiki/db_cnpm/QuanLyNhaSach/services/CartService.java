package doancuoiki.db_cnpm.QuanLyNhaSach.services;


import doancuoiki.db_cnpm.QuanLyNhaSach.domain.*;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqAddItemToCart;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqUpdateToCart;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.BookRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.CartItemRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.CartRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;

    private final AccountService accountService;

    private final CartItemRepository cartItemRepository;

    private final BookRepository bookRepository;

    private final BookService bookService;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       BookRepository bookRepository, AccountService accountService, BookService bookService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
        this.accountService = accountService;
        this.bookService = bookService;
    }

    public Cart addItemToCart(ReqAddItemToCart reqAddItemToCart) throws AppException {
        Account account = accountService.getUserByEmail(reqAddItemToCart.getEmail());
        if(account == null){
            throw new AppException("Account not found");
        }
            Customer customer = account.getCustomer();
            Cart cart = this.cartRepository.findByCustomer(customer);

            if(cart == null){
                Cart otherCart = new Cart();
                otherCart.setCustomer(customer);
                otherCart.setCount(0);
                otherCart.setSumPrice(0);

                cart = this.cartRepository.save(otherCart);
            }
            Book book = this.bookService.getBookById(reqAddItemToCart.getBookId());
            if(book != null){
                Book realBook = book;
                CartItem oldCartItem = this.cartItemRepository.findByCartAndBook(cart, realBook);
                if(oldCartItem ==null){
                    CartItem cartItem = new CartItem();
                    cartItem.setCart(cart);
                    cartItem.setBook(realBook);
                    cartItem.setQuantity(reqAddItemToCart.getQuantity());
                    cartItem.setPrice(realBook.getPrice() * reqAddItemToCart.getQuantity());
                    this.cartItemRepository.save(cartItem);
                    cart.setCount(cart.getCount() + 1);
                    cart.setSumPrice(cart.getSumPrice() + cartItem.getPrice());
                    cart.getCartItems().add(cartItem);
                }else{
                    oldCartItem.setQuantity(oldCartItem.getQuantity() + reqAddItemToCart.getQuantity());
                    oldCartItem.setPrice(oldCartItem.getBook().getPrice() * oldCartItem.getQuantity());
                    this.cartItemRepository.save(oldCartItem);
                    cart.setSumPrice(cart.getSumPrice() + oldCartItem.getBook().getPrice() * reqAddItemToCart.getQuantity());
                }
            }
            cart = this.cartRepository.save(cart);
            return cart;
    }


    public Cart updateQuantityItemInCart(ReqUpdateToCart reqUpdateToCart) throws AppException {
        Optional<Cart> cart = this.cartRepository.findById(reqUpdateToCart.getCartId());
        if(cart.isPresent()){
            Cart realCart = cart.get();
           CartItem cartItem = realCart.getCartItems().stream().filter(item -> item.getId() == reqUpdateToCart.getCartItemId()).findFirst().orElse(null);
              if(cartItem != null){
                cartItem.setQuantity(reqUpdateToCart.getQuantity());
                cartItem.setPrice(cartItem.getBook().getPrice() * cartItem.getQuantity());
                this.cartItemRepository.save(cartItem);
                realCart.setSumPrice(realCart.getCartItems().stream().mapToDouble(CartItem::getPrice).sum());
                this.cartRepository.save(realCart);
              }
            return cart.get();
        }else {
            throw new AppException("Cart not found");
        }
    }

    public Cart deleteItemInCart(long cartItemId) throws AppException {
        Optional<CartItem> cartItem = this.cartItemRepository.findById(cartItemId);
        if(cartItem.isPresent()){
            CartItem realCartItem = cartItem.get();
            Cart cart = realCartItem.getCart();
            cart.getCartItems().remove(realCartItem);
            this.cartItemRepository.delete(realCartItem);
            cart.setSumPrice(cart.getCartItems().stream().mapToDouble(CartItem::getPrice).sum());
            cart.setCount(cart.getCartItems().size());
            this.cartRepository.save(cart);
            return cart;
        }
        throw new AppException("Cart item not found");
    }
}
