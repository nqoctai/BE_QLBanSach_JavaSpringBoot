package doancuoiki.db_cnpm.QuanLyNhaSach.services;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Book;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Supplier;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Supply;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqGetSupply;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResultPaginationDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.SupplyRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplyService {
    private final SupplyRepository supplyRepository;

    private final BookService bookService;

    private final SupplierService supplierService;

    public SupplyService(SupplyRepository supplyRepository, BookService bookService,SupplierService supplierService ) {
        this.supplyRepository = supplyRepository;
        this.bookService = bookService;
        this.supplierService = supplierService;
    }

    public Supply getSupplyBySupplierIdAndBookId(ReqGetSupply reqGetSupply) throws AppException {

        Supplier supplier = supplierService.getSupplierById(reqGetSupply.getSupplierID());
        if(supplier==null)
        {
            throw new AppException("Supplier not found");
        }
        Book book = bookService.getBookById(reqGetSupply.getBookID());
        if(book==null)
        {
            throw new AppException("Book not found");
        }
        Supply supply = supplyRepository.findBySupplierAndBook(supplier, book);
        if(supply==null)
        {
            throw new AppException("Supply not found");
        }
        return supply;
    }

    public Supply getSupplyById(Long id){
        return supplyRepository.findById(id).orElse(null);
    }

    public ResultPaginationDTO getAllSupply(Specification<Supply> spec, Pageable pageable) {
        Page<Supply> pageSupplys = this.supplyRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageSupplys.getTotalPages());
        meta.setTotal(pageSupplys.getTotalElements());
        rs.setMeta(meta);
        // List<ResAccountDTO> listAccountDTO = pageAccounts.map(account ->
        // convertToResAccountDTO(account)).getContent();
        List<Supply> listBook = pageSupplys.getContent();
        rs.setResult(listBook);
        return rs;
    }

    public Supply createSupply(Supply supply) throws AppException {
        Book book = bookService.getBookById(supply.getBook().getId());
        if(book==null)
        {
            throw new AppException("Book not found");
        }
        Supplier supplier = supplierService.getSupplierById(supply.getSupplier().getId());
        if(supplier==null)
        {
            throw new AppException("Supplier not found");
        }
        boolean checkExistSupplierAndBook = supplyRepository.existsBySupplierAndBook(supplier,book );
        if(checkExistSupplierAndBook)
        {
            throw new AppException("Supply already exists");
        }
        supply.setBook(book);
        supply.setSupplier(supplier);
        return supplyRepository.save(supply);
    }

    public Supply deleteSupply(Long id) throws AppException {
        Supply supply = supplyRepository.findById(id).orElse(null);
        if(supply==null)
        {
            throw new AppException("Supply not found");
        }
        supplyRepository.delete(supply);
        return supply;
    }

    public Supply updateSupply(Supply supply) throws AppException {
        Supply supply1 = supplyRepository.findById(supply.getId()).orElse(null);
        if(supply1==null)
        {
            throw new AppException("Supply not found");
        }
        Book book = bookService.getBookById(supply.getBook().getId());
        if(book==null)
        {
            throw new AppException("Book not found");
        }
        Supplier supplier = supplierService.getSupplierById(supply.getSupplier().getId());
        if(supplier==null)
        {
            throw new AppException("Supplier not found");
        }

        boolean checkExistSupplierAndBook = supplyRepository.existsBySupplierAndBook(supplier,book );
        if(checkExistSupplierAndBook && supply1.getBook().getId()!=book.getId() && supply1.getSupplier().getId()!=supplier.getId())
        {
            throw new AppException("Supply already exists");
        }
        supply1.setBook(book);
        supply1.setSupplier(supplier);
        supply1.setSupplyPrice(supply.getSupplyPrice());
        return supplyRepository.save(supply1);
    }


}
