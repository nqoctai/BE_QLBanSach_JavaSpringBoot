package doancuoiki.db_cnpm.QuanLyNhaSach.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCreateOderItem {
    private long bookId;
    private int quantity;
}
