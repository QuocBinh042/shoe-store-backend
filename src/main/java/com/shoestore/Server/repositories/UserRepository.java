package com.shoestore.Server.repositories;

import com.shoestore.Server.entities.Role;
import com.shoestore.Server.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
  public User findByEmail(String email);
  boolean existsByEmail(String email);
  boolean existsUserByCIIs(String CI);
  // Tìm kiếm theo tên, không phân biệt hoa thường
  List<User> findByNameContainingIgnoreCase(String search);

  // Tìm kiếm theo trạng thái và tên, không phân biệt hoa thường
  List<User> findByStatusAndNameContainingIgnoreCase(String status, String search);

}
