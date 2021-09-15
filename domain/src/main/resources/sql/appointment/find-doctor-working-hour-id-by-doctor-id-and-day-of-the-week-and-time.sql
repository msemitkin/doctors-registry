select doctor_working_hour.id as id
from doctor_working_hour
where doctor_working_hour.doctor_id = :doctor_id
  and doctor_working_hour.time = :time
  and doctor_working_hour.day_of_the_week = :day_of_the_week;