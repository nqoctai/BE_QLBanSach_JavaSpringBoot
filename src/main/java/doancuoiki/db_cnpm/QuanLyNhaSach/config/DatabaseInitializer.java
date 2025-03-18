package doancuoiki.db_cnpm.QuanLyNhaSach.config;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Account;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Permission;
import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Role;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.AccountRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.PermissionRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(
            PermissionRepository permissionRepository,
            RoleRepository roleRepository,
            AccountRepository accountRepository,
            PasswordEncoder passwordEncoder) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> START INIT DATABASE");
        long countPermissions = this.permissionRepository.count();
        long countRoles = this.roleRepository.count();
        long countAccounts = this.accountRepository.count();

        if (countPermissions == 0) {
            ArrayList<Permission> arr = new ArrayList<>();
            arr.add(new Permission("Create a account", "/api/v1/account", "POST", "ACCOUNTS"));
            arr.add(new Permission("Update a account", "/api/v1/account", "PUT", "ACCOUNTS"));
            arr.add(new Permission("Delete a account", "/api/v1/account/{id}", "DELETE", "ACCOUNTS"));
            arr.add(new Permission("Get a account by id", "/api/v1/account/{id}", "GET", "ACCOUNTS"));
            arr.add(new Permission("Get account with pagination", "/api/v1/accounts", "GET", "ACCOUNTS"));
            arr.add(new Permission("Change password", "/api/v1/account/change-password", "POST", "ACCOUNTS"));

            arr.add(new Permission("get book categories", "/api/v1/categories", "GET", "CATEGORIES"));

            arr.add(new Permission("Create a book", "/api/v1/book", "POST", "BOOKS"));
            arr.add(new Permission("Update a book", "/api/v1/book/{id}", "PUT", "BOOKS"));
            arr.add(new Permission("Delete a book", "/api/v1/book/{id}", "DELETE", "BOOKS"));
            arr.add(new Permission("Get a book by id", "/api/v1/books/{id}", "GET", "BOOKS"));
            arr.add(new Permission("Get book with pagination", "/api/v1/books", "GET", "BOOKS"));
            arr.add(new Permission("Get book no pagination", "/api/v1/booksNoPagination", "GET", "BOOKS"));
            arr.add(new Permission("Revuene book are placed", "/api/v1/sold-books", "GET", "BOOKS"));


            arr.add(new Permission("Add item to cart", "/api/v1/cart/add", "POST", "CARTS"));
            arr.add(new Permission("Delete item from cart", "/api/v1/cart/delete/{id}", "DELETE", "CARTS"));
            arr.add(new Permission("Update cart item", "/api/v1/cart/update", "PUT", "CARTS"));

            arr.add(new Permission("Place an order", "/api/v1/order", "POST", "ORDERS"));
            arr.add(new Permission("Fetch list order with pagination", "/api/v1/order", "GET", "ORDERS"));
            arr.add(new Permission("Get order history", "/api/v1/order/history/{id}", "GET", "ORDERS"));
            arr.add(new Permission("Create order", "/api/v1/order/create", "POST", "ORDERS"));
            arr.add(new Permission("Update order", "/api/v1/order", "PUT", "ORDERS"));

            arr.add(new Permission("get dashboard", "/api/v1/database/dashboard", "GET", "DASHBOARD"));
            arr.add(new Permission("Get revenue", "/api/v1/monthly-revenue", "GET", "DASHBOARD"));
            arr.add(new Permission("Get all categores and all book are placed", "/api/v1/dashboard/view1", "GET", "DASHBOARD"));
            arr.add(new Permission("Get top 5 book sold", "/api/v1/dashboard/get-top5-books-sold", "GET", "DASHBOARD"));
            arr.add(new Permission("Get view 6 dashboard", "/api/v1/dashboard/view6", "GET", "DASHBOARD"));
            arr.add(new Permission("Get Gender Ratio", "/api/v1/dashboard/view9", "GET", "DASHBOARD"));



            arr.add(new Permission("Create a customer", "/api/v1/customer", "POST", "CUSTOMERS"));
            arr.add(new Permission("Update a customer", "/api/v1/customer", "PUT", "CUSTOMERS"));
            arr.add(new Permission("Delete a customer", "/api/v1/customer/{id}", "DELETE", "CUSTOMERS"));
            arr.add(new Permission("Get a customer by id", "/api/v1/customer/{id}", "GET", "CUSTOMERS"));
            arr.add(new Permission("Get customer with pagination", "/api/v1/customers", "GET", "CUSTOMERS"));
            arr.add(new Permission("Get the top 5 customers who buy the most money", "/api/v1/top5-customers", "GET", "CUSTOMERS"));
            arr.add(new Permission("Get invoices By Customer Id", "/api/v1/customers/{customerId}/invoices", "GET", "CUSTOMERS"));


            arr.add(new Permission("Create a employee", "/api/v1/employee", "POST", "EMPLOYEES"));
            arr.add(new Permission("Update a employee", "/api/v1/employee", "PUT", "EMPLOYEES"));
            arr.add(new Permission("Delete a employee", "/api/v1/employee/{id}", "DELETE", "EMPLOYEES"));
            arr.add(new Permission("Get employee with pagination", "/api/v1/employees", "GET", "EMPLOYEES"));

            arr.add(new Permission("Get all shipping status", "/api/v1/shippingStatus", "GET", "SHIPPINGSTATUS"));


            arr.add(new Permission("Create a supplier", "/api/v1/supplier", "POST", "SUPPLIERS"));
            arr.add(new Permission("Update a supplier", "/api/v1/supplier", "PUT", "SUPPLIERS"));
            arr.add(new Permission("Delete a supplier", "/api/v1/supplier/{id}", "DELETE", "SUPPLIERS"));
            arr.add(new Permission("Get a supplier by id", "/api/v1/supplier/{id}", "GET", "SUPPLIERS"));
            arr.add(new Permission("Get suppliers no pagination", "/api/v1/suppliers", "GET", "SUPPLIERS"));
            arr.add(new Permission("Get suppliers with pagination", "/api/v1/suppliers-pagination", "GET", "SUPPLIERS"));
            arr.add(new Permission("Get book by supplier id", "/api/v1/supplier/{id}/books", "GET", "SUPPLIERS"));
            arr.add(new Permission("Get supply by supplier id", "/api/v1/supplier/{id}/supplies", "GET", "SUPPLIERS"));

            arr.add(new Permission("Create a supply", "/api/v1/supply", "POST", "SUPPLIES"));
            arr.add(new Permission("Update a supply", "/api/v1/supply", "PUT", "SUPPLIES"));
            arr.add(new Permission("Delete a supply", "/api/v1/supply/{id}", "DELETE", "SUPPLIES"));
            arr.add(new Permission("Get supplies with pagination", "/api/v1/supplies", "GET", "SUPPLIES"));
            arr.add(new Permission("Get supply by bookid and supplierid", "/api/v1/fetch-supply", "POST", "SUPPLIES"));

            arr.add(new Permission("Create a import receipt", "/api/v1/receipt", "POST", "IMPORTRECEIPTS"));
            arr.add(new Permission("Update a import receipt", "/api/v1/receipt", "PUT", "IMPORTRECEIPTS"));
            arr.add(new Permission("Get import receipt with pagination", "/api/v1/receipts", "GET", "IMPORTRECEIPTS"));

            arr.add(new Permission("Upload a file", "/api/v1/files", "GET", "FILES"));


            arr.add(new Permission("Create a permission", "/api/v1/permissions", "POST", "PERMISSIONS"));
            arr.add(new Permission("Update a permission", "/api/v1/permissions", "PUT", "PERMISSIONS"));
            arr.add(new Permission("Delete a permission", "/api/v1/permissions/{id}", "DELETE", "PERMISSIONS"));
            arr.add(new Permission("Get a permission by id", "/api/v1/permissions/{id}", "GET", "PERMISSIONS"));
            arr.add(new Permission("Get permissions with pagination", "/api/v1/permissions", "GET", "PERMISSIONS"));
            arr.add(new Permission("Get permissions no pagination", "/api/v1/permissions-no-pagination", "GET", "PERMISSIONS"));
            arr.add(new Permission("Get a permission by name", "/api/v1/permissions-name", "GET", "PERMISSIONS"));


            arr.add(new Permission("Create a role", "/api/v1/role", "POST", "ROLES"));
            arr.add(new Permission("Update a role", "/api/v1/role", "PUT", "ROLES"));
            arr.add(new Permission("Delete a role", "/api/v1/role/{id}", "DELETE", "ROLES"));
            arr.add(new Permission("Get a role by id", "/api/v1/role/{id}", "GET", "ROLES"));
            arr.add(new Permission("Get roles with pagination", "/api/v1/roles", "GET", "ROLES"));
            arr.add(new Permission("Get roles no pagination", "/api/v1/role", "GET", "ROLES"));


            this.permissionRepository.saveAll(arr);
        }
        if (countRoles == 0) {
            List<Permission> allPermissions = this.permissionRepository.findAll();

            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole.setDescription("Admin thì full permissions");
            adminRole.setPermissions(allPermissions);

            this.roleRepository.save(adminRole);
        }

        if (countAccounts == 0) {
            Account adminUser = new Account();
            adminUser.setEmail("admin@gmail.com");
            adminUser.setPassword(this.passwordEncoder.encode("123456"));
            adminUser.setPhone("0123456789");
            adminUser.setUsername("Admin full quyền");
            Role adminRole = this.roleRepository.findByName("ADMIN");
            if (adminRole != null) {
                adminUser.setRole(adminRole);
            }
            this.accountRepository.save(adminUser);
        }

        if (countPermissions > 0 && countRoles > 0 && countAccounts > 0) {
            System.out.println(">>> SKIP INIT DATABASE ~ ALREADY HAVE DATA...");
        } else
            System.out.println(">>> END INIT DATABASE");
    }
}
