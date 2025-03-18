package doancuoiki.db_cnpm.QuanLyNhaSach.controller;


import com.turkraft.springfilter.boot.Filter;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.ImportReceipt;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Supplier;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ApiResponse;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqCreateImportReceipt;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqUpdateImportReceipt;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResultPaginationDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.ImportReceiptService;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1")
public class ImportReceiptController {
        private final ImportReceiptService importReceiptService;

        public ImportReceiptController(ImportReceiptService importReceiptService) {
            this.importReceiptService = importReceiptService;
        }

        @PostMapping("/receipt")
        public ResponseEntity<ApiResponse<ImportReceipt>> createImportReceipt(@RequestBody ReqCreateImportReceipt reqCreateImportReceipt) throws AppException {
            ImportReceipt importReceipt = importReceiptService.createImportReceipt(reqCreateImportReceipt);
            ApiResponse<ImportReceipt> apiResponse = new ApiResponse<>();
            apiResponse.setData(importReceipt);
            apiResponse.setMessage("Create import receipt successfully");
            apiResponse.setStatus(HttpStatus.CREATED.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
        }

        @GetMapping("/receipts")
        public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllReceipt(@Filter Specification<ImportReceipt> spec, Pageable pageable) {
            ResultPaginationDTO res = importReceiptService.getReceiptAllWithPagination(spec, pageable);
            ApiResponse<ResultPaginationDTO> response = new ApiResponse<>();
            response.setData(res);
            response.setMessage("Lấy danh sách phiếu nhập hàng thành công");
            response.setStatus(HttpStatus.OK.value());
            return ResponseEntity.ok(response);
        }

        @PutMapping("/receipt")
        public ResponseEntity<ApiResponse<ImportReceipt>> updateImportReceipt(@RequestBody ReqUpdateImportReceipt reqUpdateImportReceipt) throws AppException {
            ImportReceipt importReceipt = importReceiptService.updateImportReceipt(reqUpdateImportReceipt);
            ApiResponse<ImportReceipt> apiResponse = new ApiResponse<>();
            apiResponse.setData(importReceipt);
            apiResponse.setMessage("Update import receipt successfully");
            apiResponse.setStatus(HttpStatus.OK.value());
            return ResponseEntity.ok(apiResponse);
        }


}
