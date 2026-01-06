CREATE DATABASE IF NOT EXISTS inventory;
USE inventory;

CREATE TABLE IF NOT EXISTS user_mysql (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    status INT NOT NULL
);
--
-- CREATE USER IF NOT EXISTS 'debezium'@'%' IDENTIFIED BY 'dbz';
--
-- GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'debezium'@'%';
--
-- FLUSH PRIVILEGES;

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

-- INSERT INTO user_mysql (name, email, status) VALUES ('Why', 'please.doe@example.com', 0);