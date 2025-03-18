package doancuoiki.db_cnpm.QuanLyNhaSach.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCreateImportReceiptItem {
    private Long supplierId;
    private Long bookId;
    private int quantity;

    private double totalPrice;
}
