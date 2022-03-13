package org.geekhub.doctorsregistry.web.mvc.controller.user;

import org.geekhub.doctorsregistry.RolesDataProviders;
import org.geekhub.doctorsregistry.domain.appointment.AppointmentService;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.domain.patient.PatientService;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.web.dto.patient.CreatePatientUserDTO;
import org.geekhub.doctorsregistry.web.security.AuthenticationPrincipal;
import org.geekhub.doctorsregistry.web.security.AuthenticationPrincipalExtractor;
import org.geekhub.doctorsregistry.web.security.role.Role;
import org.hamcrest.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PatientUserMVCController.class)
public class PatientUserMVCControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private PatientService patientService;
    @Autowired
    @MockBean
    private AppointmentMapper appointmentMapper;
    @Autowired
    @MockBean
    private AppointmentService appointmentService;
    @Autowired
    @MockBean
    private UserService userService;
    @Autowired
    @MockBean
    private AuthenticationPrincipalExtractor authenticationPrincipalExtractor;


    @Test(dataProvider = "roles", dataProviderClass = RolesDataProviders.class)
    public void only_anonymous_user_can_register_patients(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            SecurityMockMvcRequestPostProcessors.user("email@gmail.com").password("password").roles(role.name());
        mockMvc.perform(post("/patients/registration").with(user))
            .andExpect(status().isForbidden());
    }

    @Test
    public void returns_errors_when_submit_empty_form() throws Exception {
        Mockito.doNothing().when(patientService).save(new CreatePatientUserDTO());
        when(userService.userExists(null)).thenReturn(false);
        mockMvc.perform(post("/patients/registration").with(csrf()))
            .andExpect(status().isOk())
            .andExpect(model().hasErrors())
            .andExpect(model().attributeHasFieldErrors("patient",
                "firstName", "lastName", "email", "password", "passwordConfirmation"
            ))
            .andExpect(view().name("patient-registration"));
    }

    @Test
    public void saves_patient_when_given_correct_data() throws Exception {
        CreatePatientUserDTO patient = new CreatePatientUserDTO(
            "Firstname", "Lastname", "patient-email@gmail.com",
            "password", "password");
        Mockito.doNothing().when(patientService).save(patient);
        when(userService.userExists(patient.getEmail())).thenReturn(false);
        mockMvc.perform(post("/patients/registration").with(csrf())
            .param("firstName", patient.getFirstName())
            .param("lastName", patient.getLastName())
            .param("email", patient.getEmail())
            .param("password", patient.getPassword())
            .param("passwordConfirmation", patient.getPasswordConfirmation())
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/index"))
            .andExpect(model().hasNoErrors());
    }

    @Test
    public void returns_error_when_user_with_given_email_already_exists() throws Exception {
        CreatePatientUserDTO patient = new CreatePatientUserDTO(
            "Firstname", "Lastname", "patient-email@gmail.com",
            "password", "password");
        Mockito.doNothing().when(patientService).save(patient);
        when(userService.userExists(patient.getEmail())).thenReturn(true);
        mockMvc.perform(post("/patients/registration").with(csrf())
            .param("firstName", patient.getFirstName())
            .param("lastName", patient.getLastName())
            .param("email", patient.getEmail())
            .param("password", patient.getPassword())
            .param("passwordConfirmation", patient.getPasswordConfirmation())
        )
            .andExpect(status().isOk())
            .andExpect(model().hasErrors())
            .andExpect(model().attributeHasFieldErrors("patient", "email"))
            .andExpect(view().name("patient-registration"));
    }

    @Test
    public void returns_registration_form_correct() throws Exception {
        mockMvc.perform(get("/patients/registration"))
            .andExpect(status().isOk())
            .andExpect(view().name("patient-registration"))
            .andExpect(model().size(1))
            .andExpect(model().attribute("patient", new CreatePatientUserDTO()));
    }

    @Test
    public void returns_patient_cabinet_correct() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor patient =
            SecurityMockMvcRequestPostProcessors.user("patient@gmail.com").password("password").roles("PATIENT");
        when(authenticationPrincipalExtractor.getPrincipal()).thenReturn(new AuthenticationPrincipal(100, Role.PATIENT));
        mockMvc.perform(get("/patients/me/cabinet").with(patient))
            .andExpect(status().isOk())
            .andExpect(view().name("patient-cabinet"))
            .andExpect(model().size(2))
            .andExpect(model().attribute("pending", Matchers.any(List.class)))
            .andExpect(model().attribute("archive", Matchers.any(List.class)));
    }

    @Test
    public void only_authenticated_users_have_access_to_their_personal_cabinets() throws Exception {
        mockMvc.perform(get("/patients/me/cabinet"))
            .andExpect(status().isFound());
    }
}