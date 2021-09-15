package org.geekhub.doctorsregistry.mvc.mvc.controller.user;

import org.geekhub.doctorsregistry.web.dto.clinic.CreateClinicUserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import springfox.documentation.annotations.ApiIgnore;

@Controller
public class AdminUserMVCController {

    @GetMapping("/admins/me/cabinet")
    public String cabinet(Model model) {
        model.addAttribute("clinic", new CreateClinicUserDTO());
        return "admin-cabinet";
    }

    @GetMapping("/analytics")
    public String getAnalytics() {
        return "analytics";
    }

}
