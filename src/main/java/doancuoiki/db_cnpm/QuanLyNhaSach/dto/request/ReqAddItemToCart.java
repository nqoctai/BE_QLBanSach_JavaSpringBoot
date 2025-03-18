package doancuoiki.db_cnpm.QuanLyNhaSach.dto.request;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReqAddItemToCart {
    private String email;
    private long bookId;
    private int quantity;
}
