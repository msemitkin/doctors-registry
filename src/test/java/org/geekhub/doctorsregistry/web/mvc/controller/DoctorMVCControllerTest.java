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
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.repository.specialization.SpecializationEntity;
import org.geekhub.doctorsregistry.web.dto.appointment.CreateAppointmentDTO;
import org.geekhub.doctorsregistry.web.dto.doctor.DoctorDTO;
import org.geekhub.doctorsregistry.web.dto.specialization.SpecializationDTO;
import org.geekhub.doctorsregistry.web.security.AuthenticationPrincipal;
import org.geekhub.doctorsregistry.web.security.AuthenticationPrincipalExtractor;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(DoctorMVCController.class)
public class DoctorMVCControllerTest extends AbstractTestNGSpringContextTests {
    private static final int TEST_PATIENT_ID = 333;

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
    @Autowired
    @MockBean
    private AuthenticationPrincipalExtractor authenticationPrincipalExtractor;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void only_authenticated_users_can_see_doctors() throws Exception {
        mockMvc.perform(get("/doctors"))
            .andExpect(status().isFound());
        verify(doctorService, Mockito.never()).findAll(Mockito.anyInt());
    }

    @Test(dataProvider = "roles", dataProviderClass = RolesDataProviders.class)
    public void all_roles_can_see_doctors_list(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").roles(role.toString()).password("password");

        List<DoctorEntity> doctors = List.of(
            new DoctorEntity(1, "name1", "surname1", "email1@gmail.com",
                new SpecializationEntity(1, "specialization1"), 100, 100),
            new DoctorEntity(2, "name2", "surname2", "email2@gmail.com",
                new SpecializationEntity(2, "specialization2"), 200, 100),
            new DoctorEntity(2, "name3", "surname3", "email3@gmail.com",
                new SpecializationEntity(3, "specialization3"), 300, 100)
        );

        List<DoctorDTO> doctorDTOs = List.of(
            new DoctorDTO(1, "name1", "surname1",
                new SpecializationDTO(1, "specialization1"), 100, 100),
            new DoctorDTO(2, "name2", "surname2",
                new SpecializationDTO(2, "specialization2"), 200, 100),
            new DoctorDTO(2, "name3", "surname3",
                new SpecializationDTO(3, "specialization3"), 300, 100)
        );
        Mockito.when(doctorService.findAll(0)).thenReturn(doctors);
        for (int i = 0; i < doctors.size(); i++) {
            Mockito.when(doctorMapper.toDTO(doctors.get(i))).thenReturn(doctorDTOs.get(i));
        }
        mockMvc.perform(get("/doctors").with(user))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(model().size(1))
            .andExpect(model().attribute("doctors", doctorDTOs));
    }

    @Test
    public void works_correct_when_there_are_no_doctors() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").roles("PATIENT").password("password");

        Mockito.when(doctorService.findAll(0)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/doctors").with(user))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(model().size(1))
            .andExpect(model().attribute("doctors", Collections.emptyList()));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void only_authenticated_users_can_see_doctor_page() throws Exception {
        int doctorId = 5;
        mockMvc.perform(get("/doctor").param("id", String.valueOf(doctorId)))
            .andExpect(status().isFound());
        verify(doctorService, Mockito.never()).findById(doctorId);
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

        List<LocalTime> times = Stream.of("08:00", "08:20", "08:40")
            .map(LocalTime::parse)
            .toList();
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
        AppointmentEntity appointment = new AppointmentEntity(null, TEST_PATIENT_ID, 1, LocalDateTime.parse("2021-10-10"));
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor notPatient =
            user("email@gmail.com").roles(role.toString()).password("password");
        when(authenticationPrincipalExtractor.getPrincipal()).thenReturn(new AuthenticationPrincipal(TEST_PATIENT_ID, Role.PATIENT));

        mockMvc.perform(post("/doctor/appointments").with(notPatient)
                .param("doctorId", "1")
                .param("inputDateTime", "2021-10-10")
            )
            .andExpect(status().isForbidden());
        verify(appointmentService, Mockito.never()).create(appointment);
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
    public void returns_error_message_when_exception_happened(
        Class<? extends Throwable> exceptionType,
        String message
    ) throws Exception {
        AppointmentEntity appointment = new AppointmentEntity(null, TEST_PATIENT_ID, 1, LocalDateTime.parse("2021-10-10T08:00"));
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor patient =
            user("email@gmail.com").roles("PATIENT").password("password");
        when(authenticationPrincipalExtractor.getPrincipal()).thenReturn(new AuthenticationPrincipal(TEST_PATIENT_ID, Role.PATIENT));
        doThrow(exceptionType).when(appointmentService).create(appointment);

        mockMvc.perform(post("/doctor/appointments").with(patient).with(csrf())
                .param("doctorId", "1")
                .param("inputDateTime", "2021-10-10T08:00")
            )
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(model().size(1))
            .andExpect(model().attributeExists("error"));
    }

    @Test
    public void saves_appointment_correct() throws Exception {
        AppointmentEntity appointment = new AppointmentEntity(null, TEST_PATIENT_ID, 1, LocalDateTime.parse("2021-10-10T08:00"));
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").roles("PATIENT").password("password");
        when(authenticationPrincipalExtractor.getPrincipal()).thenReturn(new AuthenticationPrincipal(TEST_PATIENT_ID, Role.PATIENT));
        doNothing().when(appointmentService).create(appointment);

        mockMvc.perform(post("/doctor/appointments").with(user).with(csrf())
                .param("doctorId", "1")
                .param("inputDateTime", "2021-10-10T08:00")
            )
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/doctor?id=1"));
    }

}