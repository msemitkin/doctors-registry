select email, password, enabled
from user_credentials
where email = :email