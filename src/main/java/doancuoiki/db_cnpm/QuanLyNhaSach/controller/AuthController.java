package doancuoiki.db_cnpm.QuanLyNhaSach.controller;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Customer;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Role;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqRegister;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.CustomerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Account;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ApiResponse;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqCreateAccountDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqLoginDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResCreateAccountDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResLoginDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.AccountService;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.RoleService;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.SecurityUtil;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

        private final AuthenticationManagerBuilder authenticationManagerBuilder;
        private final SecurityUtil securityUtil;
        private final AccountService accountService;
        private final PasswordEncoder passwordEncoder;
        private final RoleService roleService;
        private final CustomerService customerService;

        @Value("${nqoctai.jwt.refresh-token-validity-in-seconds}")
        private long refreshTokenExpiration;

        public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder, SecurityUtil securityUtil,
                        AccountService accountService, PasswordEncoder passwordEncoder, RoleService roleService, CustomerService customerService) {
                this.authenticationManagerBuilder = authenticationManagerBuilder;
                this.securityUtil = securityUtil;
                this.accountService = accountService;
                this.passwordEncoder = passwordEncoder;
                this.roleService = roleService;
                this.customerService = customerService;
        }

        @PostMapping("/auth/login")
        public ResponseEntity<ApiResponse<ResLoginDTO>> login(@Valid @RequestBody ReqLoginDTO reqLoginDTO) {
                UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
                                reqLoginDTO.getUsername(), reqLoginDTO.getPassword());
                Authentication authentication = authenticationManagerBuilder.getObject().authenticate(loginToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                ResLoginDTO resLoginDTO = new ResLoginDTO();
                Account currentAccountDB = accountService.getUserByEmail(reqLoginDTO.getUsername());

                if (currentAccountDB != null) {
                        ResLoginDTO.AccountLogin accountLogin = new ResLoginDTO.AccountLogin(currentAccountDB.getId(),
                                        currentAccountDB.getEmail(),
                                        currentAccountDB.getUsername(),
                                        currentAccountDB.getAvatar(),
                                        currentAccountDB.getPhone(),
                                        currentAccountDB.getRole().getName(),
                                        currentAccountDB.getCustomer());
                        resLoginDTO.setAccount(accountLogin);
                }
                String access_token = this.securityUtil.createAccessToken(authentication.getName(), resLoginDTO);

                resLoginDTO.setAccessToken(access_token);

                String refresh_token = this.securityUtil.createRefreshToken(reqLoginDTO.getUsername(), resLoginDTO);
                ResponseCookie resCookies = ResponseCookie
                                .from("refresh_token", refresh_token)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(refreshTokenExpiration)
                                .build();

                ApiResponse<ResLoginDTO> response = new ApiResponse<ResLoginDTO>();
                response.setData(resLoginDTO);
                response.setMessage("Đăng nhập thành công");
                response.setStatus(HttpStatus.OK.value());

                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                                .body(response);
        }

        @GetMapping("/auth/account")
        public ResponseEntity<ApiResponse<ResLoginDTO.UserGetAccount>> getAccount() {
                String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get()
                                : "";

                Account currentAccountDB = accountService.getUserByEmail(email);
                ResLoginDTO.AccountLogin accountLogin = new ResLoginDTO.AccountLogin();
                ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();

                if (currentAccountDB != null) {
                        accountLogin.setId(currentAccountDB.getId());
                        accountLogin.setEmail(currentAccountDB.getEmail());
                        accountLogin.setName(currentAccountDB.getUsername());
                        accountLogin.setAvatar(currentAccountDB.getAvatar());
                        accountLogin.setPhone(currentAccountDB.getPhone());
                        accountLogin.setCustomer(currentAccountDB.getCustomer());
                        accountLogin.setRole(currentAccountDB.getRole().getName());
                        userGetAccount.setAccount(accountLogin);
                }

                ApiResponse<ResLoginDTO.UserGetAccount> response = new ApiResponse<ResLoginDTO.UserGetAccount>();
                response.setData(userGetAccount);
                response.setMessage("Lấy thông tin tài khoản thành công");
                response.setStatus(HttpStatus.OK.value());
                return ResponseEntity.ok().body(response);

        }

        @GetMapping("/auth/refresh")
        public ResponseEntity<ApiResponse<ResLoginDTO>> getRefreshToken(
                        @CookieValue(name = "refresh_token", defaultValue = "abc") String refresh_token)
                        throws AppException {
                if (refresh_token.equals("abc")) {
                        throw new AppException("Bạn không có refresh token ở cookie");
                }

                // check valid
                Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refresh_token);
                String email = decodedToken.getSubject();

                // get current account
                Account currentAccountDB = accountService.getUserByEmail(email);
                ResLoginDTO resLoginDTO = new ResLoginDTO();
                ResLoginDTO.AccountLogin accountLogin = new ResLoginDTO.AccountLogin(currentAccountDB.getId(),
                                currentAccountDB.getEmail(),
                                currentAccountDB.getUsername(),
                                currentAccountDB.getAvatar(),
                                currentAccountDB.getPhone(),
                                currentAccountDB.getRole().getName(),
                                currentAccountDB.getCustomer()
                                );
                resLoginDTO.setAccount(accountLogin);
                String access_token = this.securityUtil.createAccessToken(email, resLoginDTO);
                resLoginDTO.setAccessToken(access_token);

                // create new refresh token
                String new_refresh_token = this.securityUtil.createRefreshToken(email, resLoginDTO);

                // set new refresh token to cookie
                ResponseCookie resCookies = ResponseCookie
                                .from("refresh_token", new_refresh_token)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(refreshTokenExpiration)
                                .build();

                ApiResponse<ResLoginDTO> response = new ApiResponse<ResLoginDTO>();
                response.setData(resLoginDTO);
                response.setMessage("Refresh token thành công");
                response.setStatus(HttpStatus.OK.value());
                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                                .body(response);

        }

        @PostMapping("/auth/logout")
        public ResponseEntity<ApiResponse<Void>> logout() throws AppException {
                String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get()
                                : "";
                if (email.equals("")) {
                        throw new AppException("Access token không hợp lệ");
                }
                ResponseCookie deleteSpringCookie = ResponseCookie
                                .from("refresh_token", null)
                                .httpOnly(true)
                                .secure(true)
                                .path("/")
                                .maxAge(0)
                                .build();

                ApiResponse<Void> response = new ApiResponse<>();
                response.setMessage("Đăng xuất thành công");
                response.setStatus(HttpStatus.OK.value());
                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, deleteSpringCookie.toString())
                                .body(response);
        }

        @PostMapping("/auth/register")
        public ResponseEntity<ApiResponse<ResCreateAccountDTO>> register(@Valid @RequestBody ReqRegister rqRegister)
                        throws AppException {
                boolean isEmailExist = this.accountService.isExistUserByEmail(rqRegister.getEmail());
                if (isEmailExist) {
                        throw new AppException(
                                        "Email " + rqRegister.getEmail() + "đã tồn tại, vui lòng sử dụng email khác.");
                }

                String hashPassword = this.passwordEncoder.encode(rqRegister.getPassword());
                Account account = new Account();
                account.setEmail(rqRegister.getEmail());
                account.setUsername(rqRegister.getUsername());
                account.setPhone(rqRegister.getPhone());
                account.setPassword(hashPassword);
                Role role = roleService.getRoleByName("CUSTOMER");
                if(role == null){
                        throw new AppException("Role không tồn tại");
                }
                account.setRole(role);

                Customer customer = new Customer();

                customer.setName(rqRegister.getUsername());
                customer.setPhone(rqRegister.getPhone());
                customer.setEmail(rqRegister.getEmail());
                customer = this.customerService.createCustomer(customer);
                account.setCustomer(customer);
                account = this.accountService.createAccount(account);

                ResCreateAccountDTO resCreateAccountDTO = new ResCreateAccountDTO();
                resCreateAccountDTO.setEmail(account.getEmail());
                resCreateAccountDTO.setUsername(account.getUsername());

                ApiResponse<ResCreateAccountDTO> response = new ApiResponse<>();
                response.setData(resCreateAccountDTO);
                response.setMessage("Tạo tài khoản thành công");
                response.setStatus(HttpStatus.CREATED.value());

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(response);
        }

}
