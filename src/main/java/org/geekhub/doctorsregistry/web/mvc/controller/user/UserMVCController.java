package org.geekhub.doctorsregistry.web.mvc.controller.user;

import org.geekhub.doctorsregistry.web.security.role.Role;
import org.geekhub.doctorsregistry.web.security.role.RoleResolver;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserMVCController {

    private final RoleResolver roleResolver;

    public UserMVCController(RoleResolver roleResolver) {
        this.roleResolver = roleResolver;
    }

    @GetMapping("/users/me/cabinet")
    public String cabinet(@AuthenticationPrincipal User user) {
        Role userRole = roleResolver.resolveRole(user);
        return switch (userRole) {
            case ADMIN -> "redirect:/admins/me/cabinet";
            case CLINIC -> "redirect:/clinics/me/cabinet";
            case DOCTOR -> "redirect:/doctors/me/cabinet";
            case PATIENT -> "redirect:/patients/me/cabinet";
        };
    }

}
