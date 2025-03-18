package doancuoiki.db_cnpm.QuanLyNhaSach.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testcontroller {

    @GetMapping("/test")
    @CrossOrigin
    public String test() {
        return "Hello";
    }

}
