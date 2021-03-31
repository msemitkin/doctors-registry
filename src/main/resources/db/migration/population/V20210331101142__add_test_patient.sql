INSERT INTO user_credentials (email, password)
VALUES ('test_patient1@gmail.com', '$2y$12$ZR3P3EXq84F4M8duK3I8JeJOj/ATYZyMGihOZpTJmOq.mxOs9e/vy');
INSERT INTO authority (email, authority_name)
VALUES ('test_patient1@gmail.com', 'ROLE_PATIENT');
INSERT INTO patient (first_name, last_name, email)
VALUES ('Sergiy', 'Kotlyarenko', 'test_patient1@gmail.com');


INSERT INTO user_credentials (email, password)
VALUES ('test_patient2@gmail.com', '$2y$12$QUHdbGnogF7lupYCrJkds.bX328EB3XtFEzy7oPjDtBmKn76opUFi');
INSERT INTO authority (email, authority_name)
VALUES ('test_patient2@gmail.com', 'ROLE_PATIENT');
INSERT INTO patient (first_name, last_name, email)
VALUES ('Eugene', 'Liskov', 'test_patient2@gmail.com');

INSERT INTO user_credentials (email, password)
VALUES ('test_patient3@gmail.com', '$2y$12$t4pRdT9uSrHY5fzCer8Qz.TW0nl0loNFExopZ2wKqsG8e7UYP7j22');
INSERT INTO authority (email, authority_name)
VALUES ('test_patient3@gmail.com', 'ROLE_PATIENT');
INSERT INTO patient (first_name, last_name, email)
VALUES ('Khabib', 'Nurmagomedov', 'test_patient3@gmail.com');