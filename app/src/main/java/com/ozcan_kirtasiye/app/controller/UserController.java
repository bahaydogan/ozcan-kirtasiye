package com.ozcan_kirtasiye.app.controller;

import com.ozcan_kirtasiye.app.dto.UserCreate;
import com.ozcan_kirtasiye.app.error.Error;
import com.ozcan_kirtasiye.app.model.User;
import com.ozcan_kirtasiye.app.security.CurrentUser;
import com.ozcan_kirtasiye.app.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public Page<User> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size )
    {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUsers(pageable);
    }


    @DeleteMapping("/adminDelete/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{userId}") //bak buraya<<<<<<<<<<<<<<<<<<
    @PreAuthorize("principal.id == #userId")
    public ResponseEntity<?> deleteUser2(@PathVariable long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("principal.id == #userId")
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
