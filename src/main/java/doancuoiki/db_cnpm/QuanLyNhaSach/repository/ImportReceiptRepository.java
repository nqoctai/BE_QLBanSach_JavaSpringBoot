package doancuoiki.db_cnpm.QuanLyNhaSach.repository;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Employee;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.ImportReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportReceiptRepository extends JpaRepository<ImportReceipt, Long>, JpaSpecificationExecutor<ImportReceipt> {
}
