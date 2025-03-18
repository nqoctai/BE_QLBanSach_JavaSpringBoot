package doancuoiki.db_cnpm.QuanLyNhaSach.dto.response;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Book;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Supplier;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ResSupply {
    private Long id;
    private Book book;
    private Supplier supplier;
    private double supplyPrice;
}
