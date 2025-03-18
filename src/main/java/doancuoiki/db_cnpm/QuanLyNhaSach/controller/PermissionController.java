package doancuoiki.db_cnpm.QuanLyNhaSach.controller;


import com.turkraft.springfilter.boot.Filter;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Permission;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ApiResponse;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResultPaginationDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.PermissionService;
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
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    public ResponseEntity<ApiResponse<Permission>> create(@Valid @RequestBody Permission p) throws AppException {
        // check exist
        if (this.permissionService.isPermissionExist(p)) {
            throw new AppException("Permission đã tồn tại.");
        }
        Permission permission = this.permissionService.create(p);
        ApiResponse<Permission> apiResponse = new ApiResponse<>();
        apiResponse.setData(permission);
        apiResponse.setMessage("Create permission successfully");
        apiResponse.setStatus(HttpStatus.CREATED.value());
        // create new permission
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping("/permissions")
    public ResponseEntity<ApiResponse<Permission>> update(@Valid @RequestBody Permission p) throws AppException {
        // check exist by id
        if (this.permissionService.fetchById(p.getId()) == null) {
            throw new AppException("Permission với id = " + p.getId() + " không tồn tại.");
        }

        // check exist by module, apiPath and method
        if (this.permissionService.isPermissionExist(p)) {
            // check name
            if (this.permissionService.isSameName(p))
                throw new AppException("Permission đã tồn tại.");
        }

        Permission permission = this.permissionService.update(p);
        ApiResponse<Permission> apiResponse = new ApiResponse<>();
        apiResponse.setData(permission);
        apiResponse.setMessage("Update permission successfully");
        apiResponse.setStatus(HttpStatus.OK.value());
        // update permission
        return ResponseEntity.ok().body(apiResponse);
    }

    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("id") long id) throws AppException {
        // check exist by id
        if (this.permissionService.fetchById(id) == null) {
            throw new AppException("Permission với id = " + id + " không tồn tại.");
        }
        this.permissionService.delete(id);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setMessage("Delete permission successfully");
        apiResponse.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok().body(apiResponse);
    }

    @GetMapping("/permissions")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getPermissions(
            @Filter Specification<Permission> spec, Pageable pageable) {

        ResultPaginationDTO resultPaginationDTO = this.permissionService.getPermissions(spec, pageable);
        ApiResponse<ResultPaginationDTO> apiResponse = new ApiResponse<>();
        apiResponse.setData(resultPaginationDTO);
        apiResponse.setMessage("Lấy danh sách permission thành công");
        apiResponse.setStatus(HttpStatus.OK.value());

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/permissions-no-pagination")
    public ResponseEntity<ApiResponse<List<Permission>>> getPermissionsNoPagination() {

        List<Permission> listPermissions = this.permissionService.getAllPermissionNoPagination();
        ApiResponse<List<Permission>> apiResponse = new ApiResponse<>();
        apiResponse.setData(listPermissions);
        apiResponse.setMessage("Lấy danh sách permission thành công");
        apiResponse.setStatus(HttpStatus.OK.value());

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/permissions/{id}")
    public ResponseEntity<ApiResponse<Permission>> getPermissionById(@PathVariable("id") long id) throws AppException {
        Permission permission = this.permissionService.fetchById(id);
        if (permission == null) {
            throw new AppException("Permission với id = " + id + " không tồn tại.");
        }
        ApiResponse<Permission> apiResponse = new ApiResponse<>();
        apiResponse.setData(permission);
        apiResponse.setMessage("Lấy permission thành công");
        apiResponse.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/permissions-name")
    public ResponseEntity<ApiResponse<Permission>> fetchPermissionByName(@RequestParam String name) throws AppException {
        Permission permission = this.permissionService.fetchByName(name);
        if (permission == null) {
            throw new AppException("Permission với tên = " + name + " không tồn tại.");
        }
        ApiResponse<Permission> apiResponse = new ApiResponse<>();
        apiResponse.setData(permission);
        apiResponse.setMessage("Lấy permission thành công");
        apiResponse.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(apiResponse);
    }
}
