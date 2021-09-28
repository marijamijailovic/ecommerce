package com.demo.ecommerce.repository;

import com.demo.ecommerce.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
  @Query(value="SELECT * FROM users u WHERE u.id=:userId", nativeQuery = true)
  Optional<Users> findByUuid(@Param("userId") UUID userId);
}
