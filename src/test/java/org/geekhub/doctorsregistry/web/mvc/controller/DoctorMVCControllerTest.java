package org.geekhub.doctorsregistry.web.mvc.controller;

import org.geekhub.doctorsregistry.RolesDataProviders;
import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.appointment.AppointmentService;
import org.geekhub.doctorsregistry.domain.appointment.DoctorNotAvailableException;
import org.geekhub.doctorsregistry.domain.appointment.PatientBusyException;
import org.geekhub.doctorsregistry.domain.appointment.RepeatedDayAppointmentException;
import org.geekhub.doctorsregistry.domain.appointment.TimeNotAllowedException;
import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.domain.mapper.DoctorMapper;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.repository.specialization.SpecializationEntity;
import org.geekhub.doctorsregistry.web.dto.appointment.CreateAppointmentDTO;
import org.geekhub.doctorsregistry.web.dto.doctor.DoctorDTO;
import org.geekhub.doctorsregistry.web.dto.specialization.SpecializationDTO;
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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(DoctorMVCController.class)
public class DoctorMVCControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private DoctorService doctorService;
    @Autowired
    @MockBean
    private DoctorMapper doctorMapper;
    @Autowired
    @MockBean
    private AppointmentService appointmentService;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void only_authenticated_users_can_see_doctors() throws Exception {
        mockMvc.perform(get("/doctors"))
            .andExpect(status().isFound());
        Mockito.verify(doctorService, Mockito.never()).findAll();
    }

    @Test(dataProvider = "roles", dataProviderClass = RolesDataProviders.class)
    public void all_roles_can_see_doctors_list(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").roles(role.toString()).password("password");

        mockMvc.perform(get("/doctors").with(user))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(view().name("doctors"))
            .andExpect(model().size(0));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void only_authenticated_users_can_see_doctor_page() throws Exception {
        int doctorId = 5;
        mockMvc.perform(get("/doctor").param("id", String.valueOf(doctorId)))
            .andExpect(status().isFound());
        Mockito.verify(doctorService, Mockito.never()).findById(doctorId);
    }

    @Test(dataProvider = "roles", dataProviderClass = RolesDataProviders.class)
    public void all_roles_can_see_doctor_page(Role role) throws Exception {
        int doctorId = 5;
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").roles(role.toString()).password("password");

        DoctorEntity doctorEntity = new DoctorEntity(1, "name", "surname", "email@gmail.com",
            new SpecializationEntity(10, "specialization"), 10, 250);
        DoctorDTO doctorDTO = new DoctorDTO(1, "name", "surname",
            new SpecializationDTO(1, "specialization"), 10, 100);

        List<LocalTime> times = List.of("08:00", "08:20", "08:40").stream()
            .map(LocalTime::parse)
            .collect(Collectors.toList());
        Map<LocalDate, List<LocalTime>> schedule = LocalDate.parse("2021-10-10").datesUntil(LocalDate.parse("2021-10-18"))
            .collect(Collectors.toMap(date -> date, date -> times));

        Mockito.when(doctorService.findById(doctorId)).thenReturn(doctorEntity);
        Mockito.when(doctorMapper.toDTO(doctorEntity)).thenReturn(doctorDTO);
        Mockito.when(doctorService.getSchedule(doctorId)).thenReturn(schedule);

        mockMvc.perform(get("/doctor").param("id", String.valueOf(doctorId)).with(user))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(view().name("doctor"))
            .andExpect(model().size(3))
            .andExpect(model().attribute("doctor", doctorDTO))
            .andExpect(model().attribute("schedule", schedule))
            .andExpect(model().attribute("appointment", new CreateAppointmentDTO()));
    }

    @Test
    public void returns_error_message_when_doctor_with_given_id_does_not_exist() throws Exception {
        int doctorId = 5;
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").roles("PATIENT").password("password");
        Mockito.when(doctorService.findById(doctorId)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/doctor").param("id", String.valueOf(doctorId)).with(user))
            .andExpect(status().isNotFound())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(view().name("error"))
            .andExpect(model().size(1))
            .andExpect(model().attributeExists("error"));
    }

    @Test
    public void unauthenticated_users_can_not_make_appointments() throws Exception {
        int doctorId = 5;
        mockMvc.perform(post("/doctor/appointments").with(csrf())
            .param("doctorId", String.valueOf(doctorId))
            .param("inputDateTime", "2021-10-10T10:10")
        )
            .andExpect(status().isFound());
    }

    @Test(dataProvider = "roles_except_patient", dataProviderClass = RolesDataProviders.class)
    public void only_patients_can_make_appointments(Role role) throws Exception {
        CreateAppointmentDTO appointmentDTO = new CreateAppointmentDTO(1, "2021-10-10");
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor notPatient =
            user("email@gmail.com").roles(role.toString()).password("password");

        mockMvc.perform(post("/doctor/appointments").with(notPatient)
            .param("doctorId", String.valueOf(appointmentDTO.getDoctorId()))
            .param("inputDateTime", appointmentDTO.getInputDateTime())
        )
            .andExpect(status().isForbidden());
        Mockito.verify(appointmentService, Mockito.never()).create(appointmentDTO);
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
        CreateAppointmentDTO appointmentDTO = new CreateAppointmentDTO(1, "2021-10-10");
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor patient =
            user("email@gmail.com").roles("PATIENT").password("password");

        Mockito.doThrow(exceptionType).when(appointmentService).create(appointmentDTO);

        mockMvc.perform(post("/doctor/appointments").with(patient).with(csrf())
            .param("doctorId", String.valueOf(appointmentDTO.getDoctorId()))
            .param("inputDateTime", appointmentDTO.getInputDateTime())
        )
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(model().size(1))
            .andExpect(model().attributeExists("error"));
    }

    @Test
    public void saves_appointment_correct() throws Exception {
        CreateAppointmentDTO appointmentDTO = new CreateAppointmentDTO(1, "2021-10-10");
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").roles("PATIENT").password("password");

        Mockito.doNothing().when(appointmentService).create(appointmentDTO);

        mockMvc.perform(post("/doctor/appointments").with(user).with(csrf())
            .param("doctorId", String.valueOf(appointmentDTO.getDoctorId()))
            .param("inputDateTime", appointmentDTO.getInputDateTime())
        )
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/doctor?id=" + appointmentDTO.getDoctorId()));
    }

}