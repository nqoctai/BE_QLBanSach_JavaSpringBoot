package doancuoiki.db_cnpm.QuanLyNhaSach.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "shipping_status")
public class ShippingStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "status", columnDefinition = "nvarchar(255)")
    private String status;

}
