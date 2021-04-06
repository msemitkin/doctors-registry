package org.geekhub.doctorsregistry.domain.doctor;

import org.geekhub.doctorsregistry.domain.EntityNotFoundException;
import org.geekhub.doctorsregistry.domain.appointment.appointmenttime.AppointmentTime;
import org.geekhub.doctorsregistry.domain.datime.ZonedTime;
import org.geekhub.doctorsregistry.repository.doctor.DoctorEntity;
import org.geekhub.doctorsregistry.repository.doctor.DoctorJdbcTemplateRepository;
import org.geekhub.doctorsregistry.repository.doctor.DoctorRepository;
import org.springframework.stereotype.Service;

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

    public DoctorService(DoctorRepository doctorRepository, DoctorJdbcTemplateRepository doctorJdbcTemplateRepository, ZonedTime zonedTime) {
        this.doctorRepository = doctorRepository;
        this.doctorJdbcTemplateRepository = doctorJdbcTemplateRepository;
        this.zonedTime = zonedTime;
    }

    public DoctorEntity save(DoctorEntity doctorEntity) {
        return doctorRepository.save(doctorEntity);
    }

    public List<DoctorEntity> findAll() {
        return StreamSupport.stream(doctorRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
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
