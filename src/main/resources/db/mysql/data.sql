INSERT INTO hfn.language (id, name) VALUES (1, 'English');
INSERT INTO hfn.language (id, name) VALUES (2, 'Hindi');

INSERT INTO hfn.service (id, name) VALUES (1, 'General Information');
INSERT INTO hfn.service (id, name) VALUES (2, 'Counselling');

INSERT INTO hfn.department (id, language_id, service_id) VALUES (1, 1, 1);
INSERT INTO hfn.department (id, language_id, service_id) VALUES (2, 1, 2);
INSERT INTO hfn.department (id, language_id, service_id) VALUES (3, 2, 1);
INSERT INTO hfn.department (id, language_id, service_id) VALUES (4, 2, 2);



INSERT INTO hfn.agent (id, certified, contact_number, gender, level, name, role, status, timestamp, team_id) VALUES (1, true, '+919900213110', 'M', 3, 'Venkat Sonnathi', 'ADMIN', 1, null, null);
INSERT INTO hfn.agent (id, certified, contact_number, gender, level, name, role, status, timestamp, team_id) VALUES (2, true, '+917338897712', 'F', 3, 'Kanti Sonnathi', 'TEAM_LEAD', 1, '2021-06-17 19:44:16', null);
INSERT INTO hfn.agent (id, certified, contact_number, gender, level, name, role, status, timestamp, team_id) VALUES (3, true, '+917338897713', 'F', 2, 'Varam Sonnathi', 'AGENT', 1, null, null);


INSERT INTO hfn.team (id, manager_id) VALUES (1, 2);