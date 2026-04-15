INSERT INTO faculties (id, name, website, email, phone, address, info)
VALUES (1, 'Faculty of Biology', 'bioweb.lnu.edu.ua', 'biolog@lnu.edu.ua', '274-03-72, 239-41-53',
        'Mykhaila Hrushevskoho Street, 4, Lviv, 79005, Ukraine, 79005', 'Faculty of Biology is ...'),
       (2, 'Faculty of Geography', 'geography.lnu.edu.ua', 'geodekanat@gmail.com', '239-41-62, 272-26-44',
        'Address: Doroshenka Street, 41, Lviv, 79000, Ukraine', 'Faculty of Geography is ...'),
       (3, 'Faculty of Geology', 'geology.lnu.edu.ua', 'decanat.geology@ukr.net', '261-60-56, 239-41-56',
        'Mykhaila Hryshevskoho Street, 4, Lviv, 79005, Ukraine, 79005', 'Faculty of Geology is ...'),
       (4, 'Faculty of Economics', 'econom.lnu.edu.ua', 'edean@lnu.edu.ua', '239-41-68',
        'Svobody Avenue, 18, Lviv, 79008, Ukraine, 79008', 'Faculty of Economics is ...'),
       (5, 'Faculty of Electronics and computer technologies', 'electronics.lnu.edu.ua',
        'electronics.faculty@lnu.edu.ua', '261-14-91, 239-47-24, 239-41-82',
        'Drahomanova Street, 50, Lviv, 79005, Ukraine', 'Faculty of Electronics and computer technologies is ...'),
       (6, 'Faculty of Journalism', 'journ.lnu.edu.ua', 'journft@lnu.edu.ua', '239-47-51',
        'Henerala Chuprynky Street, 49, Lviv, 79044, Україна', 'Faculty of Journalism is ...'),
       (7, 'Faculty of Foreign Languages', 'lingua.lnu.edu.ua', 'lingua.faculty@lnu.edu.ua', '239-47-16',
        'Universytetska Street 1/415, Lviv, 79000, Ukraine', 'Faculty of Foreign Languages is ...'),
       (8, 'Faculty of History', 'clio.lnu.edu.ua', 'clio@lnu.edu.ua', '261-03-28',
        'Universytetska Street, 1, Lviv, 79000, Ukraine', 'Faculty of History is ...'),
       (9, 'Faculty of Culture and Arts', 'kultart.lnu.edu.ua', 'fkultart@lnu.edu.ua', '239-41-97',
        'Valova Street,18, Lviv, 79008, Ukraine', 'Faculty of Culture and Arts is ...'),
       (10, 'Faculty of Mechanics and Mathematics', 'www.mmf.lnu.edu.ua', 'dmmf@lnu.edu.ua',
        '260-00-09, 239-41-74, 239-47-43', 'Universytetska Street, 1, Lviv, 79000, Ukraine',
        'Faculty of Mechanics and Mathematics is ...'),
       (11, 'Faculty of International Relations', 'intrel.lnu.edu.ua', 'intrel.faculty@lnu.edu.ua', '255-43-17',
        'Sichovykh Striltsiv Street, 19, Lviv, 79000, Ukraine, 79000', 'Faculty of International Relations is ...'),
       (12, 'Faculty of Pedagogical Education', 'pedagogy.lnu.edu.ua', 'pedosv.fakultet@ukr.net', '239-42-30',
        'Tuhan-Baranovskoho Street, 7, Lviv, 79000, Ukraine', 'Faculty of Pedagogical Education is ...'),
       (13, 'Faculty of Applied Mathematics and Informatics', 'ami.lnu.edu.ua', 'ami@lnu.edu.ua',
        '274-01-80, 239-41-86', 'Universytetska Street 1, Lviv, 79000, Ukraine',
        'Faculty of Applied Mathematics and Informatics is ...'),
       (14, 'Faculty of Financial Management and Business', 'financial.lnu.edu.ua', 'financial.faculty@lnu.edu.ua',
        '235-64-50, 235-86-54', 'Kopernyka Street, 3, Lviv, 79000, Ukraine',
        'Faculty of Financial Management and Business is ...'),
       (15, 'Faculty of Physics', 'physics.lnu.edu.ua', 'fiz_dekanat@lnu.edu.ua', '272-70-64',
        'Kyryla i Mefodiya Street, 8, Lviv, 79005, Ukraine', 'Faculty of Physics is ...'),
       (16, 'Faculty of Philology', 'philology.lnu.edu.ua', 'filologylnu@gmail.com', '255-41-33, 239-41-58, 239-41-88',
        'Universytetska Street, 1, room 232, Lviv, 79000, Ukraine', 'Faculty of Philology is ...'),
       (17, 'Faculty of Philosophy', 'filos.lnu.edu.ua', 'dfilos@lnu.edu.ua', '239-45-79',
        'Universytetska Street, 1, Lviv, 79001, Ukraine', 'Faculty of Philosophy is ...'),
       (18, 'Faculty of Chemistry', 'chem.lnu.edu.ua', 'chemdek@lnu.edu.ua', '260-03-91, 239-45-10',
        'Kyryla I Mefodiya, 6, Lviv, 79005, Ukraine', 'Faculty of Chemistry is ...'),
       (19, 'Faculty of Law', 'law.lnu.edu.ua', 'law.faculty@lnu.edu.ua', '239-41-02',
        'вул. Січових Стрільців, 14, м. Львів, 79000', 'Faculty of Law is ...');
SELECT setval('faculties_id_seq', 19);


INSERT INTO departments (id, name, faculty_id, email, phone, info)
VALUES (1,  'Department of Botany',                          1,  'botany@lnu.edu.ua',          '274-03-73', 'Department of Botany is ...'),
       (2,  'Department of Zoology',                         1,  'zoology@lnu.edu.ua',          '274-03-74', 'Department of Zoology is ...'),
       (3,  'Department of Physical Geography',              2,  'physgeo@lnu.edu.ua',          '239-41-63', 'Department of Physical Geography is ...'),
       (4,  'Department of Economic Geography',              2,  'ecogeo@lnu.edu.ua',           '239-41-64', 'Department of Economic Geography is ...'),
       (5,  'Department of Mineralogy',                      3,  'mineralogy@lnu.edu.ua',       '261-60-57', 'Department of Mineralogy is ...'),
       (6,  'Department of Economic Theory',                 4,  'econtheory@lnu.edu.ua',       '239-41-69', 'Department of Economic Theory is ...'),
       (7,  'Department of Computer Engineering',            5,  'compeng@lnu.edu.ua',          '261-14-92', 'Department of Computer Engineering is ...'),
       (8,  'Department of Electronic Systems',              5,  'elecsys@lnu.edu.ua',          '261-14-93', 'Department of Electronic Systems is ...'),
       (9,  'Department of Journalism',                      6,  'journalism@lnu.edu.ua',       '239-47-52', 'Department of Journalism is ...'),
       (10, 'Department of English Philology',               7,  'english@lnu.edu.ua',          '239-47-17', 'Department of English Philology is ...'),
       (11, 'Department of History of Ukraine',              8,  'histukr@lnu.edu.ua',          '261-03-29', 'Department of History of Ukraine is ...'),
       (12, 'Department of Theatre Arts',                    9,  'theatre@lnu.edu.ua',          '239-41-98', 'Department of Theatre Arts is ...'),
       (13, 'Department of Algebra and Logic',               10, 'algebra@lnu.edu.ua',          '239-41-75', 'Department of Algebra and Logic is ...'),
       (14, 'Department of Mathematical Analysis',           10, 'mathanalysis@lnu.edu.ua',     '239-41-76', 'Department of Mathematical Analysis is ...'),
       (15, 'Department of International Relations',         11, 'intrel@lnu.edu.ua',           '255-43-18', 'Department of International Relations is ...'),
       (16, 'Department of Pedagogy',                        12, 'pedagogy@lnu.edu.ua',         '239-42-31', 'Department of Pedagogy is ...'),
       (17, 'Department of Applied Mathematics',             13, 'appmath@lnu.edu.ua',          '239-41-87', 'Department of Applied Mathematics is ...'),
       (18, 'Department of Informatics',                     13, 'informatics@lnu.edu.ua',      '239-41-88', 'Department of Informatics is ...'),
       (19, 'Department of Finance',                         14, 'finance@lnu.edu.ua',          '235-64-51', 'Department of Finance is ...'),
       (20, 'Department of Theoretical Physics',             15, 'theorphys@lnu.edu.ua',        '272-70-65', 'Department of Theoretical Physics is ...'),
       (21, 'Department of Ukrainian Literature',            16, 'ukrlit@lnu.edu.ua',           '239-41-59', 'Department of Ukrainian Literature is ...'),
       (22, 'Department of Philosophy',                      17, 'philosophy@lnu.edu.ua',       '239-45-80', 'Department of Philosophy is ...'),
       (23, 'Department of Organic Chemistry',               18, 'orgchem@lnu.edu.ua',          '260-03-92', 'Department of Organic Chemistry is ...'),
       (24, 'Department of Civil Law',                       19, 'civillaw@lnu.edu.ua',         '239-41-03', 'Department of Civil Law is ...');
SELECT setval('departments_id_seq', 24);

INSERT INTO specialities (id, name, code, department_id, info)
VALUES (1, 'Computer Engineering',             '123', 7,  'Speciality of Computer Engineering at Faculty of Electronics and Computer Technologies'),
       (2, 'Applied Mathematics',              '113', 17, 'Speciality of Applied Mathematics at Faculty of Applied Mathematics and Informatics'),
       (3, 'Informatics',                      '126', 18, 'Speciality of Informatics at Faculty of Applied Mathematics and Informatics'),
       (4, 'Software Engineering',             '121', 7,  'Speciality of Software Engineering at Faculty of Electronics and Computer Technologies'),
       (5, 'Mathematics',                      '111', 13, 'Speciality of Mathematics at Faculty of Mechanics and Mathematics');
SELECT setval('specialities_id_seq', 5);

INSERT INTO curricula (id, name, speciality_id, year, degree, duration_years, info)
VALUES (1, 'Bachelor of Computer Engineering 2022',   1, 2022, 'bachelor', 4, 'Curriculum for bachelor degree in Computer Engineering, approved in 2022'),
       (2, 'Master of Computer Engineering 2023',     1, 2023, 'master',   2, 'Curriculum for master degree in Computer Engineering, approved in 2023'),
       (3, 'Bachelor of Applied Mathematics 2022',    2, 2022, 'bachelor', 4, 'Curriculum for bachelor degree in Applied Mathematics, approved in 2022'),
       (4, 'Bachelor of Informatics 2021',            3, 2021, 'bachelor', 4, 'Curriculum for bachelor degree in Informatics, approved in 2021'),
       (5, 'Bachelor of Software Engineering 2023',   4, 2023, 'bachelor', 4, 'Curriculum for bachelor degree in Software Engineering, approved in 2023'),
       (6, 'Bachelor of Mathematics 2020',            5, 2020, 'bachelor', 4, 'Curriculum for bachelor degree in Mathematics, approved in 2020');
SELECT setval('curricula_id_seq', 6);

INSERT INTO academic_groups (id, name, curriculum_id, enrollment_year, info)
VALUES (1,  'ECM-11', 1, 2022, 'First group of Computer Engineering, enrolled in 2022'),
       (2,  'ECM-12', 1, 2022, 'Second group of Computer Engineering, enrolled in 2022'),
       (3,  'ECM-13', 1, 2022, 'Third group of Computer Engineering, enrolled in 2022'),
       (4,  'ECM-21', 2, 2023, 'First master group of Computer Engineering, enrolled in 2023'),
       (5,  'PMM-11', 3, 2022, 'First group of Applied Mathematics, enrolled in 2022'),
       (6,  'PMM-12', 3, 2022, 'Second group of Applied Mathematics, enrolled in 2022'),
       (7,  'INF-11', 4, 2021, 'First group of Informatics, enrolled in 2021'),
       (8,  'INF-12', 4, 2021, 'Second group of Informatics, enrolled in 2021'),
       (9,  'SE-11',  5, 2023, 'First group of Software Engineering, enrolled in 2023'),
       (10, 'MTH-11', 6, 2020, 'First group of Mathematics, enrolled in 2020');
SELECT setval('academic_groups_id_seq', 10);

INSERT INTO lecturers (id, first_name, middle_name, last_name, email, phone, title, department_id, info)
VALUES (1, 'Ivan',     'Vasyliovych', 'Petrenko',   'i.petrenko@lnu.edu.ua',   '239-41-91', 'Professor',              7,  'Professor at Department of Computer Engineering'),
       (2, 'Olha',     'Mykolaivna',  'Kovalenko',  'o.kovalenko@lnu.edu.ua',  '239-41-87', 'Associate Professor',    17, 'Associate Professor at Department of Applied Mathematics'),
       (3, 'Mykhailo', 'Petrovych',   'Shevchenko', 'm.shevchenko@lnu.edu.ua', '239-41-88', 'Senior Lecturer',        18, 'Senior Lecturer at Department of Informatics'),
       (4, 'Nataliia', 'Ivanivna',    'Bondarenko', 'n.bondarenko@lnu.edu.ua', '239-41-92', 'Associate Professor',    7,  'Associate Professor at Department of Computer Engineering'),
       (5, 'Andrii',   'Stepanovych', 'Lysenko',    'a.lysenko@lnu.edu.ua',    '239-41-75', 'Professor',              13, 'Professor at Department of Algebra and Logic'),
       (6, 'Tetiana',  'Oleksandrivna','Marchenko',  't.marchenko@lnu.edu.ua',  '239-41-76', 'Assistant Professor',    14, 'Assistant Professor at Department of Mathematical Analysis');
SELECT setval('lecturers_id_seq', 6);

INSERT INTO courses (id, name, credits, hours, semester, curriculum_id, lecturer_id, info)
VALUES (1,  'Programming Fundamentals',          4, 120, 1, 1, 1,    'Introduction to programming using C/C++'),
       (2,  'Algorithms and Data Structures',    5, 150, 2, 1, 1,    'Core algorithms, complexity analysis and standard data structures'),
       (3,  'Computer Architecture',             4, 120, 1, 1, 4,    'Organization of computer systems and assembly language basics'),
       (4,  'Operating Systems',                 5, 150, 3, 1, 4,    'Process management, memory management, file systems'),
       (5,  'Database Systems',                  4, 120, 3, 1, 3,    'Relational model, SQL, transactions and database design'),
       (6,  'Web Technologies',                  4, 120, 4, 1, 1,    'HTML, CSS, JavaScript, REST API design principles'),
       (7,  'Software Engineering',              3, 90,  5, 1, 4,    'SDLC, design patterns, testing strategies, agile methodologies'),
       (8,  'Machine Learning',                  4, 120, 7, 1, 1,    'Supervised and unsupervised learning algorithms'),
       (9,  'Mathematical Analysis I',           5, 150, 1, 3, 2,    'Limits, continuity, differential calculus'),
       (10, 'Mathematical Analysis II',          5, 150, 2, 3, 2,    'Integral calculus, series, multivariable calculus'),
       (11, 'Linear Algebra',                    4, 120, 1, 3, 5,    'Vector spaces, linear transformations, eigenvalues'),
       (12, 'Discrete Mathematics',              4, 120, 1, 5, 2,    'Logic, sets, relations, graph theory and combinatorics'),
       (13, 'Object-Oriented Programming',       5, 150, 2, 4, 3,    'OOP principles, design patterns in Java'),
       (14, 'Networks and Communications',       4, 120, 4, 4, 4,    'OSI model, TCP/IP stack, network protocols'),
       (15, 'Probability Theory and Statistics', 4, 120, 3, 3, 5,    'Probability distributions, statistical inference'),
       (16, 'Differential Equations',            4, 120, 3, 6, 6,    'ODEs, PDEs, numerical methods for differential equations'),
       (17, 'Advanced Algorithms',               5, 150, 1, 2, 1,    'Graph algorithms, dynamic programming, NP-completeness'),
       (18, 'Distributed Systems',               4, 120, 2, 2, 4,    'Distributed computing models, consistency, CAP theorem'),
       (19, 'Functional Programming',            3, 90,  2, 5, 3,    'Lambda calculus, Haskell, functional patterns'),
       (20, 'Computer Vision',                   4, 120, 8, 1, 1,    'Image processing, feature extraction, deep learning for vision');
SELECT setval('courses_id_seq', 20);