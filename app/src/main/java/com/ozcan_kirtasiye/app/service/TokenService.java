package com.ozcan_kirtasiye.app.service;

import com.ozcan_kirtasiye.app.dto.LoginDTO;
import com.ozcan_kirtasiye.app.dto.Token;
import com.ozcan_kirtasiye.app.model.User;

public interface TokenService {

    public Token createToken(User user, LoginDTO loginDTO);

    public User verifyToken(String authHeader);


}
