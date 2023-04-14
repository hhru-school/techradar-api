CREATE DATABASE tech_radar;
GRANT ALL PRIVILEGES ON DATABASE tech_radar TO postgres;

\c tech_radar;

-- -----------------------------------------------------
-- Table company
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS company
(
    company_id       BIGSERIAL,
    name             VARCHAR(255) NOT NULL,
    creation_time    TIMESTAMP    NOT NULL,
    last_change_time TIMESTAMP    NOT NULL,
    PRIMARY KEY (company_id)
    );


-- -----------------------------------------------------
-- Table tr_user
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS tr_user
(
    user_id          BIGSERIAL,
    username         VARCHAR(45)  NOT NULL,
    password         VARCHAR(255) NOT NULL,
    company_id       BIGINT       NULL,
    creation_time    TIMESTAMP    NOT NULL,
    last_change_time TIMESTAMP    NOT NULL,
    PRIMARY KEY (user_id),
    CONSTRAINT fk_user_company1
    FOREIGN KEY (company_id)
    REFERENCES company (company_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );

CREATE INDEX fk_user_company1_idx ON tr_user (company_id ASC);

CREATE UNIQUE INDEX username_UNIQUE ON tr_user (username ASC);


-- -----------------------------------------------------
-- Table radar
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS radar
(
    radar_id         BIGSERIAL,
    name             VARCHAR(45) NOT NULL,
    company_id       BIGINT      NOT NULL,
    author_id        BIGINT      NOT NULL,
    creation_time    TIMESTAMP   NOT NULL,
    last_change_time TIMESTAMP   NOT NULL,
    PRIMARY KEY (radar_id),
    CONSTRAINT fk_radar_company1
    FOREIGN KEY (company_id)
    REFERENCES company (company_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT fk_radar_user1
    FOREIGN KEY (author_id)
    REFERENCES tr_user (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );

CREATE INDEX fk_radar_company1_idx ON radar (company_id ASC);

CREATE INDEX fk_radar_user1_idx ON radar (author_id ASC);


-- -----------------------------------------------------
-- Table ring
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ring
(
    ring_id          BIGSERIAL,
    removed_at       TIMESTAMP NULL,
    radar_id         BIGINT    NOT NULL,
    creation_time    TIMESTAMP NOT NULL,
    last_change_time TIMESTAMP NOT NULL,
    PRIMARY KEY (ring_id),
    CONSTRAINT fk_ring_radar1
    FOREIGN KEY (radar_id)
    REFERENCES radar (radar_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );

CREATE INDEX fk_ring_radar1_idx ON ring (radar_id ASC);


-- -----------------------------------------------------
-- Table quadrant
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS quadrant
(
    quadrant_id      BIGSERIAL,
    removed_at       TIMESTAMP NULL,
    radar_id         BIGINT    NOT NULL,
    creation_time    TIMESTAMP NOT NULL,
    last_change_time TIMESTAMP NOT NULL,
    PRIMARY KEY (quadrant_id),
    CONSTRAINT fk_sector_radar1
    FOREIGN KEY (radar_id)
    REFERENCES radar (radar_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );

CREATE INDEX fk_sector_radar1_idx ON quadrant (radar_id ASC);


-- -----------------------------------------------------
-- Table blip
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS blip
(
    blip_id          BIGSERIAL,
    name             VARCHAR(45)  NOT NULL,
    description      VARCHAR(500) NULL,
    radar_id         BIGINT       NOT NULL,
    creation_time    TIMESTAMP    NOT NULL,
    last_change_time TIMESTAMP    NOT NULL,
    PRIMARY KEY (blip_id),
    CONSTRAINT fk_technology_radar1
    FOREIGN KEY (radar_id)
    REFERENCES radar (radar_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );

CREATE INDEX fk_technology_radar1_idx ON blip (radar_id ASC);


-- -----------------------------------------------------
-- Table blip_event
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS blip_event
(
    blip_event_id    BIGSERIAL,
    comment          VARCHAR(500) NULL,
    version_name     VARCHAR(128) NULL,
    blip_id          BIGINT       NOT NULL,
    quadrant_id      BIGINT       NOT NULL,
    ring_id          BIGINT       NOT NULL,
    author_id        BIGINT       NOT NULL,
    creation_time    TIMESTAMP    NOT NULL,
    last_change_time TIMESTAMP    NOT NULL,
    PRIMARY KEY (blip_event_id),
    CONSTRAINT fk_technology_log_technology1
    FOREIGN KEY (blip_id)
    REFERENCES blip (blip_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT fk_technology_log_sector1
    FOREIGN KEY (quadrant_id)
    REFERENCES quadrant (quadrant_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT fk_technology_log_ring1
    FOREIGN KEY (ring_id)
    REFERENCES ring (ring_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT fk_technology_log_user1
    FOREIGN KEY (author_id)
    REFERENCES tr_user (user_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );

CREATE INDEX fk_technology_log_technology1_idx ON blip_event (blip_id ASC);

CREATE INDEX fk_technology_log_sector1_idx ON blip_event (quadrant_id ASC);

CREATE INDEX fk_technology_log_ring1_idx ON blip_event (ring_id ASC);

CREATE INDEX fk_technology_log_user1_idx ON blip_event (author_id ASC);


-- -----------------------------------------------------
-- Table ring_setting
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ring_setting
(
    ring_setting_id  BIGSERIAL,
    name             VARCHAR(45) NOT NULL,
    position         INT         NOT NULL,
    creation_time    TIMESTAMP   NOT NULL,
    last_change_time TIMESTAMP   NOT NULL,
    ring_id          BIGINT      NOT NULL,
    PRIMARY KEY (ring_setting_id),
    CONSTRAINT fk_ring_setting_ring1
    FOREIGN KEY (ring_id)
    REFERENCES ring (ring_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );

CREATE INDEX fk_ring_setting_ring1_idx ON ring_setting (ring_id ASC);


-- -----------------------------------------------------
-- Table quadrant_setting
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS quadrant_setting
(
    quadrant_setting_id BIGSERIAL,
    name                VARCHAR(45) NOT NULL,
    position            INT         NOT NULL,
    creation_time       TIMESTAMP   NOT NULL,
    last_change_time    TIMESTAMP   NOT NULL,
    quadrant_id         BIGINT      NOT NULL,
    PRIMARY KEY (quadrant_setting_id),
    CONSTRAINT fk_quadrant_setting_quadrant1
    FOREIGN KEY (quadrant_id)
    REFERENCES quadrant (quadrant_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
    );

CREATE INDEX fk_quadrant_setting_quadrant1_idx ON quadrant_setting (quadrant_id ASC);
