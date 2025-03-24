package com.shoestore.Server.repositories;

import com.shoestore.Server.entities.Role;
import com.shoestore.Server.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByRoleType(RoleType roleType);
}
