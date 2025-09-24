package com.example.security.repository;

import com.example.security.dto.response.RoleResponse;
import com.example.security.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String name);
    @Query(value = """
            select r.id, r.name from roles r where :name = r.name
            """, nativeQuery = true)
    Role findRoleByName(String name);

    @Query(value = """
            select * from roles r
            where (
                :q is null
                or r.name ilike '%' || :q || '%'
            )
            order by r.id desc
            """, nativeQuery = true)
    Page<Role> searchRoles(@Param("q") String q, Pageable pageable);

}
