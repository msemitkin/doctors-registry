package org.geekhub.doctorsregistry.mvc.mvc.controller.user;

import org.geekhub.doctorsregistry.domain.AuthenticationResolver;
import org.geekhub.doctorsregistry.domain.role.Role;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.mvc.dto.user.ChangePasswordDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;

import javax.validation.Valid;

@Controller
public class UserMVCController {

    private final UserService userService;
    private final AuthenticationResolver authenticationResolver;

    public UserMVCController(
        UserService userService,
        AuthenticationResolver authenticationResolver
    ) {
        this.userService = userService;
        this.authenticationResolver = authenticationResolver;
    }

    @GetMapping("/users/me/cabinet")
    public String cabinet(@AuthenticationPrincipal User user) {
        Role userRole = authenticationResolver.getRole();
        return getRedirectUrl(userRole);
    }

    @GetMapping("/users/me/change-password")
    public String changePasswordForm(Model model) {
        model.addAttribute("changePassword", new ChangePasswordDTO());
        return "change-password";
    }

    @PutMapping("/users/me/change-password")
    public String changePassword(
        @ModelAttribute("changePassword") @Valid ChangePasswordDTO changePasswordDTO,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "change-password";
        }
        userService.changePassword(changePasswordDTO.getOldPassword(), changePasswordDTO.getNewPassword());
        return "redirect:/index";
    }

    private String getRedirectUrl(Role userRole) {
        return switch (userRole) {
            case ADMIN -> "redirect:/admins/me/cabinet";
            case CLINIC -> "redirect:/clinics/me/cabinet";
            case DOCTOR -> "redirect:/doctors/me/cabinet";
            case PATIENT -> "redirect:/patients/me/cabinet";
        };
    }

}
