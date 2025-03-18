package doancuoiki.db_cnpm.QuanLyNhaSach.controller;

import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ApiResponse;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ViewDB.*;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.AccountRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.EmployeeRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.OrderRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.BookService;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.CategoryService;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.CustomerService;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class DashBoardController {
    private final AccountRepository accountRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private final BookService bookService;
    private final CustomerService customerService;

    private final CategoryService categoryService;
    private final EmployeeRepository employeeRepository;

    public DashBoardController(AccountRepository accountRepository, OrderRepository orderRepository,
                               OrderService orderService, CategoryService categoryService, BookService bookService,
                               CustomerService customerService, EmployeeRepository employeeRepository) {
        this.accountRepository = accountRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.categoryService = categoryService;
        this.bookService = bookService;
        this.customerService = customerService;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/dashboard/view6")
    public ResponseEntity<ApiResponse<View6DTO>> getView6() {
        ApiResponse<View6DTO> response = new ApiResponse<>();
        long tongSoLuongKhachHang = customerService.countCustomer();
        long tongSoLuongSach = bookService.countBook();
        long tongSoLuongDonHang = orderRepository.count();
        long tongSoLuongNhanVien = employeeRepository.count();
        View6DTO view6DTO = new View6DTO(tongSoLuongKhachHang, tongSoLuongSach, tongSoLuongDonHang, tongSoLuongNhanVien);
        response.setData(view6DTO);
        response.setMessage("Get view6 success");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthly-revenue")
    public ResponseEntity<ApiResponse<List<ReqMonthlyRevenue>>> getMonthlyRevenue(@RequestParam int year) {
        ApiResponse<List<ReqMonthlyRevenue>> response = new ApiResponse();
        response.setData(orderService.getMonthlyRevenueByYear(year));
        response.setMessage("Get monthly revenue success");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard/view1")
    public ResponseEntity<ApiResponse<List<View1DTO>>> getView1() {
        ApiResponse<List<View1DTO>> response = new ApiResponse<>();
        response.setData(categoryService.getView1());
        response.setMessage("Get view1 success");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard/get-top5-books-sold")
    public ResponseEntity<ApiResponse<List<SoldBookDTO>>> getTop5BooksSold() {
        ApiResponse<List<SoldBookDTO>> response = new ApiResponse<>();
        response.setData(bookService.getTop5BooksSold());
        response.setMessage("Get top 5 books sold success");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard/view9")
    public ResponseEntity<ApiResponse<List<View9DTO>>> getView9() {
        ApiResponse<List<View9DTO>> response = new ApiResponse<>();
        response.setData(customerService.getView9());
        response.setMessage("Get view9 success");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

}
