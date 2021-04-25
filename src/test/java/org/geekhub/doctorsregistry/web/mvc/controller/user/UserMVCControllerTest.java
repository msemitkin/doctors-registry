package org.geekhub.doctorsregistry.web.mvc.controller.user;

import org.geekhub.doctorsregistry.web.security.role.Role;
import org.geekhub.doctorsregistry.web.security.role.RoleResolver;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserMVCController.class)
public class UserMVCControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private RoleResolver roleResolver;

    @Test
    public void unauthorized_users_do_not_have_access_to_cabinets() throws Exception {
        mockMvc.perform(get("/users/me/cabinet"))
            .andExpect(status().isFound());
    }


    @DataProvider(name = "returns_users_cabinets_correct_parameters")
    private Object[][] returns_users_cabinets_correct_parameters() {
        return new Object[][]{
            {Role.ADMIN, "/admins/me/cabinet"},
            {Role.CLINIC, "/clinics/me/cabinet"},
            {Role.DOCTOR, "/doctors/me/cabinet"},
            {Role.PATIENT, "/patients/me/cabinet"}
        };
    }

    @Test(dataProvider = "returns_users_cabinets_correct_parameters")
    public void returns_users_cabinets_correct(Role userRole, String redirectUrl) throws Exception {
        String email = "email@gmail.com";
        String role = userRole.toString();
        String password = "password";

        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user(email).roles(role).password(password);
        UserDetails userDetails = User.builder().username(email).roles(role).password(password).build();

        Mockito.when(roleResolver.resolveRole(userDetails)).thenReturn(userRole);

        mockMvc.perform(get("/users/me/cabinet").with(user))
            .andExpect(status().isFound())
            .andExpect(redirectedUrl(redirectUrl));
    }
}