package doancuoiki.db_cnpm.QuanLyNhaSach.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Account;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Cart;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Customer;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResLoginDTO {

    @JsonProperty("access_token")
    private String accessToken;

    private AccountLogin account;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountLogin {
        private long id;
        private String email;
        private String name;
        private String avatar;
        private String phone;
        private String role;
        private Customer customer;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserGetAccount {
        private AccountLogin account;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountInsideToken {
        private long id;
        private String email;
        private String name;
    }
}
