package cn.itcast.bos.service.system;

import java.util.List;

import org.apache.shiro.authc.UsernamePasswordToken;

import cn.itcast.bos.domain.system.User;

public interface UserService {

	User findByUsername(String username);

	List<User> findAll();

	void saveUser(User model, String[] roleIds);

	


}
