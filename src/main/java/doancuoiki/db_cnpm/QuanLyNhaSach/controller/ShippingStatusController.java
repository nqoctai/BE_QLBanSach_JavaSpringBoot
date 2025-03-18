package doancuoiki.db_cnpm.QuanLyNhaSach.controller;


import doancuoiki.db_cnpm.QuanLyNhaSach.domain.ShippingStatus;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ApiResponse;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.ShippingStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ShippingStatusController {
    private final ShippingStatusService shippingStatusService;

    public ShippingStatusController(ShippingStatusService shippingStatusService) {
        this.shippingStatusService = shippingStatusService;
    }

    @GetMapping("/shippingStatus")
    public ResponseEntity<ApiResponse<List<ShippingStatus>>> getAllShippingStatus() {
        List<ShippingStatus> listShippingStatus = shippingStatusService.getAllShippingStatus();
        ApiResponse<List<ShippingStatus>> response = new ApiResponse<List<ShippingStatus>>();
        response.setData(listShippingStatus);
        response.setMessage("Lấy danh sách trạng thái giao hàng thành công");
        response.setStatus(200);
        return ResponseEntity.ok(response);
    }
}
