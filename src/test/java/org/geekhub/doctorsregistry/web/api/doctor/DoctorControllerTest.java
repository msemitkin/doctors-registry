package org.geekhub.doctorsregistry.web.api.doctor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.geekhub.doctorsregistry.RolesDataProviders;
import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.domain.mapper.DoctorMapper;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.repository.specialization.SpecializationEntity;
import org.geekhub.doctorsregistry.web.dto.appointment.AppointmentDTO;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.geekhub.doctorsregistry.web.dto.doctor.DoctorDTO;
import org.geekhub.doctorsregistry.web.dto.specialization.SpecializationDTO;
import org.geekhub.doctorsregistry.web.security.UsernameExtractor;
import org.geekhub.doctorsregistry.web.security.role.Role;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest extends AbstractTestNGSpringContextTests {
    private static final int TEST_CLINIC_ID = 333;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    @MockBean
    private DoctorService doctorService;
    @Autowired
    @MockBean
    private DoctorMapper doctorMapper;
    @Autowired
    @MockBean
    private AppointmentMapper appointmentMapper;
    @Autowired
    @MockBean
    private UserService userService;
    @Autowired
    @MockBean
    private UsernameExtractor usernameExtractor;

    @Test(dataProvider = "roles", dataProviderClass = RolesDataProviders.class)
    public void all_roles_can_see_doctors(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").roles(role.toString()).password("password");

        List<DoctorEntity> doctors = List.of(
            new DoctorEntity(1, "name1", "surname1", "email1@gmail.com",
                new SpecializationEntity(10, "specialization1"), 1, 100),
            new DoctorEntity(1, "name2", "surname2", "email2@gmail.com",
                new SpecializationEntity(20, "specialization2"), 2, 200)
        );
        List<DoctorDTO> doctorDTOs = List.of(
            new DoctorDTO(1, "name1", "surname1",
                new SpecializationDTO(10, "specialization1"), 1, 100),
            new DoctorDTO(1, "name2", "surname2",
                new SpecializationDTO(20, "specialization2"), 2, 200)
        );

        when(doctorService.findAll(0)).thenReturn(doctors);
        for (int i = 0; i < doctors.size(); i++) {
            when(doctorMapper.toDTO(doctors.get(i))).thenReturn(doctorDTOs.get(i));
        }

        ResultActions perform = mockMvc.perform(get("/api/doctors/pages/{page}", 0).with(user));
        perform
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());

        for (int i = 0; i < doctors.size(); i++) {
            String path = "$[" + i + "]";
            DoctorDTO dto = doctorDTOs.get(i);
            perform
                .andExpect(jsonPath(path).isMap())
                .andExpect(jsonPath(path + ".id", Matchers.is(dto.getId())))
                .andExpect(jsonPath(path + ".firstName", Matchers.is(dto.getFirstName())))
                .andExpect(jsonPath(path + ".lastName", Matchers.is(dto.getLastName())))
                .andExpect(jsonPath(path + ".specialization").isMap())
                .andExpect(jsonPath(path + ".specialization.id", Matchers.is(dto.getSpecialization().getId())))
                .andExpect(jsonPath(path + ".specialization.name", Matchers.is(dto.getSpecialization().getName())))
                .andExpect(jsonPath(path + ".clinicId", Matchers.is(dto.getClinicId())))
                .andExpect(jsonPath(path + ".price", Matchers.is(dto.getPrice())));
        }
    }

    @Test
    public void returns_error_when_there_is_no_doctor_with_given_id() throws Exception {
        int doctorId = 1;
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").roles("PATIENT").password("password");

        when(doctorService.findById(doctorId)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/doctors/{id}", doctorId).with(user))
            .andExpect(status().isNotFound())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isMap())
            .andExpect(jsonPath("$.*", hasSize(1)))
            .andExpect(jsonPath("$.message", is("Requested entity does not exist")));
    }

    @Test(dataProvider = "roles", dataProviderClass = RolesDataProviders.class)
    public void all_roles_can_find_doctor_by_id(Role role) throws Exception {
        int doctorId = 1;
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").roles(role.toString()).password("password");

        DoctorEntity doctor = new DoctorEntity(1, "name", "surname", "email@gmail.com",
            new SpecializationEntity(1, "specialization"), 2, 100);
        DoctorDTO doctorDTO = new DoctorDTO(1, "name", "surname",
            new SpecializationDTO(1, "specialization"), 2, 100);

        when(doctorService.findById(doctorId)).thenReturn(doctor);
        when(doctorMapper.toDTO(doctor)).thenReturn(doctorDTO);

        mockMvc.perform(get("/api/doctors/{id}", doctorId).with(user))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isMap())
            .andExpect(jsonPath("$.*", hasSize(6)))
            .andExpect(jsonPath("$.id", Matchers.is(doctorDTO.getId())))
            .andExpect(jsonPath("$.firstName", Matchers.is(doctorDTO.getFirstName())))
            .andExpect(jsonPath("$.lastName", Matchers.is(doctorDTO.getLastName())))
            .andExpect(jsonPath("$.specialization").isMap())
            .andExpect(jsonPath("$.specialization.id", Matchers.is(doctorDTO.getSpecialization().getId())))
            .andExpect(jsonPath("$.specialization.name", Matchers.is(doctorDTO.getSpecialization().getName())))
            .andExpect(jsonPath("$.clinicId", Matchers.is(doctorDTO.getClinicId())))
            .andExpect(jsonPath("$.price", Matchers.is(doctorDTO.getPrice())));
    }

    @Test(dataProvider = "roles", dataProviderClass = RolesDataProviders.class)
    public void all_roles_can_see_doctors_by_clinic_id(Role role) throws Exception {
        int clinicId = 3;
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").roles(role.toString()).password("password");

        List<DoctorEntity> doctors = List.of(
            new DoctorEntity(1, "name1", "surname1", "email1@gmail.com",
                new SpecializationEntity(10, "specialization1"), clinicId, 100),
            new DoctorEntity(1, "name2", "surname2", "email2@gmail.com",
                new SpecializationEntity(20, "specialization2"), clinicId, 200)
        );
        List<DoctorDTO> doctorDTOs = List.of(
            new DoctorDTO(1, "name1", "surname1",
                new SpecializationDTO(10, "specialization1"), clinicId, 100),
            new DoctorDTO(1, "name2", "surname2",
                new SpecializationDTO(20, "specialization2"), clinicId, 200)
        );

        when(doctorService.findDoctorsByClinic(clinicId)).thenReturn(doctors);
        for (int i = 0; i < doctors.size(); i++) {
            when(doctorMapper.toDTO(doctors.get(i))).thenReturn(doctorDTOs.get(i));
        }

        ResultActions perform = mockMvc.perform(get("/api/clinics/{id}/doctors", clinicId).with(user));
        perform
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray());

        for (int i = 0; i < doctors.size(); i++) {
            String path = "$[" + i + "]";
            DoctorDTO dto = doctorDTOs.get(i);
            perform
                .andExpect(jsonPath(path).isMap())
                .andExpect(jsonPath(path + ".id", Matchers.is(dto.getId())))
                .andExpect(jsonPath(path + ".firstName", Matchers.is(dto.getFirstName())))
                .andExpect(jsonPath(path + ".lastName", Matchers.is(dto.getLastName())))
                .andExpect(jsonPath(path + ".specialization").isMap())
                .andExpect(jsonPath(path + ".specialization.id", Matchers.is(dto.getSpecialization().getId())))
                .andExpect(jsonPath(path + ".specialization.name", Matchers.is(dto.getSpecialization().getName())))
                .andExpect(jsonPath(path + ".clinicId", Matchers.is(dto.getClinicId())))
                .andExpect(jsonPath(path + ".price", Matchers.is(dto.getPrice())));
        }
    }

    @Test
    public void returns_error_when_there_is_no_clinic_with_given_id() throws Exception {
        int clinicId = 3;
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").roles("PATIENT").password("password");

        when(doctorService.findDoctorsByClinic(clinicId)).thenThrow(EntityNotFoundException.class);

        ResultActions perform = mockMvc.perform(get("/api/clinics/{id}/doctors", clinicId).with(user));
        perform
            .andExpect(status().isNotFound())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isMap())
            .andExpect(jsonPath("$.*", hasSize(1)))
            .andExpect(jsonPath("$.message", is("Requested entity does not exist")));
    }

    @Test(dataProvider = "roles_except_doctor", dataProviderClass = RolesDataProviders.class)
    public void only_doctors_can_see_their_pending_appointments(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").password("password").roles(role.toString());

        mockMvc.perform(get("/api/doctors/me/appointments/pending").with(user))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$").doesNotExist());
    }

    @Test(dataProvider = "roles_except_doctor", dataProviderClass = RolesDataProviders.class)
    public void only_doctors_can_see_their_archived_appointments(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").password("password").roles(role.toString());

        mockMvc.perform(get("/api/doctors/me/appointments/archive").with(user))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$").doesNotExist());
    }

    @DataProvider(name = "appointments")
    private Object[][] appointments() {
        return new Object[][]{
            {List.of(
                new AppointmentEntity(1, 1, 1, LocalDateTime.parse("2021-10-10T10:00")),
                new AppointmentEntity(2, 3, 1, LocalDateTime.parse("2021-10-12T08:00"))
            ), List.of(
                new AppointmentDTO(1, 1, 1, "2021-10-10T10:00"),
                new AppointmentDTO(2, 3, 1, "2021-10-12T08:00")
            )}
        };
    }

    @Test(dataProvider = "appointments")
    public void returns_pending_appointments_correct(
        List<AppointmentEntity> appointmentEntities,
        List<AppointmentDTO> appointmentDTOs
    ) throws Exception {
        int doctorId = 333;
        when(usernameExtractor.getDoctorId()).thenReturn(doctorId);
        when(doctorService.getPendingAppointments(doctorId)).thenReturn(appointmentEntities);
        for (int i = 0; i < appointmentEntities.size(); i++) {
            when(appointmentMapper.toDTO(appointmentEntities.get(i))).thenReturn(appointmentDTOs.get(i));
        }

        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").password("password").roles("DOCTOR");

        ResultActions perform = mockMvc.perform(get("/api/doctors/me/appointments/pending").with(user));
        perform
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(appointmentEntities.size())));
        for (int i = 0; i < appointmentEntities.size(); i++) {
            String path = "$[" + i + "]";
            AppointmentDTO dto = appointmentDTOs.get(i);
            perform
                .andExpect(jsonPath(path + ".id", Matchers.is(dto.getId())))
                .andExpect(jsonPath(path + ".patientId", Matchers.is(dto.getPatientId())))
                .andExpect(jsonPath(path + ".doctorId", Matchers.is(dto.getDoctorId())))
                .andExpect(jsonPath(path + ".dateTime", Matchers.is(dto.getDateTime())));
        }
    }

    @Test(dataProvider = "appointments")
    public void returns_pending_archived_correct(List<AppointmentEntity> appointmentEntities, List<AppointmentDTO> appointmentDTOs) throws Exception {
        int doctorId = 333;
        when(usernameExtractor.getDoctorId()).thenReturn(doctorId);
        when(doctorService.getArchivedAppointments(doctorId)).thenReturn(appointmentEntities);
        for (int i = 0; i < appointmentEntities.size(); i++) {
            when(appointmentMapper.toDTO(appointmentEntities.get(i))).thenReturn(appointmentDTOs.get(i));
        }

        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").password("password").roles("DOCTOR");

        ResultActions perform = mockMvc.perform(get("/api/doctors/me/appointments/archive").with(user));
        perform
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(appointmentEntities.size())));
        for (int i = 0; i < appointmentEntities.size(); i++) {
            String path = "$[" + i + "]";
            AppointmentDTO dto = appointmentDTOs.get(i);
            perform
                .andExpect(jsonPath(path + ".id", Matchers.is(dto.getId())))
                .andExpect(jsonPath(path + ".patientId", Matchers.is(dto.getPatientId())))
                .andExpect(jsonPath(path + ".doctorId", Matchers.is(dto.getDoctorId())))
                .andExpect(jsonPath(path + ".dateTime", Matchers.is(dto.getDateTime())));
        }
    }

    @Test(dataProvider = "roles_except_clinic", dataProviderClass = RolesDataProviders.class)
    public void only_clinic_can_register_doctor(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor notAdmin =
            user("email@gmail.com").password("password").roles(role.toString());

        mockMvc.perform(post("/api/doctors").with(notAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new CreateDoctorUserDTO())))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void returns_errors_when_given_not_valid_data() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor notAdmin =
            user("email@gmail.com").password("password").roles("CLINIC");
        CreateDoctorUserDTO doctorDTO = new CreateDoctorUserDTO("", "",
            "", null, null, Collections.emptyList(), "", "");
        when(usernameExtractor.getClinicId()).thenReturn(TEST_CLINIC_ID);
        doNothing().when(doctorService).saveDoctor(TEST_CLINIC_ID, doctorDTO);

        mockMvc.perform(post("/api/doctors")
                .with(notAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctorDTO))
            )
            .andExpect(status().isBadRequest())
            .andExpect(status().isBadRequest())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.errors").isMap())
            .andExpect(jsonPath("$.errors.firstName.message").exists())
            .andExpect(jsonPath("$.errors.lastName.message").exists())
            .andExpect(jsonPath("$.errors.specializationId.message").exists())
            .andExpect(jsonPath("$.errors.price.message").exists())
            .andExpect(jsonPath("$.errors.timetable.message").exists())
            .andExpect(jsonPath("$.errors.passwordConfirmation.message").exists());
    }

    @Test
    public void saves_doctor_correct() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor notAdmin =
            user("email@gmail.com").password("password").roles("CLINIC");
        List<String> timetable = List.of("MONDAY&10:00", "MONDAY&10:20", "TUESDAY&08:00", "TUESDAY&08:20");
        CreateDoctorUserDTO doctorDTO = new CreateDoctorUserDTO("name", "surname",
            "doctorEmail@gmail.com", 1, 100, timetable, "password", "password");
        when(usernameExtractor.getClinicId()).thenReturn(TEST_CLINIC_ID);
        doNothing().when(doctorService).saveDoctor(TEST_CLINIC_ID, doctorDTO);

        mockMvc.perform(post("/api/doctors").with(notAdmin)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctorDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$").doesNotExist());
    }
}