package doancuoiki.db_cnpm.QuanLyNhaSach.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUpdateToCart {
    private long cartId;
    private long cartItemId;
    private int quantity;

}
