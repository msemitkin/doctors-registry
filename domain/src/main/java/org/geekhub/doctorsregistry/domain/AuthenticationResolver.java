package org.geekhub.doctorsregistry.domain;

import org.geekhub.doctorsregistry.domain.role.Role;

//TODO implement AuthenticationResolver
public interface AuthenticationResolver {

    int getUserId();

    Role getRole();

}
