package doancuoiki.db_cnpm.QuanLyNhaSach.domain;

import doancuoiki.db_cnpm.QuanLyNhaSach.util.SecurityUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double totalPrice;

    private String receiverEmail;

    @Column(name = "receiver_name", columnDefinition = "nvarchar(255)")
    private String receiverName;

    @Column(name = "receiver_address", columnDefinition = "nvarchar(255)")
    private String receiverAddress;
    private String receiverPhone;

    private String paymentMethod;


    @Column(name = "vnp_txn_ref")
    private String vnpTxnRef;



    private String createdBy;
    private String updatedBy;
    private Instant createdAt;
    private Instant updatedAt;


    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<OrderItem>();

    @OneToMany(mappedBy = "order")
    private List<OrderShippingEvent> orderShippingEvents = new ArrayList<OrderShippingEvent>();

    @PrePersist
    public void handleBeforeCreate() {
        this.createdBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedBy = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";

        this.updatedAt = Instant.now();
    }
}
