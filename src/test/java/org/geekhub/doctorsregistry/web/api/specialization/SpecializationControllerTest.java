package org.geekhub.doctorsregistry.web.api.specialization;

import org.geekhub.doctorsregistry.RolesDataProviders;
import org.geekhub.doctorsregistry.domain.mapper.SpecializationMapper;
import org.geekhub.doctorsregistry.domain.specialization.SpecializationService;
import org.geekhub.doctorsregistry.web.security.role.Role;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SpecializationController.class)
public class SpecializationControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private SpecializationService specializationService;
    @Autowired
    @MockBean
    private SpecializationMapper specializationMapper;

    @Test(dataProvider = "roles", dataProviderClass = RolesDataProviders.class)
    public void all_roles_can_see_specializations(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").roles(role.toString()).password("password");

        Mockito.when(specializationService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/specializations").with(user))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }

}