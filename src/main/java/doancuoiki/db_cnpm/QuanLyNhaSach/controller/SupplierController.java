package doancuoiki.db_cnpm.QuanLyNhaSach.controller;


import com.turkraft.springfilter.boot.Filter;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Book;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Supplier;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Supply;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ApiResponse;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResultPaginationDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.SupplierService;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("/suppliers")
    public ResponseEntity<ApiResponse<List<Supplier>>> getAllSupplier(){
        ApiResponse<List<Supplier>> response = new ApiResponse<>();
        response.setData(supplierService.getAllSupplier());
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Get all supplier successfully!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/suppliers-pagination")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllSupplierWithPagination(@Filter Specification<Supplier> spec, Pageable pageable) {
        ResultPaginationDTO res = supplierService.getAllSupplierWithPagination(spec,pageable);
        ApiResponse<ResultPaginationDTO> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Get all supplier with pagination successfully!");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/supplier/{id}")
    public ResponseEntity<ApiResponse<Supplier>> getSupplierById(@PathVariable("id") Long id) throws AppException {
        Supplier supplier = supplierService.getSupplierById(id);
        if(supplier == null)
        {
            throw new AppException("Supplier not found!");
        }

        ApiResponse<Supplier> response = new ApiResponse<>();
        response.setData(supplier);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Get supplier by id successfully!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/supplier/{id}/books")
    public ResponseEntity<ApiResponse<List<Book>>> getBooksBySupplierId(@PathVariable("id") Long id) throws AppException {
        Supplier supplier = supplierService.getSupplierById(id);
        if(supplier == null)
        {
            throw new AppException("Supplier not found!");
        }
        List<Supply> supplies = supplier.getSupplies();
        List<Book> books = supplies.stream().map(Supply::getBook).toList();
        ApiResponse<List<Book>> response = new ApiResponse<>();
        response.setData(books);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Get books by supplier id successfully!");
        return ResponseEntity.ok(response);
    }


    @GetMapping("/supplier/{id}/supplies")
    public ResponseEntity<ApiResponse<List<Supply>>> fetchListSupplyBySupplierId(@PathVariable("id") Long id) throws AppException {
        Supplier supplier = supplierService.getSupplierById(id);
        if(supplier == null)
        {
            throw new AppException("Supplier not found!");
        }
        List<Supply> supplies = supplier.getSupplies();
        ApiResponse<List<Supply>> response = new ApiResponse<>();
        response.setData(supplies);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Get supplies by supplier id successfully!");
        return ResponseEntity.ok(response);
    }


    @PostMapping("/supplier")
    public ResponseEntity<ApiResponse<Supplier>> createSupplier(@RequestBody Supplier rqSupplier) throws AppException {
        Supplier supplier = supplierService.createSupplier(rqSupplier);
        ApiResponse<Supplier> response = new ApiResponse<>();
        response.setData(supplier);
        response.setStatus(HttpStatus.CREATED.value());
        response.setMessage("Create supplier successfully!");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/supplier")
    public ResponseEntity<ApiResponse<Supplier>> updateSupplier(@RequestBody Supplier rqSupplier) throws AppException {
        Supplier supplier = supplierService.updateSupplier(rqSupplier);
        ApiResponse<Supplier> response = new ApiResponse<>();
        response.setData(supplier);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Update supplier successfully!");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/supplier/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSupplier(@PathVariable("id") Long id) throws AppException {
        supplierService.deleteSupplier(id);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setData(null);
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("Delete supplier successfully!");
        return ResponseEntity.ok(response);
    }

}
