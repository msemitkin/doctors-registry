package org.geekhub.doctorsregistry.domain.user;

import org.geekhub.doctorsregistry.web.dto.user.CreateUserDTO;
import org.geekhub.doctorsregistry.web.security.role.RoleResolver;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleResolver roleResolver;

    public UserService(
        UserDetailsManager userDetailsManager,
        PasswordEncoder passwordEncoder,
        RoleResolver roleResolver
    ) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.roleResolver = roleResolver;
    }

    public void saveUser(CreateUserDTO userDTO) {

        if (userDetailsManager.userExists(userDTO.getEmail())) {
            throw new UserAlreadyExistsException("User with given email already exists");
        }
        if (!passwordsMatch(userDTO.getPassword(), userDTO.getPasswordConfirmation())) {
            throw new PasswordsDoNotMatchException("Passwords do not match");
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        String[] roles = new String[]{roleResolver.resolveRole(userDTO).name()};
        UserDetails userDetails =
            User.builder()
                .username(userDTO.getEmail())
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
