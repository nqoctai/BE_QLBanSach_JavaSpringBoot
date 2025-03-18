package doancuoiki.db_cnpm.QuanLyNhaSach.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqCreateAccountDTO {
    private String username;
    private String email;
    private String password;
    private String phone;
}
