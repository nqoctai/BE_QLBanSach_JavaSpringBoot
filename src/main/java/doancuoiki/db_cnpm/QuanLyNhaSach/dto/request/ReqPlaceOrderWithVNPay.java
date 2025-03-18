package doancuoiki.db_cnpm.QuanLyNhaSach.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqPlaceOrderWithVNPay {
    private long accountId;
    private String name;
    private String address;
    private String phone;
    private String email;
    private double totalPrice;

    private String vnp_txn_ref;
}
