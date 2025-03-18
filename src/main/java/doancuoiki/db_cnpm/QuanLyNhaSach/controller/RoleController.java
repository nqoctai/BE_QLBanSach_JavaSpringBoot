package doancuoiki.db_cnpm.QuanLyNhaSach.controller;

import com.turkraft.springfilter.boot.Filter;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResultPaginationDTO;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties.Http;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Role;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ApiResponse;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.RoleService;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/role")
    public ResponseEntity<ApiResponse<Role>> createRole(@Valid @RequestBody Role role) throws AppException {
        boolean isNameRoleExist = roleService.isNameRoleExist(role.getName());
        if (isNameRoleExist) {
            throw new AppException("Tên role đã tồn tại");
        }
        Role newRole = roleService.createRole(role);
        ApiResponse<Role> response = new ApiResponse<Role>();

        response.setData(newRole);
        response.setMessage("Tạo role thành công");
        response.setStatus(HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/role")
    public ResponseEntity<ApiResponse<Role>> update(@Valid @RequestBody Role r) throws AppException {
        // check id
        if (this.roleService.getRoleById(r.getId()) == null) {
            throw new AppException("Role với id = " + r.getId() + " không tồn tại");
        }
        Role role = this.roleService.update(r);
        ApiResponse<Role> response = new ApiResponse<Role>();
        response.setData(role);
        response.setMessage("Cập nhật role thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/role/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") long id) throws AppException {
        // check id
        if (this.roleService.getRoleById(id) == null) {
            throw new AppException("Role với id = " + id + " không tồn tại");
        }
        this.roleService.delete(id);
        ApiResponse<Void> response = new ApiResponse<Void>();
        response.setMessage("Xóa role thành công");
        response.setStatus(HttpStatus.OK.value());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/roles")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getPermissions(
            @Filter Specification<Role> spec, Pageable pageable) {
        ResultPaginationDTO result = this.roleService.getRoles(spec, pageable);
        ApiResponse<ResultPaginationDTO> response = new ApiResponse<ResultPaginationDTO>();
        response.setData(result);
        response.setMessage("Lấy danh sách role thành công");
        response.setStatus(HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/role")
    public ResponseEntity<ApiResponse<List<Role>>> getAllRole() {
        List<Role> listRole = roleService.getAllRole();
        ApiResponse<List<Role>> response = new ApiResponse<List<Role>>();
        response.setData(listRole);
        response.setMessage("Lấy danh sách role thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/role/{id}")
    public ResponseEntity<ApiResponse<Role>> getById(@PathVariable("id") long id) throws AppException {

        Role role = this.roleService.getRoleById(id);
        if (role == null) {
            throw new AppException("Resume với id = " + id + " không tồn tại");
        }

        ApiResponse<Role> response = new ApiResponse<Role>();
        response.setData(role);
        response.setMessage("Lấy role thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok().body(response);
    }
}
