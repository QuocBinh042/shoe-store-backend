package com.shoestore.Server.repositories;

import com.shoestore.Server.entities.User;
import com.shoestore.Server.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    boolean existsByEmail(String email);

    Page<User> findByRoles_RoleType(RoleType roleType, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.roleType = :roleType " +
            "AND (LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<User> searchUsersByRole(@Param("keyword") String keyword, @Param("roleType") RoleType roleType, Pageable pageable);

    @Query("SELECT COUNT(u) " +
            "FROM User u JOIN u.roles r " +
            "WHERE r.roleType = :role " +
            "  AND u.createdAt > :fromDateTime")
    int countByRoleTypeAndCreatedAtAfter(
            @Param("role") RoleType role,
            @Param("fromDateTime") LocalDateTime fromDateTime
    );
}
