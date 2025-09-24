package com.example.security.repository;

import com.example.security.dto.response.UserResponse;
import com.example.security.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUsername(String username);
    @Query(value = """
    select distinct u.* from users u
    join user_roles ur on u.id = ur.user_id
    join roles r on ur.role_id = r.id
    where (
        :kw is null
        or u.username ilike '%' || :kw || '%'
        or u.full_name ilike '%' || :kw || '%'
    )
    and u.enabled = true
    order by u.created_at desc
    """, countQuery = """
    select count(distinct u.id) from users u
    where (
        :kw is null
        or u.username ilike '%' || :kw || '%'
        or u.full_name ilike '%' || :kw || '%'
    )
    and u.enabled = true
    """, nativeQuery = true)
    Page<User> searchUsers(@Param("kw") String kw, Pageable pageable);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findByUsername(String username);

}
