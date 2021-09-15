select exists(
               select id
               from doctor_working_hour
               where doctor_id = :doctor_id
                 and day_of_the_week = :day_of_the_week
                 and time = :time
           ) and (
           select not exists(
                   select *
                   from appointment
                            join doctor_working_hour dwh
                                 on appointment.doctor_working_hour_id = dwh.id
                   where doctor_id = :doctor_id
                     and day_of_the_week = :day_of_the_week
                     and time = :time
                     and date = :date
               )
       );