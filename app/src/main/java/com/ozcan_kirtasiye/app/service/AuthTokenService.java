package com.ozcan_kirtasiye.app.service;

import com.ozcan_kirtasiye.app.dto.LoginDTO;
import com.ozcan_kirtasiye.app.dto.Token;
import com.ozcan_kirtasiye.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AuthTokenService implements TokenService {


    @Autowired
    UserService userService;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Override
    public Token createToken(User user , LoginDTO loginDTO) {
        String concatenated = loginDTO.email() + ":" + loginDTO.password();
        String token = Base64.getEncoder().encodeToString(concatenated.getBytes());

        return new Token("Basic", token);
    }

    @Override
    public User verifyToken(String authHeader) {
        if (authHeader==null || authHeader.isEmpty()) {return null;}

        var base64Encoded = authHeader.split("Basic")[1];
        var decoded = new String(Base64.getDecoder().decode(base64Encoded));
        var loginDTO = decoded.split(":");
        var email = loginDTO[0];
        var password = loginDTO[1];
        User inDB = userService.findByEmail(email);
        if(inDB == null) return null;
        if(!passwordEncoder.matches(password, inDB.getPassword())) {return null;}
        return inDB;

    }
}
