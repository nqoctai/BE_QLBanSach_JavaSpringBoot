package doancuoiki.db_cnpm.QuanLyNhaSach.services;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Permission;
import doancuoiki.db_cnpm.QuanLyNhaSach.dto.response.ResultPaginationDTO;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.PermissionRepository;
import doancuoiki.db_cnpm.QuanLyNhaSach.util.error.AppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import doancuoiki.db_cnpm.QuanLyNhaSach.domain.Role;
import doancuoiki.db_cnpm.QuanLyNhaSach.repository.RoleRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public boolean isNameRoleExist(String name) {
        return roleRepository.existsByName(name);
    }

    public Role getRoleById(long id) {
        return roleRepository.findById(id).orElse(null);
    }

    public Role createRole(Role role) {
        if (role.getPermissions() != null) {
            List<Long> reqPermissions = role.getPermissions()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            role.setPermissions(dbPermissions);
        }
        return roleRepository.save(role);
    }

    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    public boolean existByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    public Role update(Role r) throws AppException {
        Role roleDB = this.getRoleById(r.getId());

        boolean isNameRoleExist = this.existByName(r.getName());
        if (isNameRoleExist && !roleDB.getName().equals(r.getName())) {
            throw new AppException("Tên role đã tồn tại");
        }
        // check permissions
        List<Permission> permissions = roleDB.getPermissions();
        if (permissions != null) {
            for(Permission p : permissions) {
                p.getRoles().remove(roleDB);
            }
        }
        roleDB.getPermissions().clear();
        if (r.getPermissions() != null) {
            List<Long> reqPermissions = r.getPermissions()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            r.setPermissions(dbPermissions);
        }

        roleDB.setName(r.getName());
        roleDB.setDescription(r.getDescription());
        roleDB.setPermissions(r.getPermissions());
        roleDB = this.roleRepository.save(roleDB);
        return roleDB;
    }

    public void delete(long id) {
        this.roleRepository.deleteById(id);
    }

    public ResultPaginationDTO getRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> pRole = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pRole.getTotalPages());
        mt.setTotal(pRole.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pRole.getContent());
        return rs;
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);
    }
}
