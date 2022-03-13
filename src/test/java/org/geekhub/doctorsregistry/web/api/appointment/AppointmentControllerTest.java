package org.geekhub.doctorsregistry.web.api.appointment;

import org.geekhub.doctorsregistry.RolesDataProviders;
import org.geekhub.doctorsregistry.domain.appointment.AppointmentService;
import org.geekhub.doctorsregistry.domain.appointment.DoctorNotAvailableException;
import org.geekhub.doctorsregistry.domain.appointment.PatientBusyException;
import org.geekhub.doctorsregistry.domain.appointment.RepeatedDayAppointmentException;
import org.geekhub.doctorsregistry.domain.appointment.TimeNotAllowedException;
import org.geekhub.doctorsregistry.domain.patient.OperationNotAllowedException;
import org.geekhub.doctorsregistry.web.dto.appointment.CreateAppointmentDTO;
import org.geekhub.doctorsregistry.web.security.UsernameExtractor;
import org.geekhub.doctorsregistry.web.security.role.Role;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentController.class)
public class AppointmentControllerTest extends AbstractTestNGSpringContextTests {

    private static final int TEST_PATIENT_ID = 333;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private AppointmentService appointmentService;
    @Autowired
    @MockBean
    private UsernameExtractor usernameExtractor;

    @Test(dataProvider = "roles_except_patient", dataProviderClass = RolesDataProviders.class)
    public void only_patients_can_create_appointments(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor notPatient =
            user("email@gmail.com").roles(role.toString()).password("password");
        mockMvc.perform(post("/api/appointments").with(notPatient))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void returns_errors_if_passed_null_values() throws Exception {
        CreateAppointmentDTO appointmentDTO =
            new CreateAppointmentDTO(null, "");

        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor patient =
            user("email@gmail.com").roles("PATIENT").password("password");

        mockMvc.perform(post("/api/appointments").with(patient)
                .param("doctorId", String.valueOf(appointmentDTO.getDoctorId()))
                .param("inputDateTime", appointmentDTO.getInputDateTime())
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$").isMap())
            .andExpect(jsonPath("$.errors").isMap())
            .andExpect(jsonPath("$.errors.doctorId.message").exists())
            .andExpect(jsonPath("$.errors.inputDateTime.message").exists());
    }

    @DataProvider(name = "returns_error_message_when_exception_happened_parameters")
    private Object[][] returns_error_message_when_exception_happened_parameters() {
        return new Object[][]{
            {TimeNotAllowedException.class, "Sorry, you can only create appointments for the next seven days"},
            {PatientBusyException.class, "Sorry, you already have an appointment at selected time"},
            {RepeatedDayAppointmentException.class, "Sorry, you already have an appointment with this doctor on selected day"},
            {DoctorNotAvailableException.class, "Doctor is not available at this time"}
        };
    }

    @Test(dataProvider = "returns_error_message_when_exception_happened_parameters")
    public void returns_error_message_when_exception_happened(Class<? extends Throwable> exceptionType, String message) throws Exception {
        CreateAppointmentDTO appointmentDTO = new CreateAppointmentDTO(1, "2021-10-10T08:00");
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor patient =
            user("email@gmail.com").roles("PATIENT").password("password");
        when(usernameExtractor.getPatientId()).thenReturn(TEST_PATIENT_ID);
        doThrow(exceptionType).when(appointmentService).create(TEST_PATIENT_ID, appointmentDTO);

        mockMvc.perform(post("/api/appointments").with(patient)
                .param("doctorId", String.valueOf(appointmentDTO.getDoctorId()))
                .param("inputDateTime", appointmentDTO.getInputDateTime())
            )
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$").isMap())
            .andExpect(jsonPath("$.*", hasSize(1)))
            .andExpect(jsonPath("$.message", is(message)));
    }

    @Test
    public void saves_appointment_correct() throws Exception {
        CreateAppointmentDTO appointmentDTO = new CreateAppointmentDTO(1, "2021-10-10T08:00");

        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor patient =
            user("email@gmail.com").roles("PATIENT").password("password");
        when(usernameExtractor.getPatientId()).thenReturn(TEST_PATIENT_ID);
        doNothing().when(appointmentService).create(TEST_PATIENT_ID, appointmentDTO);

        mockMvc.perform(post("/api/appointments").with(patient)
                .param("doctorId", String.valueOf(appointmentDTO.getDoctorId()))
                .param("inputDateTime", appointmentDTO.getInputDateTime())
            )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").doesNotExist());
    }

    @Test(dataProvider = "roles_except_patient", dataProviderClass = RolesDataProviders.class)
    public void only_patients_can_delete_appointments(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor notPatient =
            user("email@gmail.com").roles(role.toString()).password("password");
        int appointmentId = 1234;
        mockMvc.perform(delete("/api/appointments/{appointment-id}", appointmentId)
                .with(notPatient)
            )
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void returns_error_message_when_patient_does_not_have_pending_appointment_with_given_id() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor patient =
            user("email@gmail.com").roles("PATIENT").password("password");
        int appointmentId = 1234;
        when(usernameExtractor.getPatientId()).thenReturn(TEST_PATIENT_ID);
        doThrow(OperationNotAllowedException.class).when(appointmentService)
            .deleteById(TEST_PATIENT_ID, appointmentId);

        mockMvc.perform(delete("/api/appointments/{appointment-id}", appointmentId)
                .with(patient)
            )
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$").isMap())
            .andExpect(jsonPath("$.message", Matchers.is("Forbidden")));
    }

    @Test
    public void deletes_appointment_correct() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor patient =
            user("email@gmail.com").roles("PATIENT").password("password");
        int appointmentId = 1234;
        when(usernameExtractor.getPatientId()).thenReturn(TEST_PATIENT_ID);
        doNothing().when(appointmentService).deleteById(TEST_PATIENT_ID, appointmentId);

        mockMvc.perform(delete("/api/appointments/{appointment-id}", appointmentId)
                .with(patient)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").doesNotExist());
    }
}