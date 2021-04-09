package org.geekhub.doctorsregistry.domain.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(CreateUserDTO userDTO) {

        if (userDetailsManager.userExists(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("User with given email already exists");
        }
        if (!passwordsMatch(userDTO.getPassword(), userDTO.getPasswordConfirmation())) {
            throw new PasswordsDoNotMatchException("Passwords do not match");
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        UserDetails userDetails =
            User.builder()
                .username(userDTO.getEmail())
                .password(encodedPassword)
                .roles(userDTO.getRoles())
                .build();
        userDetailsManager.createUser(userDetails);
    }

    private boolean passwordsMatch(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }
}
