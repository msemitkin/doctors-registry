package org.geekhub.doctorsregistry.web.mvc.controller.user;

import org.geekhub.doctorsregistry.RolesDataProviders;
import org.geekhub.doctorsregistry.web.dto.clinic.CreateClinicUserDTO;
import org.geekhub.doctorsregistry.web.security.role.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AdminUserMVCController.class)
public class AdminUserMVCControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void unauthorized_users_do_not_have_access_to_admin_cabinet() throws Exception {
        mockMvc.perform(get("/admins/me/cabinet"))
            .andExpect(status().isFound());
    }

    @Test(dataProvider = "roles_except_admin", dataProviderClass = RolesDataProviders.class)
    public void only_admin_has_access_to_admin_cabinet(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor notAdmin =
            user("email@gmail.com").roles(role.toString()).password("password");
        mockMvc.perform(get("/admins/me/cabinet").with(notAdmin))
            .andExpect(status().isForbidden());
    }

    @Test
    public void returns_admin_cabinet_page_correct() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor admin =
            user("email@gmail.com").roles("ADMIN").password("password");
        mockMvc.perform(get("/admins/me/cabinet").with(admin))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(view().name("admin-cabinet"))
            .andExpect(model().attribute("clinic", new CreateClinicUserDTO()));
    }
}