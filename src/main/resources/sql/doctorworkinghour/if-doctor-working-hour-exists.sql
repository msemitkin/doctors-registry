select exists(
               select *
               from doctor_working_hour
               where doctor_id = :doctor_id
                 and time = :time
                 and day_of_the_week = :day_of_the_week
           );