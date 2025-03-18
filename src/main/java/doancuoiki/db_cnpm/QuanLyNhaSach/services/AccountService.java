package doancuoiki.db_cnpm.QuanLyNhaSach.services;

import java.util.List;
import java.util.stream.Collectors;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Customer;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Account;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Role;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.request.ReqUpdateAccountDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResAccountDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResCreateAccountDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResUpdateAccountDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResultPaginationDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.AccountRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    private final CustomerService customerService;

    private final EmployeeService employeeService;

    private final RoleService roleService;

    public AccountService(AccountRepository accountRepository, RoleService roleService, CustomerService customerService, EmployeeService employeeService) {
        this.accountRepository = accountRepository;
        this.roleService = roleService;
        this.customerService = customerService;
        this.employeeService = employeeService;
    }

    public boolean checkEmailExist(String email) {
        return accountRepository.existsByEmail(email);
    }

    public Account createAccount(Account rqAccount) throws AppException {
        if(rqAccount.getCustomer() != null){
            Customer customer = customerService.getCustomerById(rqAccount.getCustomer().getId());
            rqAccount.setCustomer(customer);
        }

        if (rqAccount.getRole() != null) {
            Role role = roleService.getRoleById(rqAccount.getRole().getId());
            rqAccount.setRole(role);
        }
        if(rqAccount.getRole() != null)
        {
            if(rqAccount.getRole().getName().equals("CUSTOMER"))
            {
                Customer customer = this.customerService.getCustomerByEmail(rqAccount.getEmail());
                if(customer != null)
                {
                    rqAccount.setCustomer(customer);
                }
            }else if(!rqAccount.getRole().getName().equals("ADMIN"))
            {
                Employee employee = this.employeeService.getEmployeeByEmail(rqAccount.getEmail());
                if(employee != null)
                {
                    rqAccount.setEmployee(employee);
                }else
                {
                    throw new AppException("Nhân viên không tồn tại, vui lòng tạo nhân viên trước");
                }
            }
        }
        return accountRepository.save(rqAccount);
    }

    public boolean checkAccountExist(Long id) {
        return accountRepository.existsById(id);
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }

    public ResultPaginationDTO getAllAccountWithPagination(Specification<Account> spec, Pageable pageable) {
        Page<Account> pageAccounts = this.accountRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageAccounts.getTotalPages());
        meta.setTotal(pageAccounts.getTotalElements());
        rs.setMeta(meta);
        // List<ResAccountDTO> listAccountDTO = pageAccounts.map(account ->
        // convertToResAccountDTO(account)).getContent();
        List<ResAccountDTO> listAccountDTO = pageAccounts.getContent().stream()
                .map(item -> this.convertToResAccountDTO(item)).collect(Collectors.toList());
        rs.setResult(listAccountDTO);
        return rs;
    }

    public Account updateAccount(ReqUpdateAccountDTO rqAccount) throws AppException {
        Account accountDB = accountRepository.findById(rqAccount.getId()).orElse(null);
        if (accountDB == null) {
            throw new AppException("Account not found");
        }

        if (rqAccount.getRole() != null) {
            Role role = roleService.getRoleById(rqAccount.getRole().getId());
            rqAccount.setRole(role);
        }

        boolean isExist = accountRepository.existsByEmail(rqAccount.getEmail());
        if (isExist && !accountDB.getEmail().equals(rqAccount.getEmail())) {
            throw new AppException("Email đã tồn tại");
        }
        accountDB.setEmail(rqAccount.getEmail());
        accountDB.setUsername(rqAccount.getUsername());
        accountDB.setPhone(rqAccount.getPhone());
        accountDB.setAvatar(rqAccount.getAvatar());
        accountDB.setRole(rqAccount.getRole());
        return accountRepository.save(accountDB);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    public Account getUserByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public boolean isExistUserByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    // public void updateRefreshToken(String token, String email) {
    // Account accountDB = this.getUserByEmail(email);
    // if (accountDB != null) {
    // accountDB.setRefreshToken(token);
    // accountRepository.save(accountDB);
    // }
    // }

    // public Account getUserByRefreshTokenAndEmail(String token, String email) {
    // return this.accountRepository.findByRefreshTokenAndEmail(token, email);
    // }

    public ResCreateAccountDTO convertToResCreateAccountDTO(Account account) {
        ResCreateAccountDTO res = new ResCreateAccountDTO();
        res.setEmail(account.getEmail());
        res.setUsername(account.getUsername());
        res.setCreatedAt(account.getCreatedAt());
        return res;
    }

    public ResUpdateAccountDTO convertToResUpdateAccountDTO(Account account) {
        ResUpdateAccountDTO res = new ResUpdateAccountDTO();
        res.setId(account.getId());
        res.setUsername(account.getUsername());
        res.setPhone(account.getPhone());
        res.setEmail(account.getEmail());
        res.setUpdatedAt(account.getUpdatedAt());
        return res;
    }

    public ResAccountDTO convertToResAccountDTO(Account account) {
        ResAccountDTO res = new ResAccountDTO();
        res.setId(account.getId());
        res.setUsername(account.getUsername());
        res.setEmail(account.getEmail());
        res.setPhone(account.getPhone());
        res.setCreatedAt(account.getCreatedAt());
        res.setUpdatedAt(account.getUpdatedAt());

        Role role = account.getRole();
        if (role != null) {
            ResAccountDTO.RoleAccount roleAccount = new ResAccountDTO.RoleAccount();
            roleAccount.setId(role.getId());
            roleAccount.setName(role.getName());
            res.setRole(roleAccount);
        }
        Customer customer = account.getCustomer();
        if (customer != null) {
            ResAccountDTO.CustomerAccount customerAccount = new ResAccountDTO.CustomerAccount();
            customerAccount.setId(customer.getId());
            customerAccount.setName(customer.getName());
            res.setCustomer(customerAccount);
        }

        Employee employee = account.getEmployee();
        if (employee != null) {
            ResAccountDTO.EmployeeAccount employeeAccount = new ResAccountDTO.EmployeeAccount();
            employeeAccount.setId(employee.getId());
            employeeAccount.setName(employee.getFullName());
            res.setEmployee(employeeAccount);
        }
        return res;
    }

    public boolean checkAccountExistByGoogleId(String googleId) {
        return accountRepository.existsByGoogleId(googleId);
    }
}
