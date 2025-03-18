package doancuoiki.db_cnpm.QuanLyNhaSach.domain;

import java.util.List;
import java.time.Instant;

import doancuoiki.db_cnpm.QuanLyNhaSach.util.SecurityUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String thumbnail;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String mainText;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String author;

    private double price;

    private long sold;

    private long quantity;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;



    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<BookImage> bookImages;

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
