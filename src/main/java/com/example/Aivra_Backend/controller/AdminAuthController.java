
package com.example.Aivra_Backend.controller;
import com.example.Aivra_Backend.dto.request.LoginRequest;
import com.example.Aivra_Backend.dto.response.AuthResponse;
import com.example.Aivra_Backend.service.AdminAuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    public AdminAuthController(AdminAuthService adminAuthService) {
        this.adminAuthService = adminAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = adminAuthService.login(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
