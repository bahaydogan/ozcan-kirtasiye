package com.ozcan_kirtasiye.app.dto;

import com.ozcan_kirtasiye.app.model.User;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record UserCreate(


        @NotBlank String name ,


        @Email @NotBlank String email ,


        @NotBlank String password

) {

        public User toUser(){
                User user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPassword(password); //encode ve veya createtime falan gerekebilir.
                return user;
        }




}
