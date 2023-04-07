CREATE DATABASE tech_radar;
GRANT ALL PRIVILEGES ON DATABASE tech_radar TO postgres;

\c tech_radar;

-- -----------------------------------------------------
-- Table company
-- -----------------------------------------------------

CREATE TABLE IF NOT EXISTS company
(
    id   BIGSERIAL   NOT NULL,
    name VARCHAR(45) NOT NULL,
    PRIMARY KEY (id)
);


-- -----------------------------------------------------
-- Table role
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS role
(
    value VARCHAR(45) NOT NULL,
    PRIMARY KEY (value)
);


-- -----------------------------------------------------
-- Table users
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL   NOT NULL,
    username   VARCHAR(45) NOT NULL,
    password   VARCHAR(45) NOT NULL,
    role_value VARCHAR(45) NOT NULL,
    company_id INT         NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_role1
        FOREIGN KEY (role_value)
            REFERENCES role (value)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT fk_user_company1
        FOREIGN KEY (company_id)
            REFERENCES company (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE INDEX fk_user_role1_idx ON users (role_value ASC);

CREATE INDEX fk_user_company1_idx ON users (company_id ASC);

CREATE UNIQUE INDEX username_UNIQUE ON users (username ASC);


-- -----------------------------------------------------
-- Table radar_status
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS radar_status
(
    value VARCHAR(45) NOT NULL,
    PRIMARY KEY (value)
);


-- -----------------------------------------------------
-- Table radar
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS radar
(
    id                 BIGSERIAL   NOT NULL,
    name               VARCHAR(45) NOT NULL,
    company_id         BIGSERIAL   NOT NULL,
    user_id            BIGSERIAL   NOT NULL,
    radar_status_value VARCHAR(45) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_radar_company1
        FOREIGN KEY (company_id)
            REFERENCES company (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT fk_radar_user1
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT fk_radar_radar_status1
        FOREIGN KEY (radar_status_value)
            REFERENCES radar_status (value)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE INDEX fk_radar_company1_idx ON radar (company_id ASC);

CREATE INDEX fk_radar_user1_idx ON radar (user_id ASC);

CREATE INDEX fk_radar_radar_status1_idx ON radar (radar_status_value ASC);


-- -----------------------------------------------------
-- Table ring
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ring
(
    id       BIGSERIAL NOT NULL,
    birth    TIMESTAMP NOT NULL,
    death    TIMESTAMP NULL,
    radar_id BIGSERIAL NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_ring_radar1
        FOREIGN KEY (radar_id)
            REFERENCES radar (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE INDEX fk_ring_radar1_idx ON ring (radar_id ASC);


-- -----------------------------------------------------
-- Table quadrant
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS quadrant
(
    id       BIGSERIAL   NOT NULL,
    name     VARCHAR(45) NOT NULL,
    radar_id BIGSERIAL   NOT NULL,
    position INT         NOT NULL,
    birth    TIMESTAMP   NOT NULL,
    death    TIMESTAMP   NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_sector_radar1
        FOREIGN KEY (radar_id)
            REFERENCES radar (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE INDEX fk_sector_radar1_idx ON quadrant (radar_id ASC);


-- -----------------------------------------------------
-- Table blip_template
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS blip_template
(
    id          BIGSERIAL    NOT NULL,
    name        VARCHAR(45)  NOT NULL,
    description VARCHAR(500) NULL,
    PRIMARY KEY (id)
);

-- -----------------------------------------------------
-- Table blip
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS blip
(
    id                        BIGSERIAL    NOT NULL,
    name                      VARCHAR(45)  NOT NULL,
    description               VARCHAR(500) NULL,
    radar_id                  BIGSERIAL    NOT NULL,
    based_on_blip_template_id BIGSERIAL    NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_technology_radar1
        FOREIGN KEY (radar_id)
            REFERENCES radar (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT fk_technology_technology_template1
        FOREIGN KEY (based_on_blip_template_id)
            REFERENCES blip_template (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE INDEX fk_technology_radar1_idx ON blip (radar_id ASC);

CREATE INDEX fk_technology_technology_template1_idx ON blip (based_on_blip_template_id ASC);


-- -----------------------------------------------------
-- Table blip_log
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS blip_log
(
    id           BIGSERIAL    NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    comment      VARCHAR(500) NOT NULL,
    version_name VARCHAR(128) NULL,
    blip_id      BIGSERIAL    NOT NULL,
    quadrant_id  BIGSERIAL    NOT NULL,
    ring_id      BIGSERIAL    NOT NULL,
    user_id      BIGSERIAL    NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_technology_log_technology1
        FOREIGN KEY (blip_id)
            REFERENCES blip (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT fk_technology_log_sector1
        FOREIGN KEY (quadrant_id)
            REFERENCES quadrant (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT fk_technology_log_ring1
        FOREIGN KEY (ring_id)
            REFERENCES ring (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT fk_technology_log_user1
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE INDEX fk_technology_log_technology1_idx ON blip_log (blip_id ASC);

CREATE INDEX fk_technology_log_sector1_idx ON blip_log (quadrant_id ASC);

CREATE INDEX fk_technology_log_ring1_idx ON blip_log (ring_id ASC);

CREATE INDEX fk_technology_log_user1_idx ON blip_log (user_id ASC);


-- -----------------------------------------------------
-- Table employee
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS employee
(
    id          BIGSERIAL   NOT NULL,
    firstname   VARCHAR(45) NULL,
    lastname    VARCHAR(45) NULL,
    father_name VARCHAR(45) NULL,
    company_id  BIGSERIAL   NOT NULL,
    chief_id    BIGSERIAL   NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_employee_company1
        FOREIGN KEY (company_id)
            REFERENCES company (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT fk_employee_employee1
        FOREIGN KEY (chief_id)
            REFERENCES employee (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE INDEX fk_employee_company1_idx ON employee (company_id ASC);

CREATE INDEX fk_employee_employee1_idx ON employee (chief_id ASC);


-- -----------------------------------------------------
-- Table employee_technology
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS employee_technology
(
    blip_id     BIGSERIAL NOT NULL,
    employee_id BIGSERIAL NOT NULL,
    CONSTRAINT fk_employee_technology_blip1
        FOREIGN KEY (blip_id)
            REFERENCES blip (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT fk_employee_technology_employee1
        FOREIGN KEY (employee_id)
            REFERENCES employee (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE INDEX fk_employee_technology_blip1_idx ON employee_technology (blip_id ASC);

CREATE INDEX fk_employee_technology_employee1_idx ON employee_technology (employee_id ASC);


-- -----------------------------------------------------
-- Table ring_settings
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ring_settings
(
    id       BIGSERIAL   NOT NULL,
    begin    TIMESTAMP   NOT NULL,
    ended    TIMESTAMP   NULL,
    name     VARCHAR(45) NOT NULL,
    position INT         NOT NULL,
    ring_id  BIGSERIAL   NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_ring_settings_ring1
        FOREIGN KEY (ring_id)
            REFERENCES ring (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE INDEX fk_ring_settings_ring1_idx ON ring_settings (ring_id ASC);


-- -----------------------------------------------------
-- Table quadrant_settings
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS quadrant_settings
(
    id          BIGSERIAL   NOT NULL,
    begin       TIMESTAMP   NOT NULL,
    ended       TIMESTAMP   NULL,
    name        VARCHAR(45) NOT NULL,
    position    INT         NOT NULL,
    quadrant_id BIGSERIAL   NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_quadrant_settings_quadrant1
        FOREIGN KEY (quadrant_id)
            REFERENCES quadrant (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE INDEX fk_quadrant_settings_quadrant1_idx ON quadrant_settings (quadrant_id ASC);
