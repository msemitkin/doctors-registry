SELECT id
FROM user_credentials
         join doctor d on user_credentials.email = d.email
where d.email = :email