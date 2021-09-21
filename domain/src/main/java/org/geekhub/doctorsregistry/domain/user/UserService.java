package org.geekhub.doctorsregistry.domain.user;

import org.geekhub.doctorsregistry.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(User user) {

        if (userRepository.userExists(user.getEmail())) {
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
        userRepository.createUser(userDetails);
    }

    public void changePassword(String oldPassword, String newPassword) {
        String newEncodedPassword = passwordEncoder.encode(newPassword);
        userRepository.changePassword(oldPassword, newEncodedPassword);
    }

    public boolean userExists(String email) {
        return userRepository.userExists(email);
    }

    private boolean passwordsMatch(String password, String passwordConfirmation) {
        return password.equals(passwordConfirmation);
    }
}
