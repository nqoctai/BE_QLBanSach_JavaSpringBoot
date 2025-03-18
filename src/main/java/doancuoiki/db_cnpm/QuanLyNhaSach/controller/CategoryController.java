package doancuoiki.db_cnpm.QuanLyNhaSach.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Category;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ApiResponse;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.CategoryService;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<Category>>> getAllCategory() {
        List<Category> categories = categoryService.getAllCategory();
        ApiResponse<List<Category>> response = new ApiResponse<>();
        response.setData(categories);
        response.setMessage("Lấy category thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }
}
