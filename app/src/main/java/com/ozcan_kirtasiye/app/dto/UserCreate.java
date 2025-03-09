package com.ozcan_kirtasiye.app.dto;

import com.ozcan_kirtasiye.app.enums.UserRole;
import com.ozcan_kirtasiye.app.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserCreate(


        @NotBlank String name ,

        @Email @NotBlank String email ,

        @NotBlank String password,

        UserRole role,

        @NotBlank String address,

        long id

) {

        public User toUser(){
                User user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password);//encode ve veya createtime falan gerekebilir.
                if (role == null) user.setRole(UserRole.USER);
                else user.setRole(role);
                user.setAddress(address);
                user.setId(id);
                return user;
        }

}
