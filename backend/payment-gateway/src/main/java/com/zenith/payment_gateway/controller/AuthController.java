package com.zenith.payment_gateway.controller;


import com.zenith.payment_gateway.dto.LoginRequest;
import com.zenith.payment_gateway.dto.LoginResponse;
import com.zenith.payment_gateway.dto.SignupRequest;
import com.zenith.payment_gateway.dto.SignupResponse;
import com.zenith.payment_gateway.service.UserManagement;
import com.zenith.payment_gateway.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UserManagement usersManagementService;

    // Register
    @PostMapping("/auth/register")
    public ResponseEntity<SignupResponse> register(@RequestBody SignupRequest reg){
        return ResponseEntity.ok(usersManagementService.register(reg));
    }

    // Login
    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req){
        return ResponseEntity.ok(usersManagementService.login(req));
    }

    @GetMapping("/get-profile")
    public ResponseEntity<LoginResponse> getMyProfile(){

        // Get the Authentication object for the current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // getName() -> principle name(Username) for the current authenticated user
        String email = authentication.getName();
        LoginResponse response = usersManagementService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}