package com.shoestore.Server.repositories;

import com.shoestore.Server.entities.Role;
import com.shoestore.Server.entities.User;
import com.shoestore.Server.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
  User findByEmail(String email);
  boolean existsByEmail(String email);
  List<User> findByRoles_RoleType(RoleType roleType);
  @Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleType = :roleType " +
          "AND (LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
          "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
          "OR u.phoneNumber LIKE CONCAT('%', :keyword, '%'))")
  List<User> searchUsersByRole(@Param("keyword") String keyword, @Param("roleType") RoleType roleType);
}
