select appointment.id                as id,
       appointment.patient_id        as patient_id,
       doctor_working_hour.doctor_id as doctor_id,
       appointment.date              as date,
       doctor_working_hour.time      as time
from appointment
         join doctor_working_hour
              on appointment.doctor_working_hour_id = doctor_working_hour.id
where appointment.id = :id;