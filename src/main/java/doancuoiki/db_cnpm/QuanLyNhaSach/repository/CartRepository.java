package doancuoiki.db_cnpm.QuanLyNhaSach.repository;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Account;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Cart;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByCustomer(Customer customer);

    void deleteById(long id);
}
