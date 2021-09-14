select doctor_working_hour.time
from doctor_working_hour
where doctor_working_hour.id in
      (select doctor_working_hour.id
       from doctor_working_hour
       where doctor_id = :doctor_id
         and day_of_the_week = :day_of_the_week
           except
       select doctor_working_hour_id
       from appointment
       where date = :date
       order by id);