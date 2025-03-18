package doancuoiki.db_cnpm.QuanLyNhaSach.controller;

import com.turkraft.springfilter.boot.Filter;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Account;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Customer;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ApiResponse;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ViewDB.InvoiceDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ViewDB.TopCustomerDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqChangePassword;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqUpdateAccountDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResAccountDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResultPaginationDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.CustomerService;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.OrderService;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {
    private final CustomerService customerService;

    private final OrderService orderService;

    public CustomerController(CustomerService customerService, OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @PostMapping("/customer")
    public ResponseEntity<ApiResponse<Customer>> createCustomer(@RequestBody @Valid Customer rqCustomer)
            throws AppException {

        boolean emailExist = customerService.checkEmailExist(rqCustomer.getEmail());
        if (emailExist) {
            throw new AppException("Email đã tồn tại");
        }
        Customer res = customerService.createCustomer(rqCustomer);
        ApiResponse<Customer> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Tạo khách hàng thành công");
        response.setStatus(HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/customers")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllCustomerWithPagination(
            @Filter Specification<Customer> spec, Pageable pageable) throws AppException {
        ResultPaginationDTO res = customerService.getAllCustomerWithPagination(spec, pageable);
        ApiResponse<ResultPaginationDTO> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Lấy danh sách khách hàng thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    //
    @PutMapping("/customer")
    public ResponseEntity<ApiResponse<Customer>> updateCustomer(@Valid @RequestBody Customer rqCustomer)
            throws AppException {
        boolean isExist = customerService.checkCustomerExist(rqCustomer.getId());
        if (!isExist) {
            throw new AppException("Khách hàng này không tồn tại");
        }
        Customer res = customerService.updateCustomer(rqCustomer);
        ApiResponse<Customer> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Cập nhật khách hàng thành công");
        response.setStatus(HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //
    @DeleteMapping("/customer/{id}")
    public ResponseEntity<ApiResponse<Customer>> deleteCustomer(@PathVariable("id") Long id) throws AppException {
        Customer res = customerService.getCustomerById(id);
        if (res == null) {
            throw new AppException("Id không tồn tại");
        }
        customerService.deleteCustomer(id);
        ApiResponse<Customer> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Xóa tài khoản thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<ApiResponse<Customer>> getCustomerById(@PathVariable("id") long id) throws AppException {
        boolean isExist = customerService.checkCustomerExist(id);
        if (!isExist) {
            throw new AppException("Id không tồn tại");
        }
        Customer res = customerService.getCustomerById(id);
        ApiResponse<Customer> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Lấy thông tin khách hàng thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

}
