package doancuoiki.db_cnpm.QuanLyNhaSach.services;


import doancuoiki.db_cnpm.QuanLyNhaSach.domain.*;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqCreateImportReceipt;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqCreateImportReceiptItem;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqGetSupply;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqUpdateImportReceipt;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResImportReceipt;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResSupply;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResultPaginationDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.ImportReceiptDetailRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.ImportReceiptRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImportReceiptService {
    private final ImportReceiptRepository importReceiptRepository;

    private final EmployeeService employeeService;

    private final SupplyService supplyService;

    private final ImportReceiptDetailRepository importReceiptDetaiRepository;

    private final BookService bookService;

    public ImportReceiptService(ImportReceiptRepository importReceiptRepository, EmployeeService employeeService,
                                SupplyService supplyService, ImportReceiptDetailRepository importReceiptDetaiRepository, BookService bookService) {
        this.importReceiptRepository = importReceiptRepository;
        this.employeeService = employeeService;
        this.supplyService = supplyService;
        this.importReceiptDetaiRepository = importReceiptDetaiRepository;
        this.bookService = bookService;
    }

    public ImportReceipt createImportReceipt(ReqCreateImportReceipt reqCreateImportReceipt) throws AppException {
        ImportReceipt importReceipt = new ImportReceipt();
        Employee employee = employeeService.getEmployeeByEmail(reqCreateImportReceipt.getEmployeeEmail());
        if(employee != null) {
            importReceipt.setEmployee(employee);
        }
        importReceipt.setTotalAmount(reqCreateImportReceipt.getTotalPrice());
        importReceipt = importReceiptRepository.save(importReceipt);
        for(ReqCreateImportReceiptItem reqCreateImportReceiptItem : reqCreateImportReceipt.getImportReceiptItems()) {
            ImportReceiptDetail importReceiptItem = new ImportReceiptDetail();
            importReceiptItem.setImportReceipt(importReceipt);
            importReceiptItem.setQuantity(reqCreateImportReceiptItem.getQuantity());
            importReceiptItem.setTotalPrice(reqCreateImportReceiptItem.getTotalPrice());
            ReqGetSupply reqGetSupply = new ReqGetSupply();
            reqGetSupply.setBookID(reqCreateImportReceiptItem.getBookId());
            reqGetSupply.setSupplierID(reqCreateImportReceiptItem.getSupplierId());
            Supply supply = supplyService.getSupplyBySupplierIdAndBookId(reqGetSupply);
            if(supply != null) {
                Book book = supply.getBook();
                supply.setBook(book);
                importReceiptItem.setSupply(supply);
            }else
            {
                throw new AppException("Supply not found");
            }
            importReceiptDetaiRepository.save(importReceiptItem);
            importReceipt.getImportReceiptDetails().add(importReceiptItem);
        }
        return importReceiptRepository.save(importReceipt);

    }

    public ResultPaginationDTO getReceiptAllWithPagination(Specification<ImportReceipt> spec, Pageable pageable) {
        Page<ImportReceipt> pageImportReceipt = this.importReceiptRepository.findAll(spec, pageable);


        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageImportReceipt.getTotalPages());
        meta.setTotal(pageImportReceipt.getTotalElements());
        rs.setMeta(meta);
        rs.setResult(pageImportReceipt.getContent());
        return rs;
    }

    public ImportReceipt updateImportReceipt(ReqUpdateImportReceipt reqUpdateImportReceipt) throws AppException {
        ImportReceipt importReceipt = importReceiptRepository.findById(reqUpdateImportReceipt.getId()).orElse(null);
        if(importReceipt == null) {
            throw new AppException("Import receipt not found");
        }
        Employee employee = employeeService.getEmployeeByEmail(reqUpdateImportReceipt.getEmployeeEmail());
        if(employee != null) {
            importReceipt.setEmployee(employee);
        }
        importReceipt.setTotalAmount(reqUpdateImportReceipt.getTotalPrice());
        importReceipt = importReceiptRepository.save(importReceipt);
        List<ImportReceiptDetail> ipDetail = importReceipt.getImportReceiptDetails();
        for(ImportReceiptDetail importReceiptDetail : ipDetail) {
            importReceiptDetaiRepository.delete(importReceiptDetail);
        }
        importReceipt.getImportReceiptDetails().clear();
        for(ReqCreateImportReceiptItem reqCreateImportReceiptItem : reqUpdateImportReceipt.getImportReceiptItems()) {
            ImportReceiptDetail importReceiptItem = new ImportReceiptDetail();
            importReceiptItem.setImportReceipt(importReceipt);
            importReceiptItem.setQuantity(reqCreateImportReceiptItem.getQuantity());
            importReceiptItem.setTotalPrice(reqCreateImportReceiptItem.getTotalPrice());
            ReqGetSupply reqGetSupply = new ReqGetSupply();
            reqGetSupply.setBookID(reqCreateImportReceiptItem.getBookId());
            reqGetSupply.setSupplierID(reqCreateImportReceiptItem.getSupplierId());
            Supply supply = supplyService.getSupplyBySupplierIdAndBookId(reqGetSupply);
            if(supply != null) {
                importReceiptItem.setSupply(supply);
            }else
            {
                throw new AppException("Supply not found");
            }
            importReceiptDetaiRepository.save(importReceiptItem);
            importReceipt.getImportReceiptDetails().add(importReceiptItem);
        }
        return importReceiptRepository.save(importReceipt);
    }



}
