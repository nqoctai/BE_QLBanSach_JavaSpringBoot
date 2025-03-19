package doancuoiki.db_cnpm.QuanLyNhaSach.services;


import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Account;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Customer;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Role;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ExchangeTokenRequest;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResLoginDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.AccountRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.CustomerRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.httpclient.OutboundIdentityClient;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.httpclient.OutboundUserClient;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.SecurityUtil;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final OutboundIdentityClient outboundIdentityClient;
    private final OutboundUserClient outboundUserClient;
    private final RoleService roleService;
    private final AccountService accountService;
    private final CustomerService customerService;
    private final SecurityUtil securityUtil;

    public AuthenticationService(AccountRepository accountRepository, CustomerRepository customerRepository, OutboundIdentityClient outboundIdentityClient, OutboundUserClient outboundUserClient, RoleService roleService, AccountService accountService, CustomerService customerService, SecurityUtil securityUtil) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.outboundIdentityClient = outboundIdentityClient;
        this.outboundUserClient = outboundUserClient;
        this.roleService = roleService;
        this.accountService = accountService;
        this.customerService = customerService;
        this.securityUtil = securityUtil;
    }


    @Value("${outbound.identity.client-id}")
    protected String CLIENT_ID;

    @Value("${outbound.identity.client-secret}")
    protected String CLIENT_SECRET;

    @Value("${outbound.identity.redirect-uri}")
    protected String REDIRECT_URI;

    protected final String GRANT_TYPE = "authorization_code";

    @Value("${nqoctai.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public ResLoginDTO outboundAuthenticate(String code) throws AppException {
        ExchangeTokenRequest request = new ExchangeTokenRequest();
        request.setClientId(CLIENT_ID);
        request.setClientSecret(CLIENT_SECRET);
        request.setCode(code);
        request.setGrantType(GRANT_TYPE);
        request.setRedirectUri(REDIRECT_URI);
        var response = outboundIdentityClient.exchangeToken(request);

        System.out.println(response);

        log.info("response: {}", response);

        var userInfo = outboundUserClient.getUserInfo("json", response.getAccessToken());

        log.info("userInfo: {}", userInfo);

        var account = accountRepository.findByEmail(userInfo.getEmail());
        ResLoginDTO resLogin = new ResLoginDTO();
        if(account == null){
            Account newAccount = new Account();
            newAccount.setEmail(userInfo.getEmail());
            newAccount.setUsername(userInfo.getGivenName()+" "+userInfo.getFamilyName());
            newAccount.setPassword("oauth2loginwithgoogle");
            Role role = roleService.getRoleByName("CUSTOMER");
            if(role == null){
                throw new AppException("Role không tồn tại");
            }

            newAccount.setRole(role);

            Customer customer = customerRepository.findByEmail(userInfo.getEmail());
            if(customer == null){
                customer = new Customer();
                customer.setName(userInfo.getGivenName() + " " + userInfo.getFamilyName());
                customer.setEmail(userInfo.getEmail());
                customer = this.customerRepository.save(customer);
            }
            newAccount.setCustomer(customer);
            newAccount = this.accountRepository.save(newAccount);



            ResLoginDTO.AccountLogin accountLogin = new ResLoginDTO.AccountLogin(newAccount.getId(),
                    newAccount.getEmail(),
                    newAccount.getUsername(),
                    newAccount.getAvatar(),
                    newAccount.getPhone(),
                    newAccount.getRole().getName(),
                    newAccount.getCustomer());

            resLogin.setAccount(accountLogin);


            String accessToken = securityUtil.createAccessToken(userInfo.getEmail(),resLogin);
            String refreshToken = securityUtil.createRefreshToken(userInfo.getEmail(),resLogin);


            return resLogin;

        }else{
            ResLoginDTO.AccountLogin accountLogin = new ResLoginDTO.AccountLogin(account.getId(),
                    account.getEmail(),
                    account.getUsername(),
                    account.getAvatar(),
                    account.getPhone(),
                    account.getRole().getName(),
                    account.getCustomer());

            resLogin.setAccount(accountLogin);
            String accessToken = securityUtil.createAccessToken(userInfo.getEmail(),resLogin);
            resLogin.setAccessToken(accessToken);


            resLogin.setAccessToken(accessToken);
            return resLogin;
        }

    }


}
