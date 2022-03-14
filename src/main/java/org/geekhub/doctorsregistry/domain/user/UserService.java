package org.geekhub.doctorsregistry.domain.user;

import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        UserDetailsManager userDetailsManager,
        PasswordEncoder passwordEncoder
    ) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(@NonNull User user) {
        if (userDetailsManager.userExists(user.getEmail())) {
            throw new UserAlreadyExistsException("User with given email already exists");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(encodedPassword)
            .roles(user.getRole().name())
            .build();
        userDetailsManager.createUser(userDetails);
    }

    public void changePassword(String oldPassword, String newPassword) {
        String newEncodedPassword = passwordEncoder.encode(newPassword);
        userDetailsManager.changePassword(oldPassword, newEncodedPassword);
    }

    public boolean userExists(String email) {
        return userDetailsManager.userExists(email);
    }
}
