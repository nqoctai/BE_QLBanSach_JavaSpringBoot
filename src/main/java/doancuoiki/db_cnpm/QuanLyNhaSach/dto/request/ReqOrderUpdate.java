package doancuoiki.db_cnpm.QuanLyNhaSach.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqOrderUpdate {
    private long id;
    private long statusId;
    private String note;
}
