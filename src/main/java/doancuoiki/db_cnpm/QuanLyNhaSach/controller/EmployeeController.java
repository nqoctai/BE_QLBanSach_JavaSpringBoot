package doancuoiki.db_cnpm.QuanLyNhaSach.controller;


import com.turkraft.springfilter.boot.Filter;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Customer;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Employee;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ApiResponse;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResultPaginationDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.EmployeeService;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/employee")
    public ResponseEntity<ApiResponse<Employee>> createEmployee(@RequestBody @Valid Employee rqEmployee)
            throws AppException {

        boolean emailExist = employeeService.checkEmailExist(rqEmployee.getEmail());
        if (emailExist) {
            throw new AppException("Email đã tồn tại");
        }
        Employee res = employeeService.createEmployee(rqEmployee);
        ApiResponse<Employee> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Tạo nhân viên thành công");
        response.setStatus(HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/employees")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllEmployeeWithPagination(
            @Filter Specification<Employee> spec, Pageable pageable) throws AppException {
        ResultPaginationDTO res = employeeService.getAllEmployeeWithPagination(spec, pageable);
        ApiResponse<ResultPaginationDTO> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Lấy danh sách nhân viên thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/employee")
    public ResponseEntity<ApiResponse<Employee>> updateEmployee(@Valid @RequestBody Employee rqEmployee)
            throws AppException {
        boolean isExist = employeeService.checkEmployeeExist(rqEmployee.getId());
        if (!isExist) {
            throw new AppException("Nhân viên này không tồn tại");
        }
        Employee res = employeeService.updateEmployee(rqEmployee);
        ApiResponse<Employee> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Cập nhật nhân viên thành công");
        response.setStatus(HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<ApiResponse<Employee>> deleteEmployee(@PathVariable("id") Long id) throws AppException {
        Employee res = employeeService.getEmployeeById(id);
        if (res == null) {
            throw new AppException("Id không tồn tại");
        }
        employeeService.deleteEmployee(id);
        ApiResponse<Employee> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Xóa nhân viên thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/employee/{id}")
    public ResponseEntity<ApiResponse<Employee>> getEmployeeById(@PathVariable("id") long id) throws AppException {
        boolean isExist = employeeService.checkEmployeeExist(id);
        if (!isExist) {
            throw new AppException("Id không tồn tại");
        }
        Employee res = employeeService.getEmployeeById(id);
        ApiResponse<Employee> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Lấy thông tin nhân viên thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }


}
