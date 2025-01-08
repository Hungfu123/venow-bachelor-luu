CREATE TABLE events
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id     VARCHAR(36)     NOT NULL,
    title        VARCHAR(255) NOT NULL,
    description  VARCHAR(255) NOT NULL,
    location     VARCHAR(255) NOT NULL,
    start_date   TIMESTAMP    NOT NULL,
    end_date     TIMESTAMP    NOT NULL,
    booths       INT    NOT NULL,
    organizer_id VARCHAR(36)     NOT NULL
);
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       user_id VARCHAR(36) NOT NULL UNIQUE,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       name VARCHAR(255) NOT NULL ,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL,
                       CONSTRAINT chk_role CHECK (role IN ('EXHIBITOR', 'ORGANIZER'))
);
CREATE TABLE applications (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              application_id VARCHAR(36) NOT NULL UNIQUE ,
                              business_name VARCHAR(255) NOT NULL,
                              contact_information VARCHAR(255) NOT NULL,
                              event_id VARCHAR(36) NOT NULL,
                              message VARCHAR(255) NOT NULL,
                              previous_participation BOOLEAN NOT NULL,
                              status VARCHAR(255) NOT NULL,
                              type_of_business VARCHAR(255) NOT NULL,
                              user_id VARCHAR(36) NOT NULL,
                              UNIQUE(user_id, event_id)
);
CREATE TABLE contracts (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           contract_id VARCHAR(36) NOT NULL UNIQUE,
                           organizer_id VARCHAR(36) NOT NULL,
                           exhibitor_id VARCHAR(36) NOT NULL,
                           application_id VARCHAR(36) NOT NULL,
                           terms VARCHAR(255) NOT NULL,
                           price INT,
                           booth_number VARCHAR(255),
                           status VARCHAR(255) NOT NULL,
                           FOREIGN KEY (organizer_id) REFERENCES users(user_id),
                           FOREIGN KEY (exhibitor_id) REFERENCES users(user_id),
                           FOREIGN KEY (application_id) REFERENCES applications(application_id),
                           UNIQUE(contract_id, organizer_id, exhibitor_id)
);


