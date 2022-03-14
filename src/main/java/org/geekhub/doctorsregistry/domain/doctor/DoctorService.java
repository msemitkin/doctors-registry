package org.geekhub.doctorsregistry.domain.doctor;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.datime.ZonedTime;
import org.geekhub.doctorsregistry.domain.mapper.DoctorMapper;
import org.geekhub.doctorsregistry.domain.schedule.DayTime;
import org.geekhub.doctorsregistry.domain.schedule.DayTimeSpliterator;
import org.geekhub.doctorsregistry.domain.user.User;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.clinic.ClinicRepository;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.repository.doctor.DoctorJdbcTemplateRepository;
import org.geekhub.doctorsregistry.repository.doctor.DoctorRepository;
import org.geekhub.doctorsregistry.repository.doctorworkinghour.DoctorWorkingHourEntity;
import org.geekhub.doctorsregistry.repository.doctorworkinghour.DoctorWorkingHourRepository;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private static final int PAGE_SIZE = 10;
    private static final int NUMBER_OF_DAYS_IN_SCHEDULE = 7;

    private final DoctorRepository doctorRepository;
    private final DoctorJdbcTemplateRepository doctorJdbcTemplateRepository;
    private final ZonedTime zonedTime;
    private final DayTimeSpliterator dayTimeSpliterator;
    private final UserService userService;
    private final DoctorMapper doctorMapper;
    private final DoctorWorkingHourRepository doctorWorkingHourRepository;
    private final ClinicRepository clinicRepository;

    public DoctorService(
        DoctorRepository doctorRepository,
        DoctorJdbcTemplateRepository doctorJdbcTemplateRepository,
        ZonedTime zonedTime,
        DayTimeSpliterator dayTimeSpliterator,
        UserService userService,
        DoctorMapper doctorMapper,
        DoctorWorkingHourRepository doctorWorkingHourRepository,
        ClinicRepository clinicRepository
    ) {
        this.doctorRepository = doctorRepository;
        this.doctorJdbcTemplateRepository = doctorJdbcTemplateRepository;
        this.zonedTime = zonedTime;
        this.dayTimeSpliterator = dayTimeSpliterator;
        this.userService = userService;
        this.doctorMapper = doctorMapper;
        this.doctorWorkingHourRepository = doctorWorkingHourRepository;
        this.clinicRepository = clinicRepository;
    }

    public List<DoctorEntity> findAll(int page) {
        return doctorRepository.findAll(PageRequest.of(page, PAGE_SIZE)).toList();
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

    public boolean isDoctorAvailable(Integer doctorId, LocalDateTime dateTime) {
        return doctorJdbcTemplateRepository.doctorAvailable(doctorId, dateTime);
    }

    @Transactional
    public void saveDoctor(int clinicId, @NonNull CreateDoctorUserDTO doctorDTO) {
        User user = User.newDoctor(doctorDTO.getEmail(), doctorDTO.getPassword());
        userService.saveUser(user);

        DoctorEntity doctorEntity = doctorMapper.toEntity(doctorDTO, clinicId);
        Integer doctorId = doctorRepository.save(doctorEntity).getId();

        List<DayTime> doctorTimetable = dayTimeSpliterator.splitToDayTime(doctorDTO.getTimetable());
        List<DoctorWorkingHourEntity> doctorWorkingHours = doctorTimetable.stream()
            .map(entry -> new DoctorWorkingHourEntity(
                null,
                doctorId,
                entry.getTime(),
                entry.getDay().getValue())
            )
            .toList();
        doctorWorkingHourRepository.setDoctorWorkingHours(doctorWorkingHours);
    }

    public Map<LocalDate, List<LocalTime>> getSchedule(int doctorId) {
        List<LocalDate> dates = getDates();
        return dates.stream().collect(Collectors.toMap(
            Function.identity(),
            date -> doctorWorkingHourRepository.getAvailableWorkingHours(doctorId, date)
        ));
    }

    public List<AppointmentEntity> getPendingAppointments(int doctorId) {
        List<AppointmentEntity> appointments = doctorJdbcTemplateRepository.getAppointments(doctorId);
        LocalDateTime timeNow = zonedTime.now();
        return appointments.stream()
            .filter(appointment -> appointment.getDateTime().isAfter(timeNow))
            .toList();
    }

    public List<AppointmentEntity> getArchivedAppointments(int doctorId) {
        List<AppointmentEntity> appointments = doctorJdbcTemplateRepository.getAppointments(doctorId);
        LocalDateTime timeNow = zonedTime.now();
        return appointments.stream()
            .filter(appointment -> appointment.getDateTime().isBefore(timeNow))
            .toList();
    }

    private List<LocalDate> getDates() {
        LocalDate dateNow = zonedTime.now().toLocalDate();
        LocalDate tomorrowDate = dateNow.plusDays(1);
        return tomorrowDate.datesUntil(dateNow.plusDays(NUMBER_OF_DAYS_IN_SCHEDULE + 1L)).toList();
    }

}
