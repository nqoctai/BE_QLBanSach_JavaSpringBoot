package doancuoiki.db_cnpm.QuanLyNhaSach.repository;


import doancuoiki.db_cnpm.QuanLyNhaSach.domain.ShippingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingStatusRepository extends JpaRepository<ShippingStatus, Long> {

}
