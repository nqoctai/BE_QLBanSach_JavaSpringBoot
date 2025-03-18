package doancuoiki.db_cnpm.QuanLyNhaSach.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.SecurityUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
public class OrderShippingEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String createdBy;
    private Instant createdAt;

    @Column(name = "note", columnDefinition = "nvarchar(255)")
    private String note;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order; // Liên kết với bảng Order

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private ShippingStatus shippingStatus;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.createdAt = Instant.now();
    }
}
