package org.geekhub.doctorsregistry.domain.doctor;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.datime.ZonedTime;
import org.geekhub.doctorsregistry.domain.mapper.DoctorMapper;
import org.geekhub.doctorsregistry.domain.schedule.DayTime;
import org.geekhub.doctorsregistry.domain.schedule.DayTimeSpliterator;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.clinic.ClinicRepository;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.repository.doctor.DoctorJdbcTemplateRepository;
import org.geekhub.doctorsregistry.repository.doctor.DoctorRepository;
import org.geekhub.doctorsregistry.repository.doctorworkinghour.DoctorWorkingHourEntity;
import org.geekhub.doctorsregistry.repository.doctorworkinghour.DoctorWorkingHourRepository;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.geekhub.doctorsregistry.web.security.UsernameExtractor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private static final int PAGE_SIZE = 10;

    private final DoctorRepository doctorRepository;
    private final DoctorJdbcTemplateRepository doctorJdbcTemplateRepository;
    private final ZonedTime zonedTime;
    private final DayTimeSpliterator dayTimeSpliterator;
    private final UserService userService;
    private final DoctorMapper doctorMapper;
    private final DoctorWorkingHourRepository doctorWorkingHourRepository;
    private final ClinicRepository clinicRepository;
    private final UsernameExtractor usernameExtractor;

    public DoctorService(
        DoctorRepository doctorRepository,
        DoctorJdbcTemplateRepository doctorJdbcTemplateRepository,
        ZonedTime zonedTime,
        DayTimeSpliterator dayTimeSpliterator,
        UserService userService,
        DoctorMapper doctorMapper,
        DoctorWorkingHourRepository doctorWorkingHourRepository,
        ClinicRepository clinicRepository,
        UsernameExtractor usernameExtractor
    ) {
        this.doctorRepository = doctorRepository;
        this.doctorJdbcTemplateRepository = doctorJdbcTemplateRepository;
        this.zonedTime = zonedTime;
        this.dayTimeSpliterator = dayTimeSpliterator;
        this.userService = userService;
        this.doctorMapper = doctorMapper;
        this.doctorWorkingHourRepository = doctorWorkingHourRepository;
        this.clinicRepository = clinicRepository;
        this.usernameExtractor = usernameExtractor;
    }

    public List<DoctorEntity> findAll(int page) {
        return doctorRepository.findAll(PageRequest.of(page, PAGE_SIZE)).toList();
    }

    public int getIdByEmail(String email) {
        return doctorRepository.getIdByEmail(email);
    }

    public DoctorEntity findById(int id) {
        return doctorRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    public List<DoctorEntity> findDoctorsByClinic(Integer clinicId) {
        if (clinicRepository.existsById(clinicId)) {
            return doctorRepository.findDoctorEntitiesByClinicId(clinicId);
        } else {
            throw new EntityNotFoundException();
        }
    }

    public boolean doctorAvailable(Integer doctorId, LocalDateTime dateTime) {
        return doctorJdbcTemplateRepository.doctorAvailable(doctorId, dateTime);
    }

    @Transactional
    public void saveDoctor(CreateDoctorUserDTO doctorDTO) {
        Integer clinicId = clinicRepository.getIdByEmail(usernameExtractor.getClinicUserName());
        DoctorEntity doctorEntity = doctorMapper.toEntity(doctorDTO, clinicId);

        userService.saveUser(doctorDTO);

        Integer doctorId = doctorRepository.save(doctorEntity).getId();
        List<DayTime> doctorTimetable = dayTimeSpliterator.splitToDayTime(doctorDTO.getTimetable());
        List<DoctorWorkingHourEntity> doctorWorkingHours = doctorTimetable.stream()
            .map(entry ->
                new DoctorWorkingHourEntity(
                    null,
                    doctorId,
                    Time.valueOf(entry.getTime()),
                    entry.getDay().getValue())
            ).collect(Collectors.toList());
        doctorWorkingHourRepository.setDoctorWorkingHours(doctorWorkingHours);
    }

    public Map<LocalDate, List<LocalTime>> getSchedule(Integer doctorId) {
        Map<LocalDate, List<LocalTime>> result = new TreeMap<>();
        LocalDate dateNow = zonedTime.now().toLocalDate();
        for (int deltaDays = 1; deltaDays < 8; deltaDays++) {
            LocalDate date = dateNow.plusDays(deltaDays);
            List<LocalTime> availableWorkingHours = doctorWorkingHourRepository.getAvailableWorkingHours(doctorId, date);
            result.put(date, availableWorkingHours);
        }
        return result;
    }

    public List<AppointmentEntity> getPendingAppointments() {
        String email = usernameExtractor.getDoctorUserName();
        int doctorId = getIdByEmail(email);
        List<AppointmentEntity> appointments = doctorJdbcTemplateRepository.getAppointments(doctorId);
        LocalDateTime timeNow = zonedTime.now();
        return appointments.stream()
            .filter(appointment -> appointment.getDateTime().isAfter(timeNow))
            .collect(Collectors.toList());
    }

    public List<AppointmentEntity> getArchivedAppointments() {
        String email = usernameExtractor.getDoctorUserName();
        int doctorId = getIdByEmail(email);
        List<AppointmentEntity> appointments = doctorJdbcTemplateRepository.getAppointments(doctorId);
        LocalDateTime timeNow = zonedTime.now();
        return appointments.stream()
            .filter(appointment -> appointment.getDateTime().isBefore(timeNow))
            .collect(Collectors.toList());
    }

}