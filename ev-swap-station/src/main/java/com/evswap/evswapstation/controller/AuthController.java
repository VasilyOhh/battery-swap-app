package com.evswap.evswapstation.controller;

import com.evswap.evswapstation.entity.User;
import com.evswap.evswapstation.enums.Role;
import com.evswap.evswapstation.repository.UserRepository;
import com.evswap.evswapstation.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // ✅ Đăng ký
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.existsByUserName(user.getUserName())) {
            return ResponseEntity.badRequest().body("❌ Username already exists");
        }

        // Mã hóa mật khẩu
        user.setPassword(encoder.encode(user.getPassword()));

        // Gán role mặc định (driver)
        user.setRole(Role.DRIVER);

        userRepository.save(user);
        return ResponseEntity.ok("✅ Register successfully");
    }

    // ✅ Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        System.out.println("========== 🔍 LOGIN REQUEST ==========");
        System.out.println("📝 Full request body: " + loginRequest);

        // Lấy thông tin từ request (hỗ trợ cả camelCase và snake_case)
        String email = loginRequest.getOrDefault("email", loginRequest.getOrDefault("Email", null));
        String username = loginRequest.getOrDefault("userName", loginRequest.getOrDefault("username",
                loginRequest.getOrDefault("Username", null)));
        String password = loginRequest.get("password");

        System.out.println("📧 Email: " + email);
        System.out.println("👤 Username: " + username);
        System.out.println("🔑 Password exists: " + (password != null));
        System.out.println("=====================================");

        // Kiểm tra password bắt buộc
        if (password == null || password.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ Password is required");
        }

        Optional<User> userOpt;

        // 🔍 Tìm user theo email hoặc username
        if (email != null && !email.isEmpty()) {
            System.out.println("🔎 Searching by email: " + email);
            // Lấy tất cả user với email (để handle trường hợp có nhiều)
            var users = userRepository.findAllByEmail(email);
            if (!users.isEmpty()) {
                // Lấy user đầu tiên
                userOpt = Optional.of(users.get(0));
                System.out.println("✅ Found " + users.size() + " user(s) with email, using first one (ID: " + userOpt.get().getUserID() + ")");
            } else {
                userOpt = Optional.empty();
                System.out.println("❌ No user found with email");
            }
        } else if (username != null && !username.isEmpty()) {
            System.out.println("🔎 Searching by username: " + username);
            userOpt = userRepository.findByUserName(username);
            System.out.println("✅ User found by username: " + userOpt.isPresent());
        } else {
            System.out.println("❌ Neither email nor username provided!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("❌ Email or username is required");
        }

        if (userOpt.isEmpty()) {
            System.out.println("❌ User not found!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("❌ Invalid username/email or password");
        }

        User user = userOpt.get();
        System.out.println("👤 Found user: " + user.getUserName() + " / " + user.getEmail());
        System.out.println("🔑 Input password: " + password);
        System.out.println("💾 Stored password hash: " + user.getPassword());

        // Kiểm tra mật khẩu
        boolean passwordValid = false;

        // Thử so khớp với BCrypt hash
        if (encoder.matches(password, user.getPassword())) {
            passwordValid = true;
            System.out.println("✅ Password matches (BCrypt)");
        }
        // Nếu không match với BCrypt, thử so sánh plain text (cho trường hợp password chưa được hash)
        else if (password.equals(user.getPassword())) {
            passwordValid = true;
            System.out.println("✅ Password matches (plain text) - WARNING: Password stored as plain text!");

            // Tự động encode lại password với giá trị input
            String newHash = encoder.encode(password);
            user.setPassword(newHash);
            userRepository.save(user);
            System.out.println("🔒 Password has been re-encoded and saved");
        }

        if (!passwordValid) {
            System.out.println("❌ Password mismatch!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("❌ Invalid username/email or password");
        }

        System.out.println("✅ Login successful!");

        // Tạo JWT token
        String token = jwtService.generateToken(user);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", user.getRole().name());
        response.put("username", user.getUserName());
        response.put("email", user.getEmail());
        response.put("fullName", user.getFullName());

        return ResponseEntity.ok(response);
    }
}