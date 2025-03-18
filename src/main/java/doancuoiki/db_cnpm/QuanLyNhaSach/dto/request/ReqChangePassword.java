package doancuoiki.db_cnpm.QuanLyNhaSach.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqChangePassword {
    private String email;
    private String oldPassword;
    private String newPassword;
}
