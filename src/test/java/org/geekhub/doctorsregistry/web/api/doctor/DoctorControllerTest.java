package org.geekhub.doctorsregistry.web.api.doctor;

import org.geekhub.doctorsregistry.RolesDataProviders;
import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.domain.mapper.DoctorMapper;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.repository.specialization.SpecializationEntity;
import org.geekhub.doctorsregistry.web.dto.appointment.AppointmentDTO;
import org.geekhub.doctorsregistry.web.dto.doctor.DoctorDTO;
import org.geekhub.doctorsregistry.web.dto.specialization.SpecializationDTO;
import org.geekhub.doctorsregistry.web.security.role.Role;
import org.hamcrest.Matchers;
import org.mockito.Mockito;
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
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest extends AbstractTestNGSpringContextTests {

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
    private AppointmentMapper appointmentMapper;

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

        Mockito.when(doctorService.findAll()).thenReturn(doctors);
        for (int i = 0; i < doctors.size(); i++) {
            Mockito.when(doctorMapper.toDTO(doctors.get(i))).thenReturn(doctorDTOs.get(i));
        }

        ResultActions perform = mockMvc.perform(get("/api/doctors").with(user));
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

        Mockito.when(doctorService.findById(doctorId)).thenThrow(EntityNotFoundException.class);

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

        Mockito.when(doctorService.findById(doctorId)).thenReturn(doctor);
        Mockito.when(doctorMapper.toDTO(doctor)).thenReturn(doctorDTO);

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

        Mockito.when(doctorService.findDoctorsByClinic(clinicId)).thenReturn(doctors);
        for (int i = 0; i < doctors.size(); i++) {
            Mockito.when(doctorMapper.toDTO(doctors.get(i))).thenReturn(doctorDTOs.get(i));
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

        Mockito.when(doctorService.findDoctorsByClinic(clinicId)).thenThrow(EntityNotFoundException.class);

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
}