package doancuoiki.db_cnpm.QuanLyNhaSach.services;

import java.util.ArrayList;
import java.util.List;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.BookImage;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ViewDB.SoldBookDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ViewDB.View6DTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ViewDB.View7DTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.BookImageRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Account;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Book;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Category;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResAccountDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResultPaginationDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.BookRepository;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final CategoryService categoryService;
    private final BookImageRepository bookImageRepository;

    public BookService(BookRepository bookRepository, CategoryService categoryService,
            BookImageRepository bookImageRepository) {
        this.bookRepository = bookRepository;
        this.categoryService = categoryService;
        this.bookImageRepository = bookImageRepository;
    }

    public Book createBook(Book book) {
        // Tải category từ categoryService và thiết lập vào book
        Category category = categoryService.getCategoryById(book.getCategory().getId());
        book.setCategory(category);

        // Lưu tạm book để tạo ID (tránh lỗi transient)
        Book savedBook = bookRepository.save(book);

        // Thêm các book images vào danh sách của book
        List<String> slider = book.getBookImages().stream().map(image -> image.getUrl()).toList();
        List<BookImage> bookImages = new ArrayList<>();
        for (String image : slider) {
            BookImage bookImage = new BookImage();
            bookImage.setUrl(image);
            bookImage.setBook(savedBook);
            BookImage bimg = bookImageRepository.save(bookImage);
            bookImages.add(bimg);
        }
        savedBook.setBookImages(bookImages);

        // Lưu lại book với các book images đã được thiết lập
        return bookRepository.save(savedBook);
    }

    public ResultPaginationDTO getAllBookWithPagination(Specification<Book> spec, Pageable pageable) {
        Page<Book> pageBooks = this.bookRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageBooks.getTotalPages());
        meta.setTotal(pageBooks.getTotalElements());
        rs.setMeta(meta);
        // List<ResAccountDTO> listAccountDTO = pageAccounts.map(account ->
        // convertToResAccountDTO(account)).getContent();
        List<Book> listBook = pageBooks.getContent();
        rs.setResult(listBook);
        return rs;
    }

    public Book updateBook(long id, Book rqBook) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            return null;
        }

        // Cập nhật các trường cơ bản
        book.setThumbnail(rqBook.getThumbnail());
        book.setMainText(rqBook.getMainText());
        book.setAuthor(rqBook.getAuthor());
        book.setPrice(rqBook.getPrice());
        book.setSold(rqBook.getSold());
        book.setQuantity(rqBook.getQuantity());

        // Cập nhật category
        Category category = categoryService.getCategoryById(rqBook.getCategory().getId());
        book.setCategory(category);

        // Cập nhật book images
        List<BookImage> existingImages = book.getBookImages();
        bookImageRepository.deleteAll(existingImages); // Xóa các hình ảnh cũ

        List<String> slider = rqBook.getBookImages().stream().map(image -> image.getUrl()).toList();
        List<BookImage> bookImages = new ArrayList<>();
        for (String image : slider) {
            BookImage bookImage = new BookImage();
            bookImage.setUrl(image);
            bookImage.setBook(book);
            BookImage bimg = bookImageRepository.save(bookImage);
            bookImages.add(bimg);
        }
        book.setBookImages(bookImages);
        return bookRepository.save(book);
    }

    public boolean checkBookExist(Long id) {
        return bookRepository.existsById(id);
    }

    public void deleteBook(Long id) throws AppException {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            throw new AppException("Sách không tồn tại");
        }
        List<BookImage> bookImages = book.getBookImages();
        bookImages.forEach(bookImage -> {
            bookImageRepository.deleteById(bookImage.getId());
        });
        bookRepository.deleteById(id);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Book> getAllBook() {
        return bookRepository.findAll();
    }

    public long countBook() {
        return bookRepository.count();
    }

    public List<SoldBookDTO> getTop5BooksSold() {
        List<Book> books = bookRepository.findTop5ByOrderBySoldDesc();
        List<SoldBookDTO> soldBookDTOS = new ArrayList<>();
        for (Book book : books) {
            SoldBookDTO soldBookDTO = new SoldBookDTO();
            soldBookDTO.setBookID(book.getId());
            soldBookDTO.setTotalQuantity(book.getSold());
            soldBookDTO.setBookName(book.getMainText());
            soldBookDTOS.add(soldBookDTO);
        }
        return soldBookDTOS;
    }

}
