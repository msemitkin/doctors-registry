package org.geekhub.doctorsregistry.domain.doctor;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.appointment.appointmenttime.AppointmentTime;
import org.geekhub.doctorsregistry.domain.datime.ZonedTime;
import org.geekhub.doctorsregistry.domain.doctorworkinghour.DoctorWorkingHourService;
import org.geekhub.doctorsregistry.domain.schedule.DayTime;
import org.geekhub.doctorsregistry.domain.schedule.DayTimeSpliterator;
import org.geekhub.doctorsregistry.domain.user.UserService;
import org.geekhub.doctorsregistry.repository.appointment.AppointmentEntity;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.repository.doctor.DoctorJdbcTemplateRepository;
import org.geekhub.doctorsregistry.repository.doctor.DoctorRepository;
import org.geekhub.doctorsregistry.repository.doctorworkinghour.DoctorWorkingHourEntity;
import org.geekhub.doctorsregistry.repository.specialization.SpecializationEntity;
import org.geekhub.doctorsregistry.web.dto.doctor.CreateDoctorUserDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorJdbcTemplateRepository doctorJdbcTemplateRepository;
    private final ZonedTime zonedTime;
    private final DayTimeSpliterator dayTimeSpliterator;
    private final DoctorWorkingHourService doctorWorkingHourService;
    private final UserService userService;

    public DoctorService(
        DoctorRepository doctorRepository,
        DoctorJdbcTemplateRepository doctorJdbcTemplateRepository,
        ZonedTime zonedTime,
        DayTimeSpliterator dayTimeSpliterator,
        DoctorWorkingHourService doctorWorkingHourService, UserService userService) {
        this.doctorRepository = doctorRepository;
        this.doctorJdbcTemplateRepository = doctorJdbcTemplateRepository;
        this.zonedTime = zonedTime;
        this.dayTimeSpliterator = dayTimeSpliterator;
        this.doctorWorkingHourService = doctorWorkingHourService;
        this.userService = userService;
    }

    public DoctorEntity save(DoctorEntity doctorEntity) {
        return doctorRepository.save(doctorEntity);
    }

    public List<DoctorEntity> findAll() {
        return StreamSupport.stream(doctorRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }

    public int getIdByEmail(String email) {
        return doctorJdbcTemplateRepository.getIdByEmail(email);
    }

    public DoctorEntity findById(int id) {
        return doctorRepository.findById(id)
            .orElseThrow(EntityNotFoundException::new);
    }

    public List<DoctorEntity> findDoctorsByClinic(Integer clinicId) {
        return doctorRepository.findDoctorEntitiesByClinicId(clinicId);
    }

    public void deleteById(int id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(id);
        }
    }


    public boolean doctorAvailable(Integer doctorId, LocalDateTime dateTime) {
        return doctorJdbcTemplateRepository.doctorWorksAt(doctorId, dateTime.getDayOfWeek(), dateTime.toLocalTime()) &&
               doctorJdbcTemplateRepository.doNotHaveAppointments(doctorId, dateTime);
    }

    @Transactional
    public void saveDoctor(CreateDoctorUserDTO doctorDTO) {
        SpecializationEntity specialization = new SpecializationEntity(doctorDTO.getSpecializationId(), null);
        DoctorEntity doctorEntity = new DoctorEntity(
            null,
            doctorDTO.getFirstName(),
            doctorDTO.getLastName(),
            specialization,
            doctorDTO.getClinicId(),
            doctorDTO.getPrice()
        );

        userService.saveUser(doctorDTO);

        DoctorEntity savedDoctor = doctorRepository.save(doctorEntity);
        List<DayTime> doctorTimetable = dayTimeSpliterator.splitToDayTime(doctorDTO.getTimetable());
        List<DoctorWorkingHourEntity> doctorWorkingHours = doctorTimetable.stream()
            .map(entry ->
                new DoctorWorkingHourEntity(
                    null,
                    savedDoctor.getId(),
                    Time.valueOf(entry.time()),
                    entry.day().getValue())
            ).collect(Collectors.toList());
        doctorWorkingHourService.setWorkingHours(doctorWorkingHours);

    }

    public Map<LocalDate, List<LocalTime>> getSchedule(Integer doctorId) {
        Map<LocalDate, List<LocalTime>> result = new TreeMap<>();
        LocalDate dateNow = zonedTime.now().toLocalDate();
        for (int deltaDays = 1; deltaDays < 8; deltaDays++) {
            LocalDate date = dateNow.plusDays(deltaDays);
            List<LocalTime> availableWorkingHours = getAvailableWorkingHours(doctorId, date);
            result.put(date, availableWorkingHours);
        }
        return result;
    }

    public List<AppointmentEntity> getPendingAppointments(Integer doctorId) {
        List<AppointmentEntity> appointments = doctorJdbcTemplateRepository.getAppointments(doctorId);
        LocalDateTime timeNow = zonedTime.now();
        return appointments.stream()
            .filter(appointment -> appointment.getDateTime().isAfter(timeNow))
            .collect(Collectors.toList());
    }

    public List<AppointmentEntity> getArchivedAppointments(Integer doctorId) {
        List<AppointmentEntity> appointments = doctorJdbcTemplateRepository.getAppointments(doctorId);
        LocalDateTime timeNow = zonedTime.now();
        return appointments.stream()
            .filter(appointment -> appointment.getDateTime().isBefore(timeNow))
            .collect(Collectors.toList());
    }

    private List<LocalTime> getAvailableWorkingHours(Integer doctorId, LocalDate date) {
        List<LocalTime> result = new ArrayList<>();
        List<LocalTime> supportedTimes = AppointmentTime.getSupportedTimes();
        for (LocalTime time : supportedTimes) {
            if (doctorAvailable(doctorId, LocalDateTime.of(date, time))) {
                result.add(time);
            }
        }
        return result;
    }

}
