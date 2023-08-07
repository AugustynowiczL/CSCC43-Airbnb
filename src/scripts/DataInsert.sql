INSERT INTO Numbers (num) VALUES (0),(1),(2),(3),(4),(5),(6),(7),(8),(9);

INSERT INTO Users (id, password, name, address, dob, occupation, sin)
VALUES ("test1", "test1", "test1", "1 tester street", "2000-01-01", "tester", 1000);
INSERT INTO Users (id, password, name, address, dob, occupation, sin)
VALUES ("test2", "test2", "test2", "2 tester street", "2000-02-01", "tester", 2000);
INSERT INTO Users (id, password, name, address, dob, occupation, sin)
VALUES ("test3", "test3", "test3", "3 tester street", "2000-03-01", "tester", 3000);
INSERT INTO Users (id, password, name, address, dob, occupation, sin)
VALUES ("test4", "test4", "test4", "4 tester street", "2000-04-01", "tester", 4000);

INSERT INTO PaymentInformation (id, creditcard)
VALUES ("test1", 9999);
INSERT INTO PaymentInformation (id, creditcard)
VALUES ("test2", 8999);
INSERT INTO PaymentInformation (id, creditcard)
VALUES ("test3", 7999);
INSERT INTO PaymentInformation (id, creditcard)
VALUES ("test4", 6999);

SET @year = 2023;

INSERT INTO Listings(uid, type, latitude, longitude, address, country, postal_code, city)
VALUES ("test1", 0, 10, 10, "1 tester street", "canada", "a1a1a1", "toronto");
SET @tester11 = LAST_INSERT_ID();
INSERT INTO Availability (id, date, isAvailable, price)
SELECT @tester11, DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) AS generated_date, 0, 50
FROM Numbers n1, Numbers n2, Numbers n3, Numbers n4, Numbers n5
WHERE DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) <= '2023-12-31';

INSERT INTO Listings(uid, type, latitude, longitude, address, country, postal_code, city)
VALUES ("test1", 0, 11, 11, "11 tester street", "canada", "a1a1a2", "toronto");
SET @tester12 = LAST_INSERT_ID();
INSERT INTO Availability (id, date, isAvailable, price)
SELECT @tester12, DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) AS generated_date, 0, 60
FROM Numbers n1, Numbers n2, Numbers n3, Numbers n4, Numbers n5
WHERE DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) <= '2023-12-31';

INSERT INTO Listings(uid, type, latitude, longitude, address, country, postal_code, city)
VALUES ("test1", 0, 12, 12, "111 tester street", "canada", "a1a1a3", "montreal");
SET @tester13 = LAST_INSERT_ID();
INSERT INTO Availability (id, date, isAvailable, price)
SELECT @tester13, DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) AS generated_date, 0, 70
FROM Numbers n1, Numbers n2, Numbers n3, Numbers n4, Numbers n5
WHERE DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) <= '2023-12-31';

INSERT INTO Listings(uid, type, latitude, longitude, address, country, postal_code, city)
VALUES ("test2", 0, 20, 20, "2 tester street", "usa", "a1a1a1", "new york");
SET @tester21 = LAST_INSERT_ID();
INSERT INTO Availability (id, date, isAvailable, price)
SELECT @tester21, DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) AS generated_date, 0, 80
FROM Numbers n1, Numbers n2, Numbers n3, Numbers n4, Numbers n5
WHERE DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) <= '2023-12-31';

INSERT INTO Listings(uid, type, latitude, longitude, address, country, postal_code, city)
VALUES ("test2", 0, 21, 21, "22 tester street", "canada", "b1a1a2", "toronto");
SET @tester22 = LAST_INSERT_ID();
INSERT INTO Availability (id, date, isAvailable, price)
SELECT @tester22, DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) AS generated_date, 0, 90
FROM Numbers n1, Numbers n2, Numbers n3, Numbers n4, Numbers n5
WHERE DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) <= '2023-12-31';

INSERT INTO Listings(uid, type, latitude, longitude, address, country, postal_code, city)
VALUES ("test2", 0, 22, 22, "222 tester street", "usa", "c1a1a3", "seattle");
SET @tester23 = LAST_INSERT_ID();
INSERT INTO Availability (id, date, isAvailable, price)
SELECT @tester23, DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) AS generated_date, 0, 100
FROM Numbers n1, Numbers n2, Numbers n3, Numbers n4, Numbers n5
WHERE DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) <= '2023-12-31';

INSERT INTO Listings(uid, type, latitude, longitude, address, country, postal_code, city)
VALUES ("test3", 0, 30, 30, "3 tester street", "canada", "a1a1a3", "montreal");
SET @tester31 = LAST_INSERT_ID();
INSERT INTO Availability (id, date, isAvailable, price)
SELECT @tester31, DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) AS generated_date, 0, 110
FROM Numbers n1, Numbers n2, Numbers n3, Numbers n4, Numbers n5
WHERE DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) <= '2023-12-31';

INSERT INTO Listings(uid, type, latitude, longitude, address, country, postal_code, city)
VALUES ("test3", 0, 31, 31, "33 tester street", "canada", "b1a1a1", "toronto");
SET @tester32 = LAST_INSERT_ID();
INSERT INTO Availability (id, date, isAvailable, price)
SELECT @tester32, DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) AS generated_date, 0, 120
FROM Numbers n1, Numbers n2, Numbers n3, Numbers n4, Numbers n5
WHERE DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) <= '2023-12-31';

INSERT INTO Listings(uid, type, latitude, longitude, address, country, postal_code, city)
VALUES ("test3", 0, 32, 32, "333 tester street", "usa", "c1a1a4", "seattle");
SET @tester33 = LAST_INSERT_ID();
INSERT INTO Availability (id, date, isAvailable, price)
SELECT @tester33, DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) AS generated_date, 0, 130
FROM Numbers n1, Numbers n2, Numbers n3, Numbers n4, Numbers n5
WHERE DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) <= '2023-12-31';

INSERT INTO Listings(uid, type, latitude, longitude, address, country, postal_code, city)
VALUES ("test4", 0, 40, 40, "4 tester street", "usa", "d1a1a3", "chicago");
SET @tester41 = LAST_INSERT_ID();
INSERT INTO Availability (id, date, isAvailable, price)
SELECT @tester41, DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) AS generated_date, 0, 140
FROM Numbers n1, Numbers n2, Numbers n3, Numbers n4, Numbers n5
WHERE DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) <= '2023-12-31';

INSERT INTO Listings(uid, type, latitude, longitude, address, country, postal_code, city)
VALUES ("test4", 0, 41, 41, "44 tester street", "usa", "d1a1a4", "chicago");
SET @tester42 = LAST_INSERT_ID();
INSERT INTO Availability (id, date, isAvailable, price)
SELECT @tester42, DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) AS generated_date, 0, 150
FROM Numbers n1, Numbers n2, Numbers n3, Numbers n4, Numbers n5
WHERE DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) <= '2023-12-31';

INSERT INTO Listings(uid, type, latitude, longitude, address, country, postal_code, city)
VALUES ("test4", 0, 42, 42, "444 tester street", "usa", "c1a1a3", "seattle");
SET @tester43 = LAST_INSERT_ID();
INSERT INTO Availability (id, date, isAvailable, price)
SELECT @tester43, DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) AS generated_date, 0, 160
FROM Numbers n1, Numbers n2, Numbers n3, Numbers n4, Numbers n5
WHERE DATE_ADD('2023-01-01', INTERVAL n1.num*10000 + n2.num*1000 + n3.num*100 + n4.num*10 + n5.num DAY) <= '2023-12-31';


INSERT INTO Bookings(uid, lid, start_date, end_date)
VALUES ("test2", @tester11, "2023-01-01", "2023-01-31");
SET @booking1 = LAST_INSERT_ID();
UPDATE Availability
SET isAvailable = 1
WHERE id = @tester11 AND date BETWEEN "2023-01-01" AND "2023-01-31";
INSERT INTO Bookings(uid, lid, start_date, end_date)
VALUES ("test2", @tester31, "2023-05-01", "2023-05-15");
SET @booking2 = LAST_INSERT_ID();
UPDATE Availability
SET isAvailable = 1
WHERE id = @tester31 AND date BETWEEN "2023-05-01" AND "2023-05-15";
INSERT INTO Bookings(uid, lid, start_date, end_date)
VALUES ("test2", @tester42, "2023-10-15", "2023-10-21");
SET @booking3 = LAST_INSERT_ID();
UPDATE Availability
SET isAvailable = 1
WHERE id = @tester42 AND date BETWEEN "2023-10-15" AND "2023-10-21";
INSERT INTO Bookings(uid, lid, start_date, end_date)
VALUES ("test1", @tester22, "2023-10-15", "2023-10-21");
SET @booking4 = LAST_INSERT_ID();
UPDATE Availability
SET isAvailable = 1
WHERE id = @tester22 AND date BETWEEN "2023-10-15" AND "2023-10-21";
INSERT INTO Bookings(uid, lid, start_date, end_date)
VALUES ("test1", @tester31, "2023-02-01", "2023-02-07");
SET @booking5 = LAST_INSERT_ID();
UPDATE Availability
SET isAvailable = 1
WHERE id = @tester31 AND date BETWEEN "2023-02-01" AND "2023-02-07";
INSERT INTO Bookings(uid, lid, start_date, end_date)
VALUES ("test1", @tester41, "2023-10-15", "2023-10-21");
SET @booking6 = LAST_INSERT_ID();
UPDATE Availability
SET isAvailable = 1
WHERE id = @tester41 AND date BETWEEN "2023-10-15" AND "2023-10-21";
INSERT INTO Bookings(uid, lid, start_date, end_date)
VALUES ("test3", @tester12, "2023-01-01", "2023-01-31");
SET @booking7 = LAST_INSERT_ID();
UPDATE Availability
SET isAvailable = 1
WHERE id = @tester12 AND date BETWEEN "2023-01-01" AND "2023-01-31";
INSERT INTO Bookings(uid, lid, start_date, end_date)
VALUES ("test3", @tester33, "2023-02-01", "2023-04-01");
SET @booking8 = LAST_INSERT_ID();
UPDATE Availability
SET isAvailable = 1
WHERE id = @tester33 AND date BETWEEN "2023-02-01" AND "2023-04-01";
INSERT INTO Bookings(uid, lid, start_date, end_date)
VALUES ("test3", @tester41, "2023-11-15", "2023-11-21");
SET @booking9 = LAST_INSERT_ID();
UPDATE Availability
SET isAvailable = 1
WHERE id = @tester41 AND date BETWEEN "2023-11-15" AND "2023-11-21";
INSERT INTO Bookings(uid, lid, start_date, end_date)
VALUES ("test4", @tester13, "2023-01-01", "2023-05-30");
SET @booking10 = LAST_INSERT_ID();
UPDATE Availability
SET isAvailable = 1
WHERE id = @tester13 AND date BETWEEN "2023-01-01" AND "2023-05-30";
INSERT INTO Bookings(uid, lid, start_date, end_date)
VALUES ("test4", @tester32, "2023-02-01", "2023-04-01");
SET @booking11 = LAST_INSERT_ID();
UPDATE Availability
SET isAvailable = 1
WHERE id = @tester32 AND date BETWEEN "2023-02-01" AND "2023-04-01";
INSERT INTO Bookings(uid, lid, start_date, end_date)
VALUES ("test4", @tester42, "2023-08-15", "2023-09-21");
SET @booking12 = LAST_INSERT_ID();
UPDATE Availability
SET isAvailable = 1
WHERE id = @tester42 AND date BETWEEN "2023-08-15" AND "2023-09-21";

INSERT INTO CommentHosts(id, comment)
VALUES (@booking1, "This was a very bad place I did not enjoy it bad quality");
INSERT INTO CommentHosts(id, comment)
VALUES (@booking2, "It was as expected Ok stay");
INSERT INTO CommentHosts(id, comment)
VALUES (@booking3, "I enjoyed my stay very much will come again");
INSERT INTO CommentHosts(id, comment)
VALUES (@booking4, "Host was very welcoming and undestanding");
INSERT INTO CommentHosts(id, comment)
VALUES (@booking5, "Great price for the place");
INSERT INTO CommentHosts(id, comment)
VALUES (@booking6, "Hot tub was broken no refund");
INSERT INTO CommentHosts(id, comment)
VALUES (@booking7, "Very nice view on the river");
INSERT INTO CommentHosts(id, comment)
VALUES (@booking8, "House was falling apart will not come again");
INSERT INTO CommentHosts(id, comment)
VALUES (@booking9, "Mediocore could have been better");
INSERT INTO CommentHosts(id, comment)
VALUES (@booking10, "Amazing stay for a family vacation");
INSERT INTO CommentHosts(id, comment)
VALUES (@booking11, "would not recommend");
INSERT INTO CommentHosts(id, comment)
VALUES (@booking12, "highly recommend");


INSERT INTO ListingAmenities(lid, name)
VALUES (@tester11, "wifi");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester11, "fireplace");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester11, "water");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester12, "wifi");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester12, "console");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester12, "towels");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester13, "wifi");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester13, "ac");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester13, "water");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester21, "wifi");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester21, "utensils");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester21, "water");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester21, "hottub");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester22, "fireplace");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester22, "water");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester23, "wifi");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester23, "parking");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester23, "breakfast");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester31, "laptop");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester31, "ac");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester31, "kitchen");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester32, "wifi");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester32, "parking");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester32, "breakfast");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester33, "wifi");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester33, "utensils");
INSERT INTO ListingAmenities(lid, name)
VALUES (@tester33, "hottub");

INSERT INTO RenterCancellations(username)
VALUES ("test1");
INSERT INTO RenterCancellations(username)
VALUES ("test1");
INSERT INTO RenterCancellations(username)
VALUES ("test3");
INSERT INTO RenterCancellations(username)
VALUES ("test4");
INSERT INTO RenterCancellations(username)
VALUES ("test4");

INSERT INTO HostCancellations(username)
VALUES ("test1");
INSERT INTO HostCancellations(username)
VALUES ("test2");
INSERT INTO HostCancellations(username)
VALUES ("test3");
INSERT INTO HostCancellations(username)
VALUES ("test4");