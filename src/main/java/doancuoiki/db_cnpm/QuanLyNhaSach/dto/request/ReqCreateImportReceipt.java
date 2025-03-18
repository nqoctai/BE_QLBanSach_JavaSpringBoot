package doancuoiki.db_cnpm.QuanLyNhaSach.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ReqCreateImportReceipt {
    private String employeeEmail;
    private double totalPrice;
    private List<ReqCreateImportReceiptItem> importReceiptItems = new ArrayList<>();

}
