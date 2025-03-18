package doancuoiki.db_cnpm.QuanLyNhaSach.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "import_receipt_details")
@Getter
@Setter
public class ImportReceiptDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "import_receipt_id")
    @JsonIgnore
    private ImportReceipt importReceipt;

    @ManyToOne
    @JoinColumn(name = "supply_id")
    private Supply supply;

    private int quantity;

    private double totalPrice;


}
