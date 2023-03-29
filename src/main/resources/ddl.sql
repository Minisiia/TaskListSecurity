CREATE TABLE tasks (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       created_at DATETIME
);
INSERT INTO tasks (title, description, created_at)
VALUES ('Сделать дейлики', 'Победить всех монстров из 4х ежедневных заданий', NOW()),
       ('Райден Сёгун', 'Победить еженедельного босса Райден Сёгун', NOW()),
       ('Прокачать Диону', 'Достигнуть 90 лвл в течение недели', NOW()),
       ('Пройти Бездну', 'Закончить прохождение подземелья "Бездна" на 12 этаже', NOW()),
       ('Пройти ивенты', 'Закончить прохождение всех активных ивентов', NOW()),
       ('Собрать "Заблудшего в метели"', 'Собрать полный сет артифактов "Заблудший в метели" для Дионы', NOW());

CREATE TABLE security (
    id int AUTO_INCREMENT PRIMARY KEY,
    username varchar(30) NOT NULL,
    password varchar(255),
    role VARCHAR(30)
);