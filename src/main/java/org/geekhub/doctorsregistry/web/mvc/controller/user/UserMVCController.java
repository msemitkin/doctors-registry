package org.geekhub.doctorsregistry.web.mvc.controller.user;

import org.geekhub.doctorsregistry.web.mvc.controller.RoleNotSupportedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserMVCController {


    @GetMapping("/users/me/cabinet")
    public String cabinet(@AuthenticationPrincipal User user) {
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CLINIC"))) {
            return "redirect:/clinics/me/cabinet";
        } else if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_DOCTOR"))) {
            return "redirect:/doctors/me/cabinet";
        } else if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PATIENT"))) {
            return "redirect:/patients/me/cabinet";
        } else if (user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admins/me/cabinet";
        } else {
            throw new RoleNotSupportedException();
        }
    }

}
