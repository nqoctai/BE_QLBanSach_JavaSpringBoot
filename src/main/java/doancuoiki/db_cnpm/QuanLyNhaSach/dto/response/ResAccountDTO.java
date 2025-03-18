package doancuoiki.db_cnpm.QuanLyNhaSach.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResAccountDTO {
    private long id;
    private String email;
    private String username;
    private String phone;
    private String avatar;
    private Instant updatedAt;
    private Instant createdAt;

    private RoleAccount role;

    private CustomerAccount customer;

    private EmployeeAccount employee;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoleAccount {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CustomerAccount {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmployeeAccount {
        private long id;
        private String name;
    }

}
