package doancuoiki.db_cnpm.QuanLyNhaSach.services;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.ShippingStatus;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.ShippingStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingStatusService {
    private final ShippingStatusRepository shippingStatusRepository;

    public ShippingStatusService(ShippingStatusRepository shippingStatusRepository) {
        this.shippingStatusRepository = shippingStatusRepository;
    }

    public List<ShippingStatus> getAllShippingStatus() {
        return shippingStatusRepository.findAll();
    }

    public ShippingStatus getShippingStatusById(long id) {
        return shippingStatusRepository.findById(id).orElse(null);
    }

}
