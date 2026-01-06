CREATE DATABASE IF NOT EXISTS testmysqlDB;
USE testmysqlDB;

CREATE TABLE IF NOT EXISTS user_mysql (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    status INT NOT NULL
);

INSERT INTO user_mysql (name, email, status) VALUES
    ('John Doe', 'john.doe@example.com', 1),
    ('Jane Smith', 'jane.smith@example.com', 1),
    ('Alice Johnson', 'alice.johnson@example.com', 0),
    ('Bob Brown', 'bob.brown@example.com', 1),
    ('Charlie White', 'charlie.white@example.com', 0),
    ('David Black', 'david.black@example.com', 1),
    ('Eva Green', 'eva.green@example.com', 1),
    ('Frank Harris', 'frank.harris@example.com', 0),
    ('Grace Lee', 'grace.lee@example.com', 1),
    ('Hank Turner', 'hank.turner@example.com', 0);
