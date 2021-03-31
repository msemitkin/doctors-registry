INSERT INTO user_credentials (email, password)
VALUES ('test_clinic1@gmail.com', '$2y$12$gyGcWLk4rykqw2wHNJvAgeNJTl9baa//awArnhL1naJmZeYJJWGHK');
INSERT INTO authority (email, authority_name)
VALUES ('test_clinic1@gmail.com', 'ROLE_CLINIC');
INSERT INTO clinic (email, name, address)
VALUES ('test_clinic1@gmail.com', 'test_clinic1_name', 'test-clinic1 address, 153');

INSERT INTO user_credentials (email, password)
VALUES ('test_clinic2@gmail.com', '$2y$12$GZbbTtKhw/s0LrRDeZXdxeLmuev89SvpvYZN8xVNG1tHhid.Vbp2q');
INSERT INTO authority (email, authority_name)
VALUES ('test_clinic2@gmail.com', 'ROLE_CLINIC');
INSERT INTO clinic (email, name, address)
VALUES ('test_clinic2@gmail.com', 'test_clinic2_name', 'test-clinic2 address, 200');

INSERT INTO user_credentials (email, password)
VALUES ('test_clinic3@gmail.com', '$2y$12$UZ3Miv2t.146YISy/IUYXOhKpYBEihgsNogvy0d6qZNUkC4e/7bkK');
INSERT INTO authority (email, authority_name)
VALUES ('test_clinic3@gmail.com', 'ROLE_CLINIC');
INSERT INTO clinic (email, name, address)
VALUES ('test_clinic3@gmail.com', 'test_clinic3_name', 'test-clinic3 address, 300');