package doancuoiki.db_cnpm.QuanLyNhaSach.controller;

import com.turkraft.springfilter.boot.Filter;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Supply;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ApiResponse;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqGetSupply;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResSupply;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResultPaginationDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.SupplyService;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1")
public class SupplyController {
    private final SupplyService supplyService;

    public SupplyController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

    @PostMapping("/fetch-supply")
    public ResponseEntity<ApiResponse<ResSupply>> getSupplyBySupplierIdAndBookId(@RequestBody ReqGetSupply reqGetSupply) throws AppException {
        Supply supply = supplyService.getSupplyBySupplierIdAndBookId(reqGetSupply);
        ResSupply resSupply = new ResSupply();
        resSupply.setId(supply.getId());
        resSupply.setBook(supply.getBook());
        resSupply.setSupplier(supply.getSupplier());
        resSupply.setSupplyPrice(supply.getSupplyPrice());
        ApiResponse<ResSupply> response = new ApiResponse<>();
        response.setData(resSupply);
        response.setStatus(200);
        response.setMessage("Get supply by supplier id and book id successfully!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/supplies")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllSupply(@Filter Specification<Supply> spec, Pageable pageable) {
        ResultPaginationDTO res = supplyService.getAllSupply(spec, pageable);
        ApiResponse<ResultPaginationDTO> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Get all supply successfully!");
        response.setStatus(200);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/supply")
    public ResponseEntity<ApiResponse<Supply>> createSupply(@RequestBody Supply supply) throws AppException {
        Supply res = supplyService.createSupply(supply);
        ApiResponse<Supply> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Thêm supply thành công");
        response.setStatus(HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/supply/{id}")
    public ResponseEntity<ApiResponse<Supply>> deleteSupply(@PathVariable("id") Long id) throws AppException {
        Supply res = supplyService.deleteSupply(id);
        ApiResponse<Supply> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Xóa supply thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/supply")
    public ResponseEntity<ApiResponse<Supply>> updateSupply(@RequestBody Supply supply) throws AppException {
        Supply res = supplyService.updateSupply(supply);
        ApiResponse<Supply> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Cập nhật supply thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }
}
