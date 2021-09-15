select exists(
               select *
               from appointment
                        join doctor_working_hour on appointment.doctor_working_hour_id = doctor_working_hour.id
               where appointment.patient_id = :patient_id
                 and doctor_working_hour.doctor_id = :doctor_id
                 and appointment.date = :date
           )