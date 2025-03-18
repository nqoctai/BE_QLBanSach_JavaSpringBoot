package doancuoiki.db_cnpm.QuanLyNhaSach.config;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Account;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Customer;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Role;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.AccountRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.CustomerRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.RoleRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.AccountService;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.CustomerService;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class OAuth2Config {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Bean
    @Transactional
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        DefaultOAuth2UserService defaultService = new DefaultOAuth2UserService();
        return (userRequest) -> {
            OAuth2User oauth2User = defaultService.loadUser(userRequest);

            // Lấy thông tin từ Google
            String googleId = oauth2User.getAttribute("sub");
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");
            String picture = oauth2User.getAttribute("picture");

            // Tìm account hiện có
            Optional<Account> existingAccount = accountRepository.findByGoogleId(googleId);

            if (existingAccount.isPresent()) {
                // Cập nhật thông tin nếu tài khoản đã tồn tại
                Account account = existingAccount.get();
                account.setEmail(email);
                account.setAvatar(picture);
                accountRepository.save(account);

                // Cập nhật thông tin customer
                Customer customer = customerRepository.findByAccount(account);
                if (customer != null) {
                    customer.setName(name);
                    customer.setEmail(email);
                    customerRepository.save(customer);
                }

                return oauth2User;
            } else {
                // Tạo mới Account
                Account newAccount = new Account();
                newAccount.setGoogleId(googleId);
                newAccount.setEmail(email);
                newAccount.setUsername(email);
                newAccount.setPassword("oauth2password"); // Không dùng để đăng nhập
                newAccount.setAvatar(picture);

                // Lấy role CUSTOMER
                Role userRole = roleRepository.findByName("CUSTOMER");
                if (userRole == null) {
                    userRole = new Role();
                    userRole.setName("CUSTOMER");
                    roleRepository.save(userRole);
                }

                newAccount.setRole(userRole);

                // Lưu account để lấy ID
                newAccount = accountRepository.save(newAccount);

                // Tạo customer mới và liên kết với account
                Customer newCustomer = new Customer();
                newCustomer.setName(name);
                newCustomer.setEmail(email);
                newCustomer.setIsOauthUser(true);
                newCustomer.setBirthday(LocalDate.now());
                newCustomer.setAccount(newAccount);

                // Lưu customer
                newCustomer = customerRepository.save(newCustomer);

                // Liên kết account với customer
                newAccount.setCustomer(newCustomer);
                accountRepository.save(newAccount);

                return oauth2User;
            }
        };
    }
}