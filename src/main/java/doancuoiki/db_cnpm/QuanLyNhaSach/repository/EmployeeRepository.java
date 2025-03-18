package doancuoiki.db_cnpm.QuanLyNhaSach.repository;


import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Customer;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    boolean existsByEmail(String email);
    Employee findByEmail(String email);
}
