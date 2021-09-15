select email as username, password, enabled
from user_credentials
where email = ?