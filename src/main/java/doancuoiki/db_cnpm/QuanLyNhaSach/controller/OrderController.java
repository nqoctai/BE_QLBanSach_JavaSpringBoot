package doancuoiki.db_cnpm.QuanLyNhaSach.controller;


import com.turkraft.springfilter.boot.Filter;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Order;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ApiResponse;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqCreateOrder;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqOrderUpdate;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqPlaceOrder;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqPlaceOrderWithVNPay;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResultPaginationDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.OrderService;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping("/order/create")
    public ResponseEntity<ApiResponse<Order>> createOrder(@RequestBody ReqCreateOrder rqCreateOrder) throws AppException
    {
        Order res = orderService.createOrder(rqCreateOrder);
        ApiResponse<Order> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Create order successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/order")
    public ResponseEntity<ApiResponse<Order>> placeOrder(@RequestBody ReqPlaceOrder reqPlaceOrder) throws AppException {
        Order order = orderService.placeOrder(reqPlaceOrder);
        ApiResponse<Order> apiResponse = new ApiResponse<Order>();
        apiResponse.setData(order);
        apiResponse.setMessage("Place order successfully");
        apiResponse.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/order")
    public ResponseEntity<ApiResponse<Order>> updateOrder(@RequestBody ReqOrderUpdate rqOrderUpdate) throws AppException {
        Order res = orderService.updateOrder(rqOrderUpdate);
        ApiResponse<Order> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Update order successfully");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @GetMapping("/order")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> fetchListOrder(@Filter Specification<Order> spec, Pageable pageable) {
        ResultPaginationDTO resultPaginationDTO = orderService.fetchListOrder(spec, pageable);
        ApiResponse<ResultPaginationDTO> apiResponse = new ApiResponse<ResultPaginationDTO>();
        apiResponse.setData(resultPaginationDTO);
        apiResponse.setMessage("Fetch list order successfully");
        apiResponse.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/order/history/{id}")
    public ResponseEntity<ApiResponse<List<Order>>> getHistoryOrder(@PathVariable("id") Long accountId) throws AppException {
        List<Order> orders = orderService.getHistoryOrder(accountId);
        ApiResponse<List<Order>> apiResponse = new ApiResponse<List<Order>>();
        apiResponse.setData(orders);
        apiResponse.setMessage("Get history order successfully");
        apiResponse.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(apiResponse);
    }









}
