select 'patient' as user_type, count(*) as count
from patient
union
select 'doctor' as user_type, count(*) as count
from doctor
union
select 'clinic' as user_type, count(*) as count
from clinic;