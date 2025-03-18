package doancuoiki.db_cnpm.QuanLyNhaSach.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqOrderItem {
    private double price;
    private int quantity;
    private long bookId;

}
