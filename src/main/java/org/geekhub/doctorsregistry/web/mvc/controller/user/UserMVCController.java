package org.geekhub.doctorsregistry.web.mvc.controller.user;

import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.web.dto.user.ChangePasswordDTO;
import org.geekhub.doctorsregistry.web.security.role.Role;
import org.geekhub.doctorsregistry.web.security.role.RoleResolver;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@Controller
@ApiIgnore
public class UserMVCController {

    private final UserService userService;
    private final RoleResolver roleResolver;

    public UserMVCController(UserService userService, RoleResolver roleResolver) {
        this.userService = userService;
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

}
