package doancuoiki.db_cnpm.QuanLyNhaSach.services;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.*;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ViewDB.ReqMonthlyRevenue;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.*;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResultPaginationDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.CartItemRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.CartRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.OrderItemRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.OrderRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final AccountService accountService;

    private final CartRepository cartRepository;

    private final BookService bookService;

    private final CustomerService customerService;

    private final OrderItemRepository orderItemRepository;

    private final CartItemRepository cartItemRepository;

    private final ShippingStatusService shippingStatusService;

    private final OrderShippingEventService orderShippingEventService;

    public OrderService(OrderRepository orderRepository, AccountService accountService, CartRepository cartRepository,
                        OrderItemRepository orderItemRepository, CartItemRepository cartItemRepository,
                        BookService bookService, CustomerService customerService, ShippingStatusService shippingStatusService,
                        OrderShippingEventService orderShippingEventService) {
        this.orderRepository = orderRepository;
        this.accountService = accountService;
        this.cartRepository = cartRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookService = bookService;
        this.customerService = customerService;
        this.shippingStatusService = shippingStatusService;
        this.orderShippingEventService = orderShippingEventService;
    }

    public Order createOrder(ReqCreateOrder reqCreateOrder) throws AppException {
        Order order = new Order();
        Customer customer = this.customerService.getCustomerByEmail(reqCreateOrder.getEmail());
        if (customer == null) {
            customer = new Customer();
            customer.setEmail(reqCreateOrder.getEmail());
            customer.setPhone(reqCreateOrder.getReceiverPhone());
            customer.setName(reqCreateOrder.getReceiverName());
            customer.setAddress(reqCreateOrder.getReceiverAddress());
            customer = this.customerService.createCustomer(customer);
        }
        order.setCustomer(customer);
        order.setReceiverEmail(reqCreateOrder.getEmail());
        order.setReceiverName(reqCreateOrder.getReceiverName());
        order.setReceiverAddress(reqCreateOrder.getReceiverAddress());
        order.setReceiverPhone(reqCreateOrder.getReceiverPhone());
        order.setTotalPrice(reqCreateOrder.getTotalPrice());
        ShippingStatus shippingStatus = this.shippingStatusService.getShippingStatusById(reqCreateOrder.getStatusId());
        order = this.orderRepository.save(order);
        OrderShippingEvent orderShippingEvent = new OrderShippingEvent();
        orderShippingEvent.setOrder(order);
        orderShippingEvent.setShippingStatus(shippingStatus);
        orderShippingEvent = this.orderShippingEventService.createOrderShippingEvent(orderShippingEvent);
        order.getOrderShippingEvents().add(orderShippingEvent);
        for (ReqCreateOderItem item : reqCreateOrder.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            Book book = this.bookService.getBookById(item.getBookId());
            if (book == null) {
                throw new AppException("Book not found");
            } else {
                if (book.getQuantity() < item.getQuantity()) {
                    List<OrderShippingEvent> orderShippingEvents = order.getOrderShippingEvents();
                    for (OrderShippingEvent orderShippingEvent1 : orderShippingEvents) {
                        this.orderShippingEventService.deleteOrderShippingEvent(orderShippingEvent1.getId());
                    }
                    orderRepository.delete(order);
                    throw new AppException("Not enough book quantity");
                }
            }
            book.setQuantity(book.getQuantity() - item.getQuantity());
            this.bookService.updateBook(book.getId(), book);
            orderItem.setBook(book);
            orderItem.setPrice(item.getQuantity() * book.getPrice());
            orderItem.setQuantity(item.getQuantity());
            orderItem = this.orderItemRepository.save(orderItem);
            order.getOrderItems().add(orderItem);
        }

        return this.orderRepository.save(order);
    }

    @Transactional
    public Order placeOrder(ReqPlaceOrder reqPlaceOrder) throws AppException {
        Account account = accountService.getAccountById(reqPlaceOrder.getAccountId());
        if (account == null) {
            throw new AppException("Account not found");
        }
        Customer customer = account.getCustomer();
        if (customer == null) {
            throw new AppException("Customer not found");
        }
        Cart cart = this.cartRepository.findByCustomer(customer);
        if (cart == null) {
            throw new AppException("Cart not found");
        }
        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems == null || cartItems.size() == 0) {
            throw new AppException("Cart is empty");
        }
        Order order = new Order();
        order.setCustomer(customer);

        order.setReceiverEmail(reqPlaceOrder.getEmail());
        order.setPaymentMethod(reqPlaceOrder.getPaymentMethod());
        order.setVnpTxnRef(reqPlaceOrder.getVnp_txn_ref());
        order.setReceiverAddress(reqPlaceOrder.getAddress());
        order.setReceiverName(reqPlaceOrder.getName());
        order.setReceiverPhone(reqPlaceOrder.getPhone());
        order.setTotalPrice(reqPlaceOrder.getTotalPrice());
        ShippingStatus shippingStatus = this.shippingStatusService.getShippingStatusById(1);
        order = orderRepository.save(order);
        OrderShippingEvent orderShippingEvent = new OrderShippingEvent();
        orderShippingEvent.setOrder(order);
        orderShippingEvent.setShippingStatus(shippingStatus);
        orderShippingEvent = this.orderShippingEventService.createOrderShippingEvent(orderShippingEvent);
        order.getOrderShippingEvents().add(orderShippingEvent);

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            Book book = cartItem.getBook();
            if (book.getQuantity() < cartItem.getQuantity()) {
                List<OrderShippingEvent> orderShippingEvents = order.getOrderShippingEvents();
                for (OrderShippingEvent orderShippingEvent1 : orderShippingEvents) {
                    this.orderShippingEventService.deleteOrderShippingEvent(orderShippingEvent1.getId());
                }
                orderRepository.delete(order);
                throw new AppException("Not enough book quantity");
            }
            book.setQuantity(book.getQuantity() - cartItem.getQuantity());
            this.bookService.updateBook(book.getId(), book);
            orderItem = this.orderItemRepository.save(orderItem);
            order.getOrderItems().add(orderItem);
        }

        for (CartItem cartItem : cartItems) {
            this.cartItemRepository.deleteById(cartItem.getId());
        }
        this.cartRepository.deleteById(cart.getId());
        return orderRepository.save(order);
    }



    public ResultPaginationDTO fetchListOrder(Specification<Order> spec, Pageable pageable) {
        Page<Order> pageOrders = this.orderRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageOrders.getTotalPages());
        meta.setTotal(pageOrders.getTotalElements());
        rs.setMeta(meta);
        rs.setResult(pageOrders.getContent());
        return rs;
    }

    public List<Order> getHistoryOrder(Long accountId) throws AppException {
        Account account = accountService.getAccountById(accountId);
        if (account == null) {
            throw new AppException("Account not found");
        }

        Customer customer = account.getCustomer();

        return this.orderRepository.findByCustomer(customer);
    }

    public Order updateOrder(ReqOrderUpdate rqOrderUpdate) throws AppException {
        Order order = this.orderRepository.findById(rqOrderUpdate.getId()).orElse(null);
        if (order == null) {
            throw new AppException("Order not found");
        }
        ShippingStatus shippingStatus = this.shippingStatusService.getShippingStatusById(rqOrderUpdate.getStatusId());
        OrderShippingEvent orderShippingEvent = new OrderShippingEvent();
        orderShippingEvent.setOrder(order);
        orderShippingEvent.setShippingStatus(shippingStatus);

        if (shippingStatus.getStatus().equals("Hủy bỏ")) {
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                Book book = orderItem.getBook();
                book.setQuantity(book.getQuantity() + orderItem.getQuantity());
                this.bookService.updateBook(book.getId(), book);
            }
        }

        if (rqOrderUpdate.getNote() != null) {
            orderShippingEvent.setNote(rqOrderUpdate.getNote());
        }
        orderShippingEvent = this.orderShippingEventService.createOrderShippingEvent(orderShippingEvent);
        order.getOrderShippingEvents().add(orderShippingEvent);
        order.setUpdatedAt(Instant.now());
        return this.orderRepository.save(order);
    }

    public Order findByTransactionRef(String transactionRef) {
        return orderRepository.findByVnpTxnRef(transactionRef);
    }

    public List<ReqMonthlyRevenue> getMonthlyRevenueByYear(int year) {
        List<ReqMonthlyRevenue> res = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            LocalDateTime start = LocalDateTime.of(year, i, 1, 0, 0);
            LocalDateTime end = LocalDateTime.of(year, i, start.getMonth().length(start.toLocalDate().isLeapYear()), 23, 59);
            Instant startInstant = start.atZone(ZoneId.systemDefault()).toInstant();
            Instant endInstant = end.atZone(ZoneId.systemDefault()).toInstant();
            List<Order> orders = orderRepository.findByCreatedAtBetween(startInstant, endInstant);
            long total = 0;
            for (Order order : orders) {
                total += order.getTotalPrice();
            }
            ReqMonthlyRevenue reqMonthlyRevenue = new ReqMonthlyRevenue();
            reqMonthlyRevenue.setMonth(i);
            reqMonthlyRevenue.setRevenue(total);
            res.add(reqMonthlyRevenue);
        }
        return res;
    }

}


