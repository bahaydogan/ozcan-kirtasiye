package com.ozcan_kirtasiye.app.controller;

import com.ozcan_kirtasiye.app.dto.UserCreate;
import com.ozcan_kirtasiye.app.error.Error;
import com.ozcan_kirtasiye.app.model.Product;
import com.ozcan_kirtasiye.app.model.User;
import com.ozcan_kirtasiye.app.repository.IUserRepo;
import com.ozcan_kirtasiye.app.service.IUserService;
import com.ozcan_kirtasiye.app.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    private IUserService userService;


    @PostMapping
    public ResponseEntity<?> saveUser(@Valid @RequestBody UserCreate user) {

        User realUser = user.toUser();
        if (realUser.getName() == null || realUser.getName().isEmpty() || realUser.getPassword() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else{
            userService.createUser(user.toUser());
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/{token}/active")
    public ResponseEntity<?> activateUser(@PathVariable String token) {
        userService.activateUser(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // format su sekilde:  api/user?page=2&size=1    mesela
    @GetMapping
    public ResponseEntity<?> getAllUsers(Pageable pageable) {
        return new ResponseEntity<>(userService.getAllUsers(pageable), HttpStatus.OK);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserById(@PathVariable Long userId, @RequestBody User newUser) {
        return new ResponseEntity<>(userService.updateUserById(userId, newUser), HttpStatus.OK);
    }




    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Error handle(MethodArgumentNotValidException ex) {
            Error apiError = new Error();
            apiError.setPath("/api/user");
            apiError.setMessage("Validation error");
            apiError.setStatus(400);
            return apiError;
    }

}
