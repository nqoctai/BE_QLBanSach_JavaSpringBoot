package doancuoiki.db_cnpm.QuanLyNhaSach.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    boolean existsByEmail(String email);

    boolean existsByGoogleId(String googleId);

    Optional<Account> findByGoogleId(String googleId);

    Account findByEmail(String email);

    // Account findByRefreshTokenAndEmail(String refreshToken, String email);

}
