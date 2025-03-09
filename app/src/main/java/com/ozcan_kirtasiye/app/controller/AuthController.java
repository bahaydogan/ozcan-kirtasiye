package com.ozcan_kirtasiye.app.controller;

import com.ozcan_kirtasiye.app.dto.LoginDTO;
import com.ozcan_kirtasiye.app.dto.LoginResponse;
import com.ozcan_kirtasiye.app.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    AuthService authService;


    @PostMapping("/api/auth")
    LoginResponse handleAuth(@Valid @RequestBody LoginDTO loginDTO){
        return authService.auth(loginDTO);

    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<?> handleLogout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
        return ResponseEntity.ok("Successfully logged out");
    }
}
