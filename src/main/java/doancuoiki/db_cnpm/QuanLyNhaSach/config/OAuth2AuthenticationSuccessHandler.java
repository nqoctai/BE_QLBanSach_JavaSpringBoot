//package doancuoiki.db_cnpm.QuanLyNhaSach.config;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.http.ResponseCookie;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//import org.springframework.stereotype.Component;
//
//import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Account;
//import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResLoginDTO;
//import doancuoiki.db_cnpm.QuanLyNhaSach.repository.AccountRepository;
//import doancuoiki.db_cnpm.QuanLyNhaSach.util.SecurityUtil;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//@Component
//public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
//
//    private final SecurityUtil securityUtil;
//    private final AccountRepository accountRepository;
//
//    @Value("${nqoctai.jwt.refresh-token-validity-in-seconds}")
//    private long refreshTokenExpiration;
//
//    @Autowired
//    public OAuth2AuthenticationSuccessHandler(@Lazy SecurityUtil securityUtil, AccountRepository accountRepository) {
//        this.securityUtil = securityUtil;
//        this.accountRepository = accountRepository;
//    }
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//            Authentication authentication) throws IOException, ServletException {
//
//        // Lấy thông tin người dùng từ OAuth2
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        String email = oAuth2User.getAttribute("email");
//        String name = oAuth2User.getAttribute("name");
//
//        // Tạo đối tượng ResLoginDTO để chứa thông tin cần thiết cho JWT
//        ResLoginDTO loginDTO = new ResLoginDTO();
//        ResLoginDTO.AccountLogin accountDto = new ResLoginDTO.AccountLogin();
//        accountDto.setId(0L); // Sẽ được thay thế bằng ID thực từ cơ sở dữ liệu
//        accountDto.setEmail(email);
//        accountDto.setName(name);
//        loginDTO.setAccount(accountDto);
//
//        // Tìm account trong cơ sở dữ liệu để lấy ID thực
//        Account account = accountRepository.findByEmail(email);
//        if (account != null) {
//            accountDto.setId(account.getId());
//            // Thêm thông tin khác từ account nếu cần
//        }
//
//        // Tạo JWT tokens
//        String accessToken = securityUtil.createAccessToken(email, loginDTO);
//        String new_refresh_token = this.securityUtil.createRefreshToken(email, loginDTO);
//
////        ResponseCookie refreshCookie = ResponseCookie
////                .from("refresh_token", new_refresh_token)
////                .httpOnly(true)
////                .secure(true)
////                .path("/")
////                .maxAge(refreshTokenExpiration)
////                .build();
////
////        response.addHeader("Set-Cookie", refreshCookie.toString());
//
//        // Chuyển hướng đến frontend kèm theo JWT token trong fragment URL
//        response.sendRedirect("http://localhost:3000/?access_token=" + accessToken);
//    }
//}