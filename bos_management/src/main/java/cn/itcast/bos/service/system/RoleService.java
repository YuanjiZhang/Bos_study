package cn.itcast.bos.service.system;

import java.util.List;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;

public interface RoleService {

	List<Role> findByUser(User user);

	List<Role> findAll(Role model);

	void saveRole(Role model, String [] permissionIds, String menuIds);

}
