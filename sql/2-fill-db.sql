\c tech_radar;

INSERT INTO company (company_id, name, creation_time, last_change_time)
VALUES (1, 'HeadHunter', now(), now());
SELECT setval('company_company_id_seq', (SELECT max(company_id) FROM company));

INSERT INTO tr_user (user_id, username, password, company_id, creation_time, last_change_time)
VALUES (1, 'hh.user', 'password', 1, now(), now());
SELECT setval('tr_user_user_id_seq', (SELECT max(user_id) FROM tr_user));

INSERT INTO radar (radar_id, name, company_id, author_id, creation_time, last_change_time)
VALUES (1, 'test', 1, 1, now(), now());
SELECT setval('radar_radar_id_seq', (SELECT max(radar_id) FROM radar));

INSERT INTO ring (ring_id, name, position, radar_id, creation_time, last_change_time)
VALUES (1, 'adopt', 1, 1, now(), now()),
       (2, 'hold', 4, 1, now(), now()),
       (3, 'assess', 3, 1, now(), now()),
       (4, 'remove', 5, 1, now(), now()),
       (5, 'trial', 2, 1, now(), now());
SELECT setval('ring_ring_id_seq', (SELECT max(ring_id) FROM ring));

INSERT INTO quadrant (quadrant_id, name, position, radar_id, creation_time, last_change_time)
VALUES (1, 'Data Management', 1, 1, now(), now()),
       (2, 'Languages', 3, 1, now(), now()),
       (3, 'Platform & Insfrastructure', 4, 1, now(), now()),
       (4, 'Frameworks & Tools', 2, 1, now(), now());
SELECT setval('quadrant_quadrant_id_seq', (SELECT max(quadrant_id) FROM quadrant));

