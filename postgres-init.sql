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
-- Table users
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL   NOT NULL,
    username   VARCHAR(45) NOT NULL,
    password   VARCHAR(45) NOT NULL,
    company_id INT         NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_company1
        FOREIGN KEY (company_id)
            REFERENCES company (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE INDEX fk_user_company1_idx ON users (company_id ASC);

CREATE UNIQUE INDEX username_UNIQUE ON users (username ASC);


-- -----------------------------------------------------
-- Table radar
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS radar
(
    id         BIGSERIAL   NOT NULL,
    name       VARCHAR(45) NOT NULL,
    company_id INT         NOT NULL,
    user_id    INT         NOT NULL,
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
            ON UPDATE NO ACTION
);

CREATE INDEX fk_radar_company1_idx ON radar (company_id ASC);

CREATE INDEX fk_radar_user1_idx ON radar (user_id ASC);


-- -----------------------------------------------------
-- Table ring
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ring
(
    id         BIGSERIAL NOT NULL,
    created_at TIMESTAMP NOT NULL,
    removed_at TIMESTAMP NULL,
    radar_id   INT       NOT NULL,
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
    id         BIGSERIAL   NOT NULL,
    name       VARCHAR(45) NOT NULL,
    radar_id   INT         NOT NULL,
    position   INT         NOT NULL,
    created_at TIMESTAMP   NOT NULL,
    removed_at TIMESTAMP   NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_sector_radar1
        FOREIGN KEY (radar_id)
            REFERENCES radar (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE INDEX fk_sector_radar1_idx ON quadrant (radar_id ASC);


-- -----------------------------------------------------
-- Table blip
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS blip
(
    id                        BIGSERIAL    NOT NULL,
    name                      VARCHAR(45)  NOT NULL,
    description               VARCHAR(500) NULL,
    radar_id                  INT          NOT NULL,
    based_on_blip_template_id INT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_technology_radar1
        FOREIGN KEY (radar_id)
            REFERENCES radar (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE INDEX fk_technology_radar1_idx ON blip (radar_id ASC);


-- -----------------------------------------------------
-- Table blip_log
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS blip_log
(
    id           BIGSERIAL    NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    comment      VARCHAR(500) NOT NULL,
    version_name VARCHAR(128) NULL,
    blip_id      INT          NOT NULL,
    quadrant_id  INT          NOT NULL,
    ring_id      INT          NOT NULL,
    user_id      INT          NOT NULL,
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
-- Table ring_settings
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS ring_settings
(
    id         BIGSERIAL   NOT NULL,
    created_at TIMESTAMP   NOT NULL,
    name       VARCHAR(45) NOT NULL,
    position   INT         NOT NULL,
    ring_id    INT         NOT NULL,
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
    created_at  TIMESTAMP   NOT NULL,
    name        VARCHAR(45) NOT NULL,
    position    INT         NOT NULL,
    quadrant_id INT         NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_quadrant_settings_quadrant1
        FOREIGN KEY (quadrant_id)
            REFERENCES quadrant (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

CREATE INDEX fk_quadrant_settings_quadrant1_idx ON quadrant_settings (quadrant_id ASC);
