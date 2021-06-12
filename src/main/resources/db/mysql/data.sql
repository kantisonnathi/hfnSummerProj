INSERT INTO hfn.language (id, name) VALUES (1, 'English');
INSERT INTO hfn.language (id, name) VALUES (2, 'Hindi');

INSERT INTO hfn.service (id, name) VALUES (1, 'General Information');
INSERT INTO hfn.service (id, name) VALUES (2, 'Counselling');

INSERT INTO hfn.department (id, language_id, service_id) VALUES (1, 1, 1);
INSERT INTO hfn.department (id, language_id, service_id) VALUES (2, 1, 2);
INSERT INTO hfn.department (id, language_id, service_id) VALUES (3, 2, 1);
INSERT INTO hfn.department (id, language_id, service_id) VALUES (4, 2, 2);



