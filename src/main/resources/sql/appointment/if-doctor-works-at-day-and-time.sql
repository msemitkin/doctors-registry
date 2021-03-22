select exists(
               select id
               from doctor_working_hour
               where doctor_id = :doctor_id
                 and day_of_the_week = :day_of_the_week
                 and time = :time
           )