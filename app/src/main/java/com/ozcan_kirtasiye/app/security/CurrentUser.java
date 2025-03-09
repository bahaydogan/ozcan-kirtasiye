package com.ozcan_kirtasiye.app.security;

import com.ozcan_kirtasiye.app.enums.UserRole;
import com.ozcan_kirtasiye.app.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class CurrentUser implements UserDetails {

    long id;
    String email;
    String password;
    boolean enabled;
    private UserRole role;

    public CurrentUser(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.enabled = user.isActive();
        // Assuming the User entity has a method getRole() that returns a UserRole (enum)
        this.role = user.getRole(); // UserRole enum (USER or ADMIN)
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Dynamically assign authorities based on role
        List<GrantedAuthority> authorities = new ArrayList<>();

        // If the user is an ADMIN, assign ADMIN authority
        if (role == UserRole.ADMIN) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        }

        // All users get the USER authority
        authorities.add(new SimpleGrantedAuthority("USER"));

        return authorities;
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
