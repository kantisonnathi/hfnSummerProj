INSERT INTO vtcservice.language (id, name) VALUES (1, 'English');
INSERT INTO vtcservice.language (id, name) VALUES (2, 'Hindi');

INSERT INTO vtcservice.service (id, name) VALUES (1, 'General Information');
INSERT INTO vtcservice.service (id, name) VALUES (2, 'Counselling');

INSERT INTO vtcservice.department (id, language_id, service_id) VALUES (1, 1, 1);
INSERT INTO vtcservice.department (id, language_id, service_id) VALUES (2, 1, 2);
INSERT INTO vtcservice.department (id, language_id, service_id) VALUES (3, 2, 1);
INSERT INTO vtcservice.department (id, language_id, service_id) VALUES (4, 2, 2);

INSERT INTO vtcservice.agent (id, end_time, certified, contact_number, gender, level, missed, name, role, status, timestamp, leased_by) VALUES (1, null, true, '+917338897712', 'F', 1, null, 'Kanti Sonnathi', 'ROLE_AGENT', null, null, null);
INSERT INTO vtcservice.agent (id, end_time, certified, contact_number, gender, level, missed, name, role, status, timestamp, leased_by) VALUES (2, null, true, '+919900213110', 'M', 3, null, 'Venkat Ramana Sonnathi', 'ROLE_ADMIN', null, null, null);
INSERT INTO vtcservice.agent (id, end_time, certified, contact_number, gender, level, missed, name, role, status, timestamp, leased_by) VALUES (3, null, true, '+917338897713', 'F', 2, null, 'Varam Sonnathi', 'ROLE_TEAM_LEAD', null, null, null);

INSERT INTO vtcservice.team (id, language_id, manager_id) VALUES (1, 1, 3);

INSERT INTO vtcservice.agent_teams (agents_id, teams_id) VALUES (1, 1);
INSERT INTO vtcservice.agent_teams (agents_id, teams_id) VALUES (3, 1);


INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (1, '09:00:00', '08:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (2, '10:00:00', '09:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (3, '11:00:00', '10:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (4, '12:00:00', '11:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (5, '13:00:00', '12:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (6, '14:00:00', '13:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (7, '15:00:00', '14:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (8, '16:00:00', '15:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (9, '17:00:00', '16:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (10, '18:00:00', '17:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (11, '19:00:00', '18:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (12, '20:00:00', '19:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (13, '21:00:00', '20:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (14, '22:00:00', '21:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (15, '23:00:00', '22:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (16, '00:00:00', '23:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (17, '01:00:00', '00:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (18, '02:00:00', '01:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (19, '03:00:00', '02:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (20, '04:00:00', '03:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (21, '05:00:00', '04:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (22, '06:00:00', '05:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (23, '07:00:00', '06:00:00');
INSERT INTO vtcservice.time_slot (id, end_time, start_time) VALUES (24, '08:00:00', '07:00:00');