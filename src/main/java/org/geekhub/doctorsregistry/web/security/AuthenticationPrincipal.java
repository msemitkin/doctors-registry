package org.geekhub.doctorsregistry.web.security;

import org.geekhub.doctorsregistry.web.security.role.Role;

public record AuthenticationPrincipal(int userId, Role role) {
}
