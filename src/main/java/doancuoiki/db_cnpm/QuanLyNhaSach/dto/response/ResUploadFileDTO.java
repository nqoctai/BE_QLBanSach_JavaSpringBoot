package doancuoiki.db_cnpm.QuanLyNhaSach.dto.response;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUploadFileDTO {
    private String fileName;
    private Instant uploadedAt;
}
