package doancuoiki.db_cnpm.QuanLyNhaSach.dto.request;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUpdateAccountDTO {
    private long id;
    private String email;
    private String username;
    private String phone;
    private String avatar;
    private Role role;
}
