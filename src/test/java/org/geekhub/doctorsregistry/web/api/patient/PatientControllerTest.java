package org.geekhub.doctorsregistry.web.api.patient;

import org.geekhub.doctorsregistry.RolesDataProviders;
import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.domain.mapper.PatientMapper;
import org.geekhub.doctorsregistry.domain.patient.PatientService;
import org.geekhub.doctorsregistry.domain.user.User;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.web.dto.patient.CreatePatientUserDTO;
import org.geekhub.doctorsregistry.web.security.AuthenticationPrincipal;
import org.geekhub.doctorsregistry.web.security.AuthenticationPrincipalExtractor;
import org.geekhub.doctorsregistry.web.security.role.Role;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
public class PatientControllerTest extends AbstractTestNGSpringContextTests {

    private static final int TEST_PATIENT_ID = 333;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private PatientService patientService;
    @Autowired
    @MockBean
    private PatientMapper patientMapper;
    @Autowired
    @MockBean
    private AppointmentMapper appointmentMapper;
    @Autowired
    @MockBean
    private UserService userService;
    @Autowired
    @MockBean
    private AuthenticationPrincipalExtractor authenticationPrincipalExtractor;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void unauthorized_users_do_not_have_access() throws Exception {
        mockMvc.perform(get("/api/patients/{id}", TEST_PATIENT_ID))
            .andExpect(status().isFound());
        verify(patientService, Mockito.never()).findById(anyInt());
    }

    @Test
    public void returns_message_when_there_is_no_patient_with_given_id() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").password("password").roles("ADMIN");
        when(patientService.findById(TEST_PATIENT_ID)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/patients/{id}", TEST_PATIENT_ID).with(user))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$").isMap())
            .andExpect(jsonPath("$.message", is("Requested entity does not exist")));
    }


    @Test
    public void saves_patient_when_given_valid_data() throws Exception {
        User patient = User.newPatient("email@gmail.com", "password");
        when(userService.userExists(patient.getEmail())).thenReturn(false);
        Mockito.doNothing().when(userService).saveUser(patient);

        mockMvc.perform(post("/api/patients")
                .param("firstName", "Firstname")
                .param("lastName", "Lastname")
                .param("email", "email@gmail.com")
                .param("password", "password")
                .param("passwordConfirmation", "password")
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").doesNotExist());
    }

    @Test(dataProvider = "roles", dataProviderClass = RolesDataProviders.class)
    public void only_unauthorized_users_can_register_patients(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("patient@gmail.com").password("password").roles(role.name());

        mockMvc.perform(post("/api/patients").with(user))
            .andExpect(status().isForbidden());
    }

    @Test
    public void returns_bad_request_when_user_with_given_email_already_exists() throws Exception {
        CreatePatientUserDTO patientDTO = new CreatePatientUserDTO("Firstname", "Lastname",
            "email@gmail.com", "password", "password");
        when(userService.userExists(patientDTO.getEmail())).thenReturn(true);

        mockMvc.perform(post("/api/patients")
                .param("firstName", patientDTO.getFirstName())
                .param("lastName", patientDTO.getLastName())
                .param("email", patientDTO.getEmail())
                .param("password", patientDTO.getPassword())
                .param("passwordConfirmation", patientDTO.getPasswordConfirmation())
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$").isMap())
            .andExpect(jsonPath("$.errors").isMap())
            .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    public void response_consists_all_expected_errors() throws Exception {
        CreatePatientUserDTO createPatientUserDTO = new CreatePatientUserDTO();

        mockMvc.perform(post("/api/patients")
                .param("firstName", createPatientUserDTO.getFirstName())
                .param("lastName", createPatientUserDTO.getLastName())
                .param("email", createPatientUserDTO.getEmail())
                .param("password", createPatientUserDTO.getPassword())
                .param("passwordConfirmation", createPatientUserDTO.getPasswordConfirmation())
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$").isMap())
            .andExpect(jsonPath("$.errors").isMap())
            .andExpect(jsonPath("$.errors.firstName").exists())
            .andExpect(jsonPath("$.errors.lastName").exists())
            .andExpect(jsonPath("$.errors.email").exists())
            .andExpect(jsonPath("$.errors.password").exists())
            .andExpect(jsonPath("$.errors.passwordConfirmation").exists());
    }

    @Test
    public void returns_bad_request_when_user_email_format_is_not_valid() throws Exception {
        CreatePatientUserDTO createPatientUserDTO = new CreatePatientUserDTO("Firstname",
            "Lastname", "emailEmail", "password", "password");

        mockMvc.perform(post("/api/patients")
                .param("firstName", createPatientUserDTO.getFirstName())
                .param("lastName", createPatientUserDTO.getLastName())
                .param("email", createPatientUserDTO.getEmail())
                .param("password", createPatientUserDTO.getPassword())
                .param("passwordConfirmation", createPatientUserDTO.getPasswordConfirmation())
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$").isMap())
            .andExpect(jsonPath("$.errors").isMap())
            .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    public void returns_empty_list_when_there_are_no_archived_appointments() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor patient =
            user("patient@gmail.com").password("password").roles("PATIENT");
        when(authenticationPrincipalExtractor.getPrincipal()).thenReturn(new AuthenticationPrincipal(TEST_PATIENT_ID, Role.PATIENT));

        mockMvc.perform(get("/api/patients/me/appointments/archive").with(patient))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test(dataProvider = "roles_except_patient", dataProviderClass = RolesDataProviders.class)
    public void only_patients_have_access_to_their_archived_appointments(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").password("password").roles(role.toString());

        mockMvc.perform(get("/api/patients/me/appointments/archive").with(user))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$").doesNotExist());
    }

    @Test(dataProvider = "roles_except_patient", dataProviderClass = RolesDataProviders.class)
    public void only_patients_have_access_to_their_pending_appointments(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").password("password").roles(role.toString());

        mockMvc.perform(get("/api/patients/me/appointments/pending").with(user))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void returns_empty_list_when_there_are_no_pending_appointments() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor patient =
            user("patient@gmail.com").password("password").roles("PATIENT");
        when(authenticationPrincipalExtractor.getPrincipal()).thenReturn(new AuthenticationPrincipal(TEST_PATIENT_ID, Role.PATIENT));

        mockMvc.perform(get("/api/patients/me/appointments/pending").with(patient))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test(dataProvider = "roles_except_admin", dataProviderClass = RolesDataProviders.class)
    public void only_admin_can_see_patients_data(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor not_admin =
            user("email@gmail.com").password("password").roles(role.name());

        mockMvc.perform(get("/api/patients/{id}", 1).with(not_admin))
            .andExpect(status().isForbidden());
    }

    @Test
    public void admin_can_see_patients_data() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor admin =
            user("email@gmail.com").password("password").roles("ADMIN");

        mockMvc.perform(get("/api/patients/{id}", 1).with(admin))
            .andExpect(status().isOk());
    }
}
