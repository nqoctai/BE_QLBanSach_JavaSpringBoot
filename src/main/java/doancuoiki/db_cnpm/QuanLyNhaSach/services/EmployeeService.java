package doancuoiki.db_cnpm.QuanLyNhaSach.services;


import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Customer;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Employee;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Role;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResultPaginationDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.EmployeeRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    private final RoleService roleService;

    public EmployeeService(EmployeeRepository employeeRepository, RoleService roleService) {
        this.employeeRepository = employeeRepository;
        this.roleService = roleService;
    }

    public boolean checkEmailExist(String email) {
        return employeeRepository.existsByEmail(email);
    }

    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    public Employee createEmployee(Employee rqEmployee) {

        return employeeRepository.save(rqEmployee);
    }

    public boolean checkEmployeeExist(Long id) {
        return employeeRepository.existsById(id);
    }

    public ResultPaginationDTO getAllEmployeeWithPagination(Specification<Employee> spec, Pageable pageable) {
        Page<Employee> pageEmployees = this.employeeRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageEmployees.getTotalPages());
        meta.setTotal(pageEmployees.getTotalElements());
        rs.setMeta(meta);
        rs.setResult(pageEmployees.getContent());
        return rs;
    }

    public Employee updateEmployee(Employee rqEmployee) throws AppException {
        Employee employeeDB = employeeRepository.findById(rqEmployee.getId()).orElse(null);
        if (employeeDB == null) {
            throw new AppException("Account not found");
        }
        boolean checkEmail = employeeRepository.existsByEmail(rqEmployee.getEmail());
        if (checkEmail && !employeeDB.getEmail().equals(rqEmployee.getEmail())) {
            throw new AppException("Email đã tồn tại");
        }
        employeeDB.setFullName(rqEmployee.getFullName());
        employeeDB.setPhone(rqEmployee.getPhone());
        employeeDB.setAddress(rqEmployee.getAddress());
        employeeDB.setEmail(rqEmployee.getEmail());
        employeeDB.setSalary(rqEmployee.getSalary());
        employeeDB.setHireDate(rqEmployee.getHireDate());



        return employeeRepository.save(employeeDB);
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

}
