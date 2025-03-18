package doancuoiki.db_cnpm.QuanLyNhaSach.services;


import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Supplier;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Supply;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResultPaginationDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.SupplierRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.SupplyRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    private final SupplierRepository supplierRepository;

    private final SupplyRepository supplyRepository;

    public SupplierService(SupplierRepository supplierRepository, SupplyRepository supplyRepository) {
        this.supplierRepository = supplierRepository;
        this.supplyRepository = supplyRepository;
    }

    public List<Supplier> getAllSupplier(){
        return supplierRepository.findAll();
    }

    public Supplier getSupplierById(Long id){
        return supplierRepository.findById(id).orElse(null);
    }

    public ResultPaginationDTO getAllSupplierWithPagination(Specification<Supplier> spec, Pageable pageable) {
        Page<Supplier> pageSuppliers = this.supplierRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageSuppliers.getTotalPages());
        meta.setTotal(pageSuppliers.getTotalElements());
        rs.setMeta(meta);
        // List<ResAccountDTO> listAccountDTO = pageAccounts.map(account ->
        // convertToResAccountDTO(account)).getContent();
        List<Supplier> listBook = pageSuppliers.getContent();
        rs.setResult(listBook);
        return rs;
    }

    public Supplier createSupplier(Supplier supplier) throws AppException {
        boolean isExist = supplierRepository.existsByName(supplier.getName());
        if(isExist){
            throw new AppException("Supplier name is already exist!");
        }
        boolean isExistEmail = supplierRepository.existsByEmail(supplier.getEmail());
        if(isExistEmail){
            throw new AppException("Supplier email is already exist!");
        }
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Supplier supplier) throws AppException {
        Supplier supplierUpdate = supplierRepository.findById(supplier.getId()).orElse(null);
        if(supplierUpdate == null){
            throw new AppException("Supplier not found!");
        }
        boolean isExist = supplierRepository.existsByName(supplier.getName());
        if(isExist && !supplierUpdate.getName().equals(supplier.getName())){
            throw new AppException("Supplier name is already exist!");
        }
        supplierUpdate.setName(supplier.getName());
        supplierUpdate.setAddress(supplier.getAddress());
        supplierUpdate.setPhone(supplier.getPhone());
        boolean isExistEmail = supplierRepository.existsByEmail(supplier.getEmail());
        if(isExistEmail && !supplierUpdate.getEmail().equals(supplier.getEmail())){
            throw new AppException("Supplier email is already exist!");
        }
        supplierUpdate.setEmail(supplier.getEmail());
        return supplierRepository.save(supplierUpdate);
    }

    public void deleteSupplier(Long id) throws AppException {
        Supplier supplier = supplierRepository.findById(id).orElse(null);
        if(supplier == null){
            throw new AppException("Supplier not found!");
        }
        List<Supply> listSupply = supplier.getSupplies();
        for (Supply supply : listSupply) {
            supplyRepository.deleteById(supply.getId());
        }
        supplierRepository.delete(supplier);
    }


}
