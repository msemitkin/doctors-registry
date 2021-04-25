package org.geekhub.doctorsregistry.web.mvc.controller.user;

import org.geekhub.doctorsregistry.RolesDataProviders;
import org.geekhub.doctorsregistry.domain.mapper.SpecializationMapper;
import org.geekhub.doctorsregistry.domain.schedule.Schedule;
import org.geekhub.doctorsregistry.domain.specialization.SpecializationService;
import org.geekhub.doctorsregistry.repository.specialization.SpecializationEntity;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.geekhub.doctorsregistry.web.dto.specialization.SpecializationDTO;
import org.geekhub.doctorsregistry.web.security.role.Role;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ClinicUserMVCController.class)
public class ClinicUserMVCControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private Schedule schedule;
    @Autowired
    @MockBean
    private SpecializationService specializationService;
    @Autowired
    @MockBean
    private SpecializationMapper specializationMapper;

    @Test(dataProvider = "roles_except_clinic", dataProviderClass = RolesDataProviders.class)
    public void only_clinics_have_access_to_clinics_personal_cabinet(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user
            = user("clinic@gmail.com").roles(role.toString()).password("password");
        mockMvc.perform(get("/clinics/me/cabinet").with(user))
            .andExpect(status().isForbidden());
    }

    @Test
    public void clinic_has_access_to_its_personal_cabinet() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor clinic
            = user("clinic@gmail.com").roles("CLINIC").password("password");
        mockMvc.perform(get("/clinics/me/cabinet").with(clinic))
            .andExpect(status().isOk())
            .andExpect(view().name("clinic-cabinet"));
    }

    @Test
    public void model_has_all_required_attributes() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor clinic
            = user("clinic@gmail.com").roles("CLINIC").password("password");

        List<SpecializationEntity> specializations = List.of(
            new SpecializationEntity(1, "specialization1"),
            new SpecializationEntity(2, "specialization2"),
            new SpecializationEntity(3, "specialization3")
        );

        List<SpecializationDTO> specializationDTOS = List.of(
            new SpecializationDTO(1, "specialization1"),
            new SpecializationDTO(2, "specialization2"),
            new SpecializationDTO(3, "specialization3")
        );

        Mockito.when(specializationService.findAll()).thenReturn(specializations);
        for (int i = 0; i < specializations.size(); i++) {
            Mockito.when(specializationMapper.toDTO(specializations.get(i)))
                .thenReturn(specializationDTOS.get(i));
        }

        List<LocalTime> times = List.of(
            LocalTime.parse("08:00"), LocalTime.parse("08:20"), LocalTime.parse("08:40"));
        Map<DayOfWeek, List<LocalTime>> scheduleMap =
            Arrays.stream(DayOfWeek.values()).collect(Collectors.toMap(
                day -> day, day -> times));
        Mockito.when(schedule.getScheduleMap()).thenReturn(scheduleMap);

        CreateDoctorUserDTO doctor = new CreateDoctorUserDTO();
        doctor.setTimetable(new ArrayList<>());
        mockMvc.perform(get("/clinics/me/cabinet").with(clinic))
            .andExpect(model().size(3))
            .andExpect(model().attribute("specializations", specializationDTOS))
            .andExpect(model().attribute("defaultSchedule", scheduleMap))
            .andExpect(model().attribute("doctor", doctor));
    }

}