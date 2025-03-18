package doancuoiki.db_cnpm.QuanLyNhaSach.controller;


import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Cart;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ApiResponse;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqAddItemToCart;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqUpdateToCart;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.CartService;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }


    @PostMapping("/cart/add")
    public ResponseEntity<ApiResponse<Cart>> addItemToCart(@RequestBody ReqAddItemToCart reqAddItemToCart) throws AppException {
        Cart cart = cartService.addItemToCart(reqAddItemToCart);
        ApiResponse<Cart> apiResponse = new ApiResponse<Cart>();
        apiResponse.setData(cart);
        apiResponse.setMessage("Add item to cart successfully");
        apiResponse.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/cart/update")
    public ResponseEntity<ApiResponse<Cart>> updateItemToCart(@RequestBody ReqUpdateToCart reqUpdateToCart)throws AppException {
            Cart cart = cartService.updateQuantityItemInCart(reqUpdateToCart);
            ApiResponse<Cart> apiResponse = new ApiResponse<Cart>();
            apiResponse.setData(cart);
            apiResponse.setMessage("Update item to cart successfully");
            apiResponse.setStatus(HttpStatus.OK.value());
            return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/cart/delete/{cartItemId}")
    public ResponseEntity<ApiResponse<Cart>> deleteItemToCart(@PathVariable("cartItemId") long id  ) throws AppException {
        Cart cart = cartService.deleteItemInCart(id);
        ApiResponse<Cart> apiResponse = new ApiResponse<Cart>();
        apiResponse.setData(cart);
        apiResponse.setMessage("Delete item to cart successfully");
        apiResponse.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(apiResponse);
    }



}
