package doancuoiki.db_cnpm.QuanLyNhaSach.controller;

import java.util.List;

import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqChangePassword;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.AccountRepository;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Account;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Role;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.ApiResponse;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqUpdateAccountDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResAccountDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResultPaginationDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.AccountService;
import doancuoiki.db_cnpm.QuanLyNhaSach.services.RoleService;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AccountController {

    private final AccountService accountService;

    private final PasswordEncoder passwordEncoder;

    private final AccountRepository accountRepository;

    private final RoleService roleService;

    public AccountController(AccountService accountService, PasswordEncoder passwordEncoder, RoleService roleService,AccountRepository accountRepository) {
        this.accountService = accountService;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.accountRepository = accountRepository;
    }

    @PostMapping("/account")
    public ResponseEntity<ApiResponse<ResAccountDTO>> createAccount(@RequestBody @Valid Account rqAccount)
            throws AppException {

        boolean emailExist = accountService.checkEmailExist(rqAccount.getEmail());
        if (emailExist) {
            throw new AppException("Email đã tồn tại");
        }
        String hashPassword = passwordEncoder.encode(rqAccount.getPassword());
        rqAccount.setPassword(hashPassword);
        Account res = accountService.createAccount(rqAccount);
        ResAccountDTO responseDTO = this.accountService.convertToResAccountDTO(res);
        ApiResponse<ResAccountDTO> response = new ApiResponse<>();
        response.setData(responseDTO);
        response.setMessage("Tạo tài khoản thành công");
        response.setStatus(HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<ApiResponse<ResAccountDTO>> getAccountById(@PathVariable("id") long id) throws AppException {
        boolean isExist = accountService.checkAccountExist(id);
        if (!isExist) {
            throw new AppException("Id không tồn tại");
        }
        Account res = accountService.getAccountById(id);
        ResAccountDTO responseDTO = this.accountService.convertToResAccountDTO(res);
        ApiResponse<ResAccountDTO> response = new ApiResponse<>();
        response.setData(responseDTO);
        response.setMessage("Lấy thông tin tài khoản thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    // @GetMapping("/accounts")
    // public ResponseEntity<ApiResponse<List<Account>>> getAllAccount() throws
    // AppException {
    // List<Account> res = accountService.getAllAccount();
    // ApiResponse<List<Account>> response = new ApiResponse<>();
    // response.setData(res);
    // response.setMessage("Lấy danh sách tài khoản thành công");
    // response.setStatus(HttpStatus.OK.value());
    // return ResponseEntity.ok(response);
    // }

    @GetMapping("/accounts")
    public ResponseEntity<ApiResponse<ResultPaginationDTO>> getAllAccountWithPagination(
            @Filter Specification<Account> spec, Pageable pageable) throws AppException {
        ResultPaginationDTO res = accountService.getAllAccountWithPagination(spec, pageable);
        ApiResponse<ResultPaginationDTO> response = new ApiResponse<>();
        response.setData(res);
        response.setMessage("Lấy danh sách tài khoản thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/account")
    public ResponseEntity<ApiResponse<ResAccountDTO>> updateAccount(@Valid @RequestBody ReqUpdateAccountDTO rqAccount)
            throws AppException {
        boolean isExist = accountService.checkAccountExist(rqAccount.getId());
        if (!isExist) {
            throw new AppException("Id không tồn tại");
        }
        Account res = accountService.updateAccount(rqAccount);
        ResAccountDTO responseDTO = this.accountService.convertToResAccountDTO(res);
        ApiResponse<ResAccountDTO> response = new ApiResponse<>();
        response.setData(responseDTO);
        response.setMessage("Cập nhật tài khoản thành công");
        response.setStatus(HttpStatus.OK.value());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/account/{id}")
    public ResponseEntity<ApiResponse<ResAccountDTO>> deleteAccount(@PathVariable("id") Long id) throws AppException {
        Account res = accountService.getAccountById(id);
        if (res == null) {
            throw new AppException("Id không tồn tại");
        }
        accountService.deleteAccount(id);
        ResAccountDTO responseDTO = this.accountService.convertToResAccountDTO(res);
        ApiResponse<ResAccountDTO> response = new ApiResponse<>();
        response.setData(responseDTO);
        response.setMessage("Xóa tài khoản thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/account/change-password")
    public ResponseEntity<ApiResponse<ResAccountDTO>> changePassword(@RequestBody ReqChangePassword req) throws AppException {
        Account account = accountService.getUserByEmail(req.getEmail());
        if (account == null) {
            throw new AppException("Tài khoản không tồn tại");
        }
        if (!passwordEncoder.matches(req.getOldPassword(), account.getPassword())) {
            throw new AppException("Mật khẩu cũ không đúng");
        }
        account.setPassword(passwordEncoder.encode(req.getNewPassword()));
        account = accountRepository.save(account);
        ResAccountDTO responseDTO = this.accountService.convertToResAccountDTO(account);
        ApiResponse<ResAccountDTO> response = new ApiResponse<>();
        response.setData(responseDTO);
        response.setMessage("Đổi mật khẩu thành công");
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

}
