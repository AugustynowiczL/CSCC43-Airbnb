Drop TABLE IF EXISTS Sailors;
create table Sailors (
    sid INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    sname varchar(100) NOT NULL,
    rating INT NOT NULL,
    age INT NOT NULL
);

/* POPULATE TABLES BELONGING IN OUR DESIRED SCHEMA */

INSERT INTO Sailors (sname,rating,age) VALUES ('Carol',10,23);
INSERT INTO Sailors (sname,rating,age) VALUES('Cicely',5,10);
INSERT INTO Sailors (sname,rating, age) VALUES ('Cicely',10,22);


