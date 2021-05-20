package org.geekhub.doctorsregistry.web.api.clinic;

import org.geekhub.doctorsregistry.RolesDataProviders;
import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.clinic.ClinicService;
import org.geekhub.doctorsregistry.domain.mapper.ClinicMapper;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.repository.clinic.ClinicEntity;
import org.geekhub.doctorsregistry.web.dto.clinic.ClinicDTO;
import org.geekhub.doctorsregistry.web.dto.clinic.CreateClinicUserDTO;
import org.geekhub.doctorsregistry.web.security.role.Role;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.ResultActions;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ClinicController.class)
public class ClinicControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private ClinicService clinicService;
    @Autowired
    @MockBean
    private ClinicMapper clinicMapper;
    @Autowired
    @MockBean
    private UserService userService;

    @Test
    public void only_authenticated_users_can_see_clinics() throws Exception {
        mockMvc.perform(get("/api/clinics"))
            .andDo(print())
            .andExpect(status().isFound());
        Mockito.verify(clinicService, Mockito.never()).findAll(Mockito.anyInt());
    }

    @Test(dataProvider = "roles", dataProviderClass = RolesDataProviders.class)
    public void all_roles_can_see_clinics(Role role) throws Exception {
        Mockito.when(clinicService.findAll(Mockito.anyInt())).thenReturn(Collections.emptyList());
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor patient =
            user("email@gmail.com").roles(role.toString()).password("password");
        mockMvc.perform(get("/api/clinics/pages/{page}", 0).with(patient))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void returns_empty_list_when_there_are_no_saved_clinics() throws Exception {
        Mockito.when(clinicService.findAll(Mockito.anyInt())).thenReturn(Collections.emptyList());
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor patient = user("email@gmail.com").roles("PATIENT").password("password");
        mockMvc.perform(get("/api/clinics/pages/{page}", 0).with(patient))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void returns_clinics_correct() throws Exception {
        List<ClinicEntity> clinics = List.of(
            new ClinicEntity(1, "name1", "address1", "email1@gmail.com"),
            new ClinicEntity(2, "name2", "address2", "email2@gmail.com"),
            new ClinicEntity(3, "name3", "address3", "email3@gmail.com")
        );
        List<ClinicDTO> clinicDTOs = List.of(
            new ClinicDTO(1, "name1", "address1"),
            new ClinicDTO(2, "name2", "address2"),
            new ClinicDTO(3, "name3", "address3")
        );

        Mockito.when(clinicService.findAll(0)).thenReturn(clinics);
        for (int i = 0; i < clinics.size(); i++) {
            Mockito.when(clinicMapper.toDTO(clinics.get(i))).thenReturn(clinicDTOs.get(i));
        }

        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor patient
            = user("email@gmail.com").roles("PATIENT").password("password");

        ResultActions perform = mockMvc.perform(get("/api/clinics/pages/{page}", 0).with(patient));
        perform
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        for (int i = 0; i < clinics.size(); i++) {
            String path = "$[" + i + "]";
            perform
                .andExpect(jsonPath(path + ".id", is(clinicDTOs.get(i).getId())))
                .andExpect(jsonPath(path + ".name", is(clinicDTOs.get(i).getName())))
                .andExpect(jsonPath(path + ".address", is(clinicDTOs.get(i).getAddress())));
        }
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void only_authenticated_users_can_find_clinic_by_id() throws Exception {
        int clinicId = 15;
        mockMvc.perform(get("/api/clinics/{id}", clinicId))
            .andExpect(status().isFound());

        Mockito.verify(clinicService, Mockito.never()).findById(clinicId);
    }

    @Test(dataProvider = "roles", dataProviderClass = RolesDataProviders.class)
    public void all_roles_can_find_clinic_by_id(Role role) throws Exception {
        int clinicId = 15;
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").roles(role.toString()).password("password");
        ClinicEntity clinicEntity = new ClinicEntity(1, "name", "address", "email@gmail.com");
        ClinicDTO clinicDTO = new ClinicDTO(clinicEntity.getId(), clinicEntity.getName(), clinicEntity.getAddress());

        Mockito.when(clinicService.findById(clinicId)).thenReturn(clinicEntity);
        Mockito.when(clinicMapper.toDTO(clinicEntity)).thenReturn(clinicDTO);

        mockMvc.perform(get("/api/clinics/{id}", clinicId).with(user))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isMap())
            .andExpect(jsonPath("$.*", hasSize(3)))
            .andExpect(jsonPath("$.id", is(clinicDTO.getId())))
            .andExpect(jsonPath("$.name", is(clinicDTO.getName())))
            .andExpect(jsonPath("$.address", is(clinicDTO.getAddress())));
    }

    @Test
    public void returns_error_when_there_is_no_clinic_with_given_id() throws Exception {
        int clinicId = 15;
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").roles("PATIENT").password("password");

        Mockito.when(clinicService.findById(clinicId)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/clinics/{id}", clinicId).with(user))
            .andExpect(status().isNotFound())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isMap())
            .andExpect(jsonPath("$.*", hasSize(1)))
            .andExpect(jsonPath("$.message", is("Requested entity does not exist")));
    }

    @Test(dataProvider = "roles_except_admin", dataProviderClass = RolesDataProviders.class)
    public void only_admin_can_register_clinics(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor notAdmin =
            user("email@gmail.com").roles(role.toString()).password("password");
        mockMvc.perform(post("/api/clinics").with(notAdmin))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void returns_errors_when_register_clinic_without_params() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor admin =
            user("email@gmail.com").roles("ADMIN").password("password");

        mockMvc.perform(post("/api/clinics").with(admin))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.errors").isMap())
            .andExpect(jsonPath("$.errors.name.message").exists())
            .andExpect(jsonPath("$.errors.address.message").exists())
            .andExpect(jsonPath("$.errors.email.message").exists())
            .andExpect(jsonPath("$.errors.password.message").exists())
            .andExpect(jsonPath("$.errors.passwordConfirmation.message").exists());
    }

    @Test
    public void saves_clinic_correct() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor admin =
            user("email@gmail.com").roles("ADMIN").password("password");

        CreateClinicUserDTO clinicDTO = new CreateClinicUserDTO("name", "address",
            "email@gmail.com", "password", "password");

        Mockito.doNothing().when(clinicService).save(clinicDTO);

        mockMvc.perform(post("/api/clinics").with(admin)
            .param("name", clinicDTO.getName())
            .param("address", clinicDTO.getAddress())
            .param("email", clinicDTO.getEmail())
            .param("password", clinicDTO.getPassword())
            .param("passwordConfirmation", clinicDTO.getPasswordConfirmation())
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").doesNotExist());
    }
}