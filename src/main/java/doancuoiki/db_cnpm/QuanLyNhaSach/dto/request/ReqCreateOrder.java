package doancuoiki.db_cnpm.QuanLyNhaSach.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReqCreateOrder {
    private String receiverName;
    private String email;
    private String receiverAddress;
    private String receiverPhone;
    private double totalPrice;
    private long statusId;
    private List<ReqCreateOderItem> orderItems;




}
