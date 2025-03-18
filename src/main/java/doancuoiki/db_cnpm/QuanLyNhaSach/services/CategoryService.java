package doancuoiki.db_cnpm.QuanLyNhaSach.services;

import java.util.ArrayList;
import java.util.List;

import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ViewDB.View1DTO;
import org.springframework.stereotype.Service;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Category;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.CategoryRepository;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public List<View1DTO> getView1() {
        List<Category> categories = categoryRepository.findAll();
        List<View1DTO> view1DTOS = new ArrayList<>();
        for (Category category : categories) {
            View1DTO view1DTO = new View1DTO();
            view1DTO.setTheLoai(category.getName());
            view1DTO.setSoLuongBan(category.getBooks().size());
            view1DTOS.add(view1DTO);
        }

        return view1DTOS;
    }

}
