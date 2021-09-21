package org.geekhub.doctorsregistry.repository.user;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository {

    void createUser(UserDetails user);


    void updateUser(UserDetails user);


    void deleteUser(String username);


    void changePassword(String email, String newPassword);


    boolean userExists(String email);


    UserDetails loadUserByEmail(String email);
}
