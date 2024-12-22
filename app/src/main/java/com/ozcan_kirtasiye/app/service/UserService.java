package com.ozcan_kirtasiye.app.service;

import com.ozcan_kirtasiye.app.model.User;
import com.ozcan_kirtasiye.app.repository.IUserRepo;
import com.ozcan_kirtasiye.app.role.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepo userRepo;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    EmailService emailService;

    @Override
    @Transactional(rollbackOn = MailException.class)
    public void createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivationToken(UUID.randomUUID().toString());
        user.setCreateTime(LocalDateTime.now());
        userRepo.save(user);
        emailService.sendActivationEmail(user.getEmail(), user.getActivationToken()); //bunu göndermesi tamam da mailde gelene tıkladıktan sonrası yapılı mı şu an?
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

//    public User getUserByEmail(String email) {
//
//    }

    @Override
    public User updateUserById(Long userId, User newUser) {
        User user = userRepo.findById(userId).orElse(null);
        if (user != null) {
            User foundUser = userRepo.findById(userId).orElse(null);
            foundUser.setEmail(newUser.getEmail());
            foundUser.setName(newUser.getName());
            //crypto password??
            foundUser.setPassword(newUser.getPassword());
            foundUser.setCreateTime(newUser.getCreateTime());  //???
            userRepo.save(foundUser);
            return foundUser;
        }
        else return null;
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

    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
