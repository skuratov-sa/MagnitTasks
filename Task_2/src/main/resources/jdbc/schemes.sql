CREATE TABLE msg_headers
(
    id       INT PRIMARY KEY AUTO_INCREMENT,
    user_id  VARCHAR(150),
    msg_id   VARCHAR(150),
    group_id VARCHAR(150)
);

CREATE TABLE msg_body
(
    id      INT PRIMARY KEY AUTO_INCREMENT,
    message VARCHAR(150),
    msg_header_id INT,
    FOREIGN KEY (msg_header_id) REFERENCES msg_headers (Id)
);