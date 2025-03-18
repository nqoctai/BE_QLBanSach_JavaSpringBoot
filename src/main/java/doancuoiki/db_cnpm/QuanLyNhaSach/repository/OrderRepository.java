package doancuoiki.db_cnpm.QuanLyNhaSach.repository;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Customer;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    List<Order> findByCustomer(Customer customer);

    Order findByVnpTxnRef(String vnp_txn_ref);

    List<Order> findByCreatedAtBetween(Instant start, Instant end);

}
