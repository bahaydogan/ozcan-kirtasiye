package com.ozcan_kirtasiye.app.service;

import com.ozcan_kirtasiye.app.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    void createUser(User user);

    void deleteUser(Long userId);

    Page<User> getAllUsers(Pageable pageable);

    User getUserById(Long userId);

    User updateUserById(Long userId, User newUser);

    void activateUser(String token);

    User getUserByEmail(String email);
}
