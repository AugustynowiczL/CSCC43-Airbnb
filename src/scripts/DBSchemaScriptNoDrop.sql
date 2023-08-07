CREATE TABLE IF NOT EXISTS Users (
	id varchar(100) NOT NULL PRIMARY KEY,
	password varchar(100) NOT NULL,
	name varchar(50) NOT NULL,
	address varchar(100) NOT NULL,
	dob DATE NOT NULL,
	occupation varchar(100) NOT NULL,
	sin INT NOT NULL
);

CREATE TABLE IF NOT EXISTS PaymentInformation (
	id varchar(100) NOT NULL,
	creditcard INT NOT NULL,
	PRIMARY KEY (id, creditcard),
	FOREIGN KEY (id) REFERENCES Users(id)
);



CREATE TABLE IF NOT EXISTS Listings (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	uid varchar(100) NOT NULL,
	type INT NOT NULL,
	latitude FLOAT NOT NULL,
	longitude FLOAT NOT NULL,
	address varchar(100) NOT NULL,
	country varchar(100) NOT NULL,
	postal_code varchar(6) NOT NULL,
	city varchar(100) NOT NULL,
	FOREIGN KEY (uid) REFERENCES Users(id),
	UNIQUE KEY alternate_key (uid, latitude, longitude)
);

CREATE TABLE IF NOT EXISTS Availability (
	id INT NOT NULL,
	date DATE NOT NULL,
	isAvailable INT NOT NULL,
	price FLOAT NOT NULL,
	PRIMARY KEY (id, date),
	FOREIGN KEY (id) REFERENCES Listings(id)
);


CREATE TABLE IF NOT EXISTS Numbers (num INT);





CREATE TABLE IF NOT EXISTS Bookings (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	uid varchar(100) NOT NULL,
	lid INT NOT NULL,
	start_date DATE NOT NULL,
	end_date DATE NOT NULL,
	FOREIGN KEY (uid) REFERENCES Users(id),
	FOREIGN KEY (lid) REFERENCES Listings(id), 
	UNIQUE KEY alternate_key (lid, start_date)
);


CREATE TABLE IF NOT EXISTS RatingRenters (
	id INT NOT NULL PRIMARY KEY,
	rating INT NOT NULL,
	FOREIGN KEY (id) REFERENCES Bookings(id)
);

CREATE TABLE IF NOT EXISTS CommentRenters (
	id INT NOT NULL,
	comment varchar(200) NOT NULL,
	PRIMARY KEY (id, comment),
	FOREIGN KEY (id) REFERENCES Bookings(id)
);

CREATE TABLE IF NOT EXISTS RatingHosts (
	id INT NOT NULL PRIMARY KEY,
	rating INT NOT NULL,
	FOREIGN KEY (id) REFERENCES Bookings(id)
);

CREATE TABLE IF NOT EXISTS CommentHosts (
	id INT NOT NULL,
	comment varchar(200) NOT NULL,
	PRIMARY KEY (id, comment),
	FOREIGN KEY (id) REFERENCES Bookings(id)
);



CREATE TABLE IF NOT EXISTS ListingAmenities (
	lid INT NOT NULL,
	name varchar(100) NOT NULL,
	PRIMARY KEY (lid, name),
	FOREIGN KEY (lid) REFERENCES Listings(id)
);



CREATE TABLE IF NOT EXISTS RenterCancellations  (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
	username varchar(100) NOT NULL
);



CREATE TABLE IF NOT EXISTS HostCancellations  (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, 
	username varchar(100) NOT NULL
);


