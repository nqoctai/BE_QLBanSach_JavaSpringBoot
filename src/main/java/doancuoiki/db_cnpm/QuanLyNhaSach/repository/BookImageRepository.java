package doancuoiki.db_cnpm.QuanLyNhaSach.repository;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Long> {

}
