SELECT appointment.id as id, patient_id, doctor_id, date, time
from appointment
         join doctor_working_hour dwh on appointment.doctor_working_hour_id = dwh.id
where patient_id = :patient_id
order by date, time;