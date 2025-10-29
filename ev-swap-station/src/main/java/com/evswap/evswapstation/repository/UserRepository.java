package com.evswap.evswapstation.repository;

import com.evswap.evswapstation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);
    
    // Lấy user đầu tiên nếu có nhiều user với cùng email
    @Query("SELECT u FROM User u WHERE u.email = :email ORDER BY u.userID ASC")
    Optional<User> findByEmail(@Param("email") String email);
    
    // Lấy tất cả user với email để debug
    List<User> findAllByEmail(String email);
    
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    Optional<User> findByUserNameOrEmail(String userName, String email);

}