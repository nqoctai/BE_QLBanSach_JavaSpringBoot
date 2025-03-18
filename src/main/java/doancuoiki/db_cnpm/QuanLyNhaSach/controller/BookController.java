package doancuoiki.db_cnpm.QuanLyNhaSach.controller;

import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ViewDB.SoldBookDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ViewDB.View7DTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.turkraft.springfilter.boot.Filter;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Account;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Book;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ApiResponse;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResultPaginationDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.BookService;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/book")
    public ResponseEntity<ApiResponse<Book>> createBook(@Valid @RequestBody Book rqBook) {
        Book res = bookService.createBook(rqBook);
        ApiResponse<Book> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Thêm sách thành công");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/books")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllBookWithPagination(@Filter Specification<Book> spec,
            Pageable pageable) {
        ResultPaginationDTO res = bookService.getAllBookWithPagination(spec, pageable);
        ApiResponse<ResultPaginationDTO> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Lấy danh sách sách thành công");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/booksNoPagination")
    public ResponseEntity<ApiResponse<List<Book>>> getAllBook() {
        List<Book> res = bookService.getAllBook();
        ApiResponse<List<Book>> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Lấy danh sách sách thành công");
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<ApiResponse<Book>> updateBook(@PathVariable("id") Long id, @Valid @RequestBody Book rqBook) {
        Book res = bookService.updateBook(id, rqBook);
        ApiResponse<Book> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Cập nhật sách thành công");
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<ApiResponse<Book>> deleteBook(@PathVariable("id") Long id) throws AppException {
        boolean res = bookService.checkBookExist(id);
        if (!res) {
            throw new AppException("Sách không tồn tại");
        }
        bookService.deleteBook(id);
        ApiResponse<Book> response = new ApiResponse<>();
        response.setData(null);
        response.setMessage("Xóa sách thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<ApiResponse<Book>> getBookById(@PathVariable("id") Long id) throws AppException {
        boolean checkBookExist = bookService.checkBookExist(id);
        if (!checkBookExist) {
            throw new AppException("Sách không tồn tại");
        }
        Book res = bookService.getBookById(id);
        ApiResponse<Book> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Lấy thông tin sách thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok().body(response);
    }



}
