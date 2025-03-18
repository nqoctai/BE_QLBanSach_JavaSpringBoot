package doancuoiki.db_cnpm.QuanLyNhaSach.controller;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Order;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ApiResponse;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.PaymentDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqOrderUpdate;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.OrderService;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.PaymentService;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/payment")

public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;

    public PaymentController(PaymentService paymentService, OrderService orderService) {
        this.paymentService = paymentService;
        this.orderService = orderService;
    }

    @GetMapping("/vn-pay")
    public ResponseEntity<ApiResponse<PaymentDTO.VNPayResponse>> pay(HttpServletRequest request) {
        ApiResponse<PaymentDTO.VNPayResponse> response = new ApiResponse<>();
        PaymentDTO.VNPayResponse vnPayResponse = paymentService.createVnPayPayment(request);
        response.setData(vnPayResponse);
        response.setMessage("Create payment successfully");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/vn-pay-callback")
    public ResponseEntity<ApiResponse<PaymentDTO.VNPayResponse>> payCallbackHandler(HttpServletRequest request, HttpServletResponse response) throws AppException, IOException {
        String txnRef = request.getParameter("vnp_TxnRef");
        String status = request.getParameter("vnp_ResponseCode");
        ApiResponse<PaymentDTO.VNPayResponse> res = new ApiResponse<>();
        res.setMessage("Payment status: " + status);
        res.setStatus(HttpStatus.OK.value());
        res.setData(PaymentDTO.VNPayResponse.builder().code(status).message("success").build());

        if (status.equals("00")) {
            Order order = orderService.findByTransactionRef(txnRef);
            if(order == null) {
                res.setMessage("Order not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
            }

            ReqOrderUpdate reqOrderUpdate = new ReqOrderUpdate();
            reqOrderUpdate.setId(order.getId());
            reqOrderUpdate.setStatusId(10);
            reqOrderUpdate.setNote("Payment success");
            orderService.updateOrder(reqOrderUpdate);
            response.sendRedirect("http://localhost:3000/order?status=" +
                    ("00".equals(status) ? "success" : "failed") + "&vnp_TxnRef=" + txnRef + "&currentStepVnPay=2");
            return ResponseEntity.ok(res);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
        }
    }
}
