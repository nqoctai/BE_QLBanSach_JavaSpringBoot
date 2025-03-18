package doancuoiki.db_cnpm.QuanLyNhaSach.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
public class ResCreateAccountDTO {
    private String email;
    private String username;
    private Instant createdAt;
}
