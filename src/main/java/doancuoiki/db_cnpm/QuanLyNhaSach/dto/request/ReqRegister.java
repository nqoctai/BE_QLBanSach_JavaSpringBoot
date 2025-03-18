package doancuoiki.db_cnpm.QuanLyNhaSach.dto.request;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReqRegister {
    private String username;
    private String password;
    private String email;
    private String phone;
}
