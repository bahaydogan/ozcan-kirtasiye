package com.ozcan_kirtasiye.app.service;

import com.ozcan_kirtasiye.app.model.User;
import com.ozcan_kirtasiye.app.repository.IUserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    OrderService orderService;

    @Override
    @Transactional(rollbackOn = MailException.class)
    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivationToken(UUID.randomUUID().toString());
        user.setCreateTime(LocalDateTime.now());
        userRepo.save(user);
        emailService.sendActivationEmail(user.getEmail(), user.getActivationToken());
    }

    @Override
    public User updateUserById(Long userId, User newUser) {
        User foundUser = userRepo.findById(userId).orElse(null);
        if (foundUser != null) {
            if (!newUser.getName().equals(foundUser.getName())) {
                foundUser.setName(newUser.getName());
            }
            if (!newUser.getEmail().equals(foundUser.getEmail())) {
                foundUser.setEmail(newUser.getEmail());
            }
            if (!passwordEncoder.matches(newUser.getPassword(), foundUser.getPassword())) {
                foundUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            }

            userRepo.save(foundUser);
            return foundUser;
        }
        else return null;
    }

    @Override
    public void deleteUser(Long userId) {
        userRepo.deleteById(userId);
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepo.findAll(pageable);
    }


    @Override
    public User getUserById(Long userId) {
        return userRepo.findById(userId).orElse(null);
    }



    public void activateUser(String token){
        User userInDB = userRepo.findByActivationToken(token);
        if(userInDB != null){
            userInDB.setActive(true);
            userInDB.setActivationToken(null);
            userRepo.save(userInDB);
        }
        else System.err.println("User not found");

    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
