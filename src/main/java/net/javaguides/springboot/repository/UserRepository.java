package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.javaguides.springboot.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findByEmail(String email);
	List<User> findAll();
	public List<User> findByRoles(Role role);
	Boolean existsByEmail(String email);
}
