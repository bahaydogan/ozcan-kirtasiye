package com.ozcan_kirtasiye.app.security;

import com.ozcan_kirtasiye.app.model.User;
import com.ozcan_kirtasiye.app.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class AppUserDetailsService implements UserDetailsService {
    @Autowired
    IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userInDB = userService.getUserByEmail(email);
        if (userInDB == null) {
            throw new UsernameNotFoundException(email);
        }
        return new CurrentUser(userInDB);
    }
}
