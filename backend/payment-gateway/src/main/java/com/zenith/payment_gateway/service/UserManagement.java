package com.zenith.payment_gateway.service;

import com.zenith.payment_gateway.dto.LoginRequest;
import com.zenith.payment_gateway.dto.LoginResponse;
import com.zenith.payment_gateway.dto.SignupRequest;
import com.zenith.payment_gateway.dto.SignupResponse;
import com.zenith.payment_gateway.entity.UserEntity;
import com.zenith.payment_gateway.repository.UserRepository;
import org.apache.kafka.common.config.types.Password;
import org.apache.kafka.common.security.auth.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class UserManagement {

    @Autowired
    private UserRepository usersRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;







    public SignupResponse register(SignupRequest request){
        SignupResponse response = new SignupResponse();

        try{
            if(usersRepository.existsByEmail(request.getEmail())){
                response.setStatusCode(409);
                response.setMessage("user exists");


            }
            else{
                UserEntity user = new UserEntity();
                user.setEmail(request.getEmail());
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                UserEntity userResult = usersRepository.save(user);

                if(userResult.getId() > 0 ) {
                    response.setStatusCode(200);
                    response.setMessage("Users Saves Sucessfullt");
                    response.setUsers(userResult);
                }

            }
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }

        return  response;

    }

    // Login implementation
    public LoginResponse login(LoginRequest loginRequest){
        LoginResponse response = new LoginResponse();

        try {
            // This line authenticates the user’s email and password
            authenticationManager.
                    authenticate
                            (new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            var user = usersRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
            var jwt = jwtService.generateToken((UserDetails) user);
            var refreshToken = jwtService.generateRefreshTokens(new HashMap<>(), (UserDetails) user);

            response.setStatusCode(200);
            response.setToken(jwt);

            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully logged in");

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    // Get my info
    public LoginResponse getMyInfo(String email){
        LoginResponse reqRes = new LoginResponse();
        try{
            Optional<UserEntity> userOptional = usersRepository.findByEmail(email);
            if(userOptional.isPresent()){
                reqRes.setUsers(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            }else{
                reqRes.setStatusCode(404);
                reqRes.setMessage("User is not found");
            }

        }catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting your info : " + e.getMessage());
        }
        return reqRes;
    }
}
