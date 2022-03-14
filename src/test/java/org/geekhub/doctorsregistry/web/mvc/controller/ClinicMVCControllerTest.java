package org.geekhub.doctorsregistry.web.mvc.controller;

import org.geekhub.doctorsregistry.RolesDataProviders;
import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.clinic.ClinicService;
import org.geekhub.doctorsregistry.domain.clinic.CreateClinicCommand;
import org.geekhub.doctorsregistry.domain.doctor.DoctorService;
import org.geekhub.doctorsregistry.domain.mapper.ClinicMapper;
import org.geekhub.doctorsregistry.domain.mapper.DoctorMapper;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.repository.clinic.ClinicEntity;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.repository.specialization.SpecializationEntity;
import org.geekhub.doctorsregistry.web.dto.clinic.ClinicDTO;
import org.geekhub.doctorsregistry.web.dto.clinic.CreateClinicUserDTO;
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
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ClinicMVCController.class)
public class ClinicMVCControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private ClinicService clinicService;
    @Autowired
    @MockBean
    private ClinicMapper clinicMapper;
    @Autowired
    @MockBean
    private DoctorService doctorService;
    @Autowired
    @MockBean
    private DoctorMapper doctorMapper;
    @Autowired
    @MockBean
    private UserService userService;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void only_authenticated_users_can_get_clinics_list() throws Exception {
        mockMvc.perform(get("/clinics"))
            .andExpect(status().isFound());
        Mockito.verify(clinicService, Mockito.never()).findAll(Mockito.anyInt());
    }

    @Test(dataProvider = "roles", dataProviderClass = RolesDataProviders.class)
    public void all_roles_have_access_to_clinics_list(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").roles(role.toString()).password("password");
        Mockito.when(clinicService.findAll(Mockito.anyInt())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/clinics").with(user))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(view().name("clinics"))
            .andExpect(model().size(1))
            .andExpect(model().attribute("clinics", Collections.emptyList()));
    }

    @Test
    public void returns_all_clinics() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").roles("PATIENT").password("password");
        List<ClinicEntity> clinics = List.of(
            new ClinicEntity(1, "ClinicName1", "ClinicAddress1", "ClinicEmail1@gmail.com"),
            new ClinicEntity(2, "ClinicName2", "ClinicAddress2", "ClinicEmail2@gmail.com"),
            new ClinicEntity(3, "ClinicName3", "ClinicAddress3", "ClinicEmail3@gmail.com")
        );
        List<ClinicDTO> clinicDTOs = List.of(
            new ClinicDTO(1, "ClinicName1", "ClinicAddress1"),
            new ClinicDTO(2, "ClinicName2", "ClinicAddress2"),
            new ClinicDTO(3, "ClinicName3", "ClinicAddress3")
        );
        Mockito.when(clinicService.findAll(0)).thenReturn(clinics);
        for (int i = 0; i < clinics.size(); i++) {
            Mockito.when(clinicMapper.toDTO(clinics.get(i))).thenReturn(clinicDTOs.get(i));
        }
        mockMvc.perform(get("/clinics").with(user))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(view().name("clinics"))
            .andExpect(model().size(1))
            .andExpect(model().attribute("clinics", clinicDTOs));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void only_authenticated_users_can_get_clinic_by_id() throws Exception {
        int clinicId = 12345;
        mockMvc.perform(get("/clinics"))
            .andExpect(status().isFound());
        Mockito.verify(clinicService, Mockito.never()).findById(clinicId);
    }

    @Test
    public void returns_error_message_when_clinic_with_given_id_does_not_exist() throws Exception {
        int clinicId = 12345;
        Mockito.when(clinicService.findById(clinicId)).thenThrow(EntityNotFoundException.class);
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor patient =
            user("email@gmail.com").roles("PATIENT").password("password");

        mockMvc.perform(get("/clinic").param("id", String.valueOf(clinicId)).with(patient))
            .andExpect(status().isNotFound())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(view().name("error"))
            .andExpect(model().size(1))
            .andExpect(model().attributeExists("error"));
    }

    @Test
    public void returns_clinic_when_exists_with_given_id() throws Exception {
        int clinicId = 12345;
        ClinicEntity clinicEntity = new ClinicEntity(1, "ClinicName", "ClinicAddress", "ClinicEmail@gmail.com");
        ClinicDTO clinicDTO = new ClinicDTO(1, "ClinicName", "ClinicAddress");

        Mockito.when(clinicService.findById(clinicId)).thenReturn(clinicEntity);
        Mockito.when(clinicMapper.toDTO(clinicEntity)).thenReturn(clinicDTO);
        Mockito.when(doctorService.findDoctorsByClinic(clinicId)).thenReturn(Collections.emptyList());

        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor patient =
            user("email@gmail.com").roles("PATIENT").password("password");

        mockMvc.perform(get("/clinic").param("id", String.valueOf(clinicId)).with(patient))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(view().name("clinic"))
            .andExpect(model().size(2))
            .andExpect(model().attribute("clinic", clinicDTO))
            .andExpect(model().attribute("doctors", Collections.emptyList()));
    }

    @Test
    public void returns_clinic_with_doctors() throws Exception {
        int clinicId = 12345;
        ClinicEntity clinicEntity = new ClinicEntity(1, "ClinicName", "ClinicAddress", "ClinicEmail@gmail.com");
        ClinicDTO clinicDTO = new ClinicDTO(1, "ClinicName", "ClinicAddress");
        List<DoctorEntity> doctors = List.of(
            new DoctorEntity(1, "DoctorName1", "DoctorSurname1", "DoctorEmail1@gmail.com",
                new SpecializationEntity(2, "specialization1"), clinicId, 100),
            new DoctorEntity(10, "DoctorName10", "DoctorSurname10", "DoctorEmail10@gmail.com",
                new SpecializationEntity(12, "specialization12"), clinicId, 200)
        );
        List<DoctorDTO> doctorDTOs = List.of(
            new DoctorDTO(1, "DoctorName1", "DoctorSurname1",
                new SpecializationDTO(2, "specialization1"), clinicId, 100),
            new DoctorDTO(10, "DoctorName10", "DoctorSurname10",
                new SpecializationDTO(12, "specialization12"), clinicId, 200)
        );

        Mockito.when(clinicService.findById(clinicId)).thenReturn(clinicEntity);
        Mockito.when(clinicMapper.toDTO(clinicEntity)).thenReturn(clinicDTO);
        Mockito.when(doctorService.findDoctorsByClinic(clinicId)).thenReturn(doctors);
        for (int i = 0; i < doctors.size(); i++) {
            Mockito.when(doctorMapper.toDTO(doctors.get(i))).thenReturn(doctorDTOs.get(i));
        }

        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor patient =
            user("email@gmail.com").roles("PATIENT").password("password");

        mockMvc.perform(get("/clinic").param("id", String.valueOf(clinicId)).with(patient))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(view().name("clinic"))
            .andExpect(model().size(2))
            .andExpect(model().attribute("clinic", clinicDTO))
            .andExpect(model().attribute("doctors", doctorDTOs));
    }

    @Test(dataProvider = "roles_except_admin", dataProviderClass = RolesDataProviders.class)
    public void only_admin_can_register_clinic(Role role) throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor user =
            user("email@gmail.com").roles(role.toString()).password("password");

        mockMvc.perform(post("/clinics").with(user).with(csrf()))
            .andExpect(status().isForbidden());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void admin_can_register_clinic() throws Exception {
        CreateClinicUserDTO clinicDTO = new CreateClinicUserDTO("name", "address",
            "clinicEmail@gmail.com", "password", "password");
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor admin =
            user("email@gmail.com").roles("ADMIN").password("password");

        CreateClinicCommand createClinicCommand = new CreateClinicCommand(
            clinicDTO.getEmail(), clinicDTO.getName(), clinicDTO.getAddress(), clinicDTO.getPassword());
        Mockito.doNothing().when(clinicService).save(createClinicCommand);

        mockMvc.perform(post("/clinics").with(admin).with(csrf())
            .param("name", clinicDTO.getName())
            .param("address", clinicDTO.getAddress())
            .param("email", clinicDTO.getEmail())
            .param("password", clinicDTO.getPassword())
            .param("passwordConfirmation", clinicDTO.getPasswordConfirmation())
        )
            .andExpect(status().isFound())
            .andExpect(redirectedUrl("/index"));
        Mockito.verify(clinicService, Mockito.times(1)).save(createClinicCommand);
    }

    @Test
    public void returns_errors_when_submit_empty_form() throws Exception {
        SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor admin =
            user("email@gmail.com").roles("ADMIN").password("password");
        mockMvc.perform(post("/clinics").with(admin).with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("admin-cabinet"))
            .andExpect(model().hasErrors())
            .andExpect(model().attributeHasFieldErrors("clinic",
                "name", "address", "email", "password", "passwordConfirmation"));
    }

}