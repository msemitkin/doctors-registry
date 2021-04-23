package org.geekhub.doctorsregistry.web.mvc.controller.user;

import org.geekhub.doctorsregistry.domain.appointment.AppointmentService;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.domain.patient.PatientService;
import org.geekhub.doctorsregistry.web.dto.patient.CreatePatientUserDTO;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
    PatientService patientService;
    @Autowired
    @MockBean
    AppointmentMapper appointmentMapper;
    @Autowired
    @MockBean
    AppointmentService appointmentService;

    @Test
    public void returns_errors_when_submit_empty_form() throws Exception {
        Mockito.doNothing().when(patientService).save(new CreatePatientUserDTO());
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
}