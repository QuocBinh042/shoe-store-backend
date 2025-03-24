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
  User findByEmail(String email);
  boolean existsByEmail(String email);

}
