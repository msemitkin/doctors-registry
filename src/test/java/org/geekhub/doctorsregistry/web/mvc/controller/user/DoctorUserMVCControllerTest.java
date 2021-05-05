package org.geekhub.doctorsregistry.web.mvc.controller.user;

import org.geekhub.doctorsregistry.RolesDataProviders;
import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.domain.mapper.AppointmentMapper;
import org.geekhub.doctorsregistry.domain.mapper.SpecializationMapper;
import org.geekhub.doctorsregistry.domain.schedule.Schedule;
import org.geekhub.doctorsregistry.domain.specialization.SpecializationService;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.web.dto.appointment.AppointmentDTO;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.geekhub.doctorsregistry.web.security.role.Role;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(DoctorUserMVCController.class)
public class DoctorUserMVCControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private DoctorService doctorService;
    @Autowired
    @MockBean
    private Schedule schedule;
    @Autowired
    @MockBean
    private SpecializationService specializationService;
    @Autowired
    @MockBean
    private SpecializationMapper specializationMapper;
    @Autowired
    @MockBean
    private UserService userService;

    @Test
    public void unauthorized_users_do_not_have_access_to_doctors_cabinets() throws Exception {
        mockMvc.perform(get("/doctors/me/cabinet"))
            .andExpect(status().isFound());
    }

    @Test(dataProvider = "roles_except_doctor", dataProviderClass = RolesDataProviders.class)
    public void only_doctors_have_access_to_their_personal_cabinets(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor notDoctor =
            user("email@gmail.com").roles(role.toString()).password("password");
        mockMvc.perform(get("/doctors/me/cabinet").with(notDoctor))
            .andExpect(status().isForbidden());
    }

    @Test
    public void returns_doctor_cabinet_correct() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor doctor =
            user("email@gmail.com").roles("DOCTOR").password("password");
        mockMvc.perform(get("/doctors/me/cabinet").with(doctor))
            .andExpect(status().isOk())
            .andExpect(view().name("doctor-cabinet"))
            .andExpect(model().size(0));
    }

    @Test(dataProvider = "roles_except_clinic", dataProviderClass = RolesDataProviders.class)
    public void only_clinics_can_register_doctors(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor notClinic =
            user("email@gmail.com").roles(role.toString()).password("password");
        mockMvc.perform(post("/doctors/registration").with(notClinic).with(csrf()))
            .andExpect(status().isForbidden());
    }

    @Test
    public void saves_doctor_correct() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor clinic =
            user("email@gmail.com").roles("CLINIC").password("password");
        List<String> timetable = List.of("MONDAY&10:00", "MONDAY&10:20", "TUESDAY&08:00", "TUESDAY&08:20");
        CreateDoctorUserDTO doctorDTO = new CreateDoctorUserDTO("name", "surname",
            "doctorEmail@gmail.com", 1, 100, timetable, "password", "password");

        Mockito.doNothing().when(doctorService).saveDoctor(doctorDTO);

        mockMvc.perform(post("/doctors/registration").with(clinic).with(csrf())
            .flashAttr("doctor", doctorDTO)
        )
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/clinics/me/cabinet"));
    }

    @Test
    public void returns_errors_when_submitted_empty_form() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor clinic =
            user("email@gmail.com").roles("CLINIC").password("password");

        mockMvc.perform(post("/doctors/registration").with(clinic).with(csrf()))
            .andExpect(status().isOk())
            .andExpect(model().hasErrors())
            .andExpect(model().attributeHasFieldErrors("doctor",
                "firstName", "lastName", "email", "price", "timetable", "password", "passwordConfirmation")
            );
    }
}