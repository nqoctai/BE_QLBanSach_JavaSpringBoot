package doancuoiki.db_cnpm.QuanLyNhaSach.dto.ViewDB;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SoldBookDTO {
    private Long bookID;
    private String bookName;
    private Long totalQuantity;
}
