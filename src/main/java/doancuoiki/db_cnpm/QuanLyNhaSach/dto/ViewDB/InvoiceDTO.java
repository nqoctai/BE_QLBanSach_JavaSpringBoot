package doancuoiki.db_cnpm.QuanLyNhaSach.dto.ViewDB;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;


@Getter
@Setter
public class InvoiceDTO {
    private Long id;
    private String createdAt;
    private String receiverName;
    private String receiverAddress;
    private String receiverPhone;
    private String receiverEmail;
    private Double totalPrice;
}
