package doancuoiki.db_cnpm.QuanLyNhaSach.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, Object> getUserDetails(@AuthenticationPrincipal OAuth2User oauth2User) {
        if (oauth2User == null) {
            return null; // Unauthorized nếu chưa đăng nhập
        }

        // Tạo một map để chứa thông tin người dùng
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("id", oauth2User.getAttribute("id"));
        userDetails.put("username", oauth2User.getAttribute("login"));
        userDetails.put("name", oauth2User.getAttribute("name"));
        userDetails.put("email", oauth2User.getAttribute("email"));
        userDetails.put("avatar_url", oauth2User.getAttribute("avatar_url"));

        return userDetails;
    }
}
