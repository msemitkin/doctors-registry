package org.geekhub.doctorsregistry.web.mvc.controller.user;

import org.geekhub.doctorsregistry.web.dto.clinic.RegisterClinicDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminUserMVCController {

    @GetMapping("/admins/me/cabinet")
    public String cabinet(Model model) {
        model.addAttribute("clinic", new RegisterClinicDTO());
        return "admin-cabinet";
    }

}
