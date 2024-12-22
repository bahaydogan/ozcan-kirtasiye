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
    UserService userService;//user repoya doğrudan erişmek yerine bu daha güvenli.


    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    TokenService tokenService;


    public LoginResponse auth(LoginDTO loginDTO){

        User userInDB = userService.findByEmail(loginDTO.email());
        if(userInDB == null){
            System.out.println("bu email ile bir user yok!!!!!");
        }
        if(!passwordEncoder.matches(loginDTO.password(), userInDB.getPassword())){
            System.out.println("yanlış parola!!!!!!!!!");
        }

        Token token = tokenService.createToken(userInDB, loginDTO);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(token);
        loginResponse.setUserCreate(new UserCreate(userInDB.getName(), userInDB.getEmail(), userInDB.getPassword())); //burası başardan farklı, onda constructor var.
        return loginResponse;

    }

}
