package doancuoiki.db_cnpm.QuanLyNhaSach.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
public class ResUpdateAccountDTO {
    private long id;
    private String username;
    private String email;
    private String phone;
    private String avatar;
    private Instant updatedAt;

}
