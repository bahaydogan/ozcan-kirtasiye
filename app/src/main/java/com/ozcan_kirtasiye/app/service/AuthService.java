package com.ozcan_kirtasiye.app.service;

import com.ozcan_kirtasiye.app.dto.LoginDTO;
import com.ozcan_kirtasiye.app.dto.LoginResponse;
import com.ozcan_kirtasiye.app.dto.Token;
import com.ozcan_kirtasiye.app.dto.UserCreate;
import com.ozcan_kirtasiye.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserService userService;//user repoya doğrudan müdahale yerine bu daha güvenli.

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ITokenService tokenService;


    public LoginResponse auth(LoginDTO loginDTO){

        User userInDB = userService.findByEmail(loginDTO.email());
        if(userInDB == null){
            System.out.println("\n\nbu email ile bir user yok!!!!!");
        }
        if(!passwordEncoder.matches(loginDTO.password(), userInDB.getPassword())){
            System.out.println("\n\nyanlış parola!!!!!!!!!");
            return null;
        }

        Token token = tokenService.createToken(userInDB, loginDTO);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setUserCreate(new UserCreate(userInDB.getName(), userInDB.getEmail(), userInDB.getPassword(), userInDB.getRole(), userInDB.getAddress(), userInDB.getId())); //burası başardan farklı, onda constructor var.
        return loginResponse;

    }

    public void logout(String token) {
        tokenService.invalidateToken(token);
    }


}