INSERT INTO blip (blip_id, name, description, quadrant_id, ring_id, radar_id, creation_time, last_change_time)
VALUES (1, 'PostgreSQL', '', 1, 1, 1, now(), now()),
       (2, 'Cassandra', '', 1, 1, 1, now(), now()),
       (3, 'Redis', '', 1, 1, 1, now(), now()),
       (4, 'Memcached', '', 1, 2, 1, now(), now()),
       (5, 'Zookeeper', '', 1, 1, 1, now(), now()),
       (6, 'ClickHouse', '', 1, 1, 1, now(), now()),
       (7, 'Airflow', '', 1, 1, 1, now(), now()),
       (8, 'Elasticsearch', '', 1, 1, 1, now(), now()),
       (9, 'Kafka', '', 1, 1, 1, now(), now()),
       (10, 'RabbitMQ', '', 1, 2, 1, now(), now()),
       (11, 'Logstash', '', 1, 3, 1, now(), now()),
       (12, 'Hadoop', '', 1, 1, 1, now(), now()),
       (13, 'Presto', '', 1, 1, 1, now(), now()),
       (14, 'Hive', '', 1, 1, 1, now(), now()),
       (15, 'Flink', '', 1, 1, 1, now(), now()),
       (16, 'Spark', '', 1, 1, 1, now(), now()),
       (17, 'Kafka Streams', '', 1, 2, 1, now(), now()),
       (18, 'Debezium', '', 1, 3, 1, now(), now()),
       (19, 'Java 17', '', 2, 1, 1, now(), now()),
       (20, 'Java 15', '', 2, 2, 1, now(), now()),
       (21, 'Java 14', '', 2, 2, 1, now(), now()),
       (22, 'Java 11', '', 2, 2, 1, now(), now()),
       (23, 'Java 8', '', 2, 4, 1, now(), now()),
       (24, 'Python 3.9', '', 2, 5, 1, now(), now()),
       (25, 'Python 3.8', '', 2, 1, 1, now(), now()),
       (26, 'Python 3.7', '', 2, 2, 1, now(), now()),
       (27, 'Python 2', '', 2, 2, 1, now(), now()),
       (28, 'Nginx', '', 3, 1, 1, now(), now()),
       (29, 'Ansible', '', 3, 1, 1, now(), now()),
       (30, 'Docker', '', 3, 1, 1, now(), now()),
       (31, 'MinIO', '', 3, 1, 1, now(), now()),
       (32, 'Sentry', '', 3, 1, 1, now(), now()),
       (33, 'Okmeter', '', 3, 1, 1, now(), now()),
       (34, 'Consul', '', 3, 1, 1, now(), now()),
       (35, 'Kubernetes', '', 3, 5, 1, now(), now()),
       (36, 'Prometheus', '', 3, 1, 1, now(), now()),
       (37, 'Bamboo', '', 3, 1, 1, now(), now()),
       (38, 'Graphana', '', 3, 1, 1, now(), now()),
       (39, 'Sonar', '', 3, 1, 1, now(), now()),
       (40, 'GitHub', '', 3, 1, 1, now(), now()),
       (41, 'PgBouncer', '', 3, 1, 1, now(), now()),
       (42, 'Filebeat', '', 4, 1, 1, now(), now()),
       (43, 'git', '', 4, 1, 1, now(), now()),
       (44, 'Maven', '', 4, 1, 1, now(), now()),
       (45, 'pip', '', 4, 1, 1, now(), now()),
       (46, 'Spring Core', '', 4, 1, 1, now(), now()),
       (47, 'SpringData', '', 4, 3, 1, now(), now()),
       (48, 'Jetty', '', 4, 1, 1, now(), now()),
       (49, 'AsyncHttpClient', '', 4, 1, 1, now(), now()),
       (50, 'Jersey', '', 4, 1, 1, now(), now()),
       (51, 'jOOQ', '', 4, 5, 1, now(), now()),
       (52, 'Hibernate', '', 4, 1, 1, now(), now()),
       (53, 'Jackson', '', 4, 1, 1, now(), now()),
       (54, 'SLF4J', '', 4, 1, 1, now(), now()),
       (55, 'Logstash', '', 4, 1, 1, now(), now()),
       (56, 'MyBatis', '', 4, 1, 1, now(), now()),
       (57, 'OpenAPI / Swagger', '', 4, 5, 1, now(), now()),
       (58, 'HikariCP', '', 4, 1, 1, now(), now()),
       (59, 'Tornado', '', 4, 1, 1, now(), now()),
       (60, 'asyncio', '', 4, 1, 1, now(), now()),
       (61, 'Flask', '', 4, 5, 1, now(), now()),
       (62, 'Gunicorn', '', 4, 5, 1, now(), now()),
       (63, 'SQLAlchemy', '', 4, 3, 1, now(), now()),
       (64, 'aiopg', 'Library for accessing a PostgreSQL database from the asyncio framework.', 4, 3, 1, now(), now()),
       (65, 'aiohttp', 'Asynchronous HTTP Client/Server for asyncio and Python.', 4, 3, 1, now(), now()),
       (66, 'PgQ', '', 4, 2, 1, now(), now()),
       (67, 'JUnit 5', '', 4, 1, 1, now(), now()),
       (68, 'JUnit 4', '', 4, 2, 1, now(), now()),
       (69, 'mockito', '', 4, 1, 1, now(), now()),
       (70, 'TestContainers', '', 4, 1, 1, now(), now()),
       (71, 'pg-embedded', '', 4, 2, 1, now(), now()),
       (72, 'pytest', '', 4, 1, 1, now(), now()),
       (73, 'unittest (python)', '', 4, 1, 1, now(), now()),
       (74, 'jib', 'Maven plugin for building Docker and OCI images for your Java applications.', 4, 1, 1, now(), now()),
       (75, 'skaffold', 'Skaffold is a command line tool that facilitates continuous development for Kubernetes applications.', 4, 1, 1, now(), now()),
       (76, 'openTelemetry',
        'OpenTelemetry is a collection of tools, APIs, and SDKs to instrument, generate, collect, and export telemetry data (metrics, logs, and traces) for analysis in order to understand your software''s performance and behavior.',
        4, 1, 1, now(), now()),
       (77, 'graphql', '', 4, 5, 1, now(), now());
SELECT setval('blip_blip_id_seq', (SELECT max(blip_id) FROM blip));
