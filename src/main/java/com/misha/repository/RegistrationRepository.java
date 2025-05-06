package com.misha.repository;

import com.misha.model.Registration;
import com.misha.model.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
@Repository
public interface RegistrationRepository extends JpaRepository<Registration,Integer> {
public Registration findByEmailAndPassword(String email,String password);
    @Query("SELECT r FROM Registration r WHERE r.uuid = :uuid")
    Registration findByUuid(@Param("uuid") String uuid);
    @Transactional
    @Modifying
    @Query("DELETE FROM Registration r WHERE r.uuid = :uuid")
    int deleteByUuid(@Param("uuid") String uuid);
    @Transactional
    @Modifying
    @Query("UPDATE Registration r SET r.roles = :roles, r.updatedAt = :updatedAt WHERE r.email = :email")
    int updateRolesByEmail(@Param("roles") Set<Role> roles, @Param("updatedAt") LocalDateTime updatedAt, @Param("email") String email);
    @Transactional
    @Modifying
    @Query("UPDATE Registration r SET r.email = :email, r.roles = :roles, r.updatedAt = :updatedAt WHERE r.uuid = :uuid")
    int updateEmailAndRolesById(
            @Param("email") String email,
            @Param("roles") Set<Role> roles,
            @Param("updatedAt") LocalDateTime updatedAt,
            @Param("uuid") String uuid
    );
    Page<Registration> findAll(Pageable pageable);



}
