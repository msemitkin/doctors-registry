package org.geekhub.doctorsregistry.domain.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    //TODO get rid of Spring UserDetailsManager implementation
    public UserService(
        UserDetailsManager userDetailsManager,
        PasswordEncoder passwordEncoder
    ) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(User user) {

        if (userDetailsManager.userExists(user.getEmail())) {
            throw new UserAlreadyExistsException("User with given email already exists");
        }
        if (!passwordsMatch(user.getPassword(), user.getPasswordConfirmation())) {
            throw new PasswordsDoNotMatchException("Passwords do not match");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        String[] roles = new String[]{user.getRole().name()};
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(encodedPassword)
            .roles(roles)
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

    private boolean passwordsMatch(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }
}
