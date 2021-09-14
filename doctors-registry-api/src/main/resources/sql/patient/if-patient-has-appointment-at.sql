select exists(
               select *
               from appointment
                        join doctor_working_hour on appointment.doctor_working_hour_id = doctor_working_hour.id
               where patient_id = :patient_id
                 and appointment.date = :date
                 and doctor_working_hour.time = :time
           )