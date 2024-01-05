-- 0 Member (member_ID, name, email, contact_number)

CREATE TABLE Member
(   
    member_ID VARCHAR(30),
    name VARCHAR(30) NOT NULL,
    email VARCHAR(50) NOT NULL,
    contact_number VARCHAR(30) NOT NULL,
    PRIMARY KEY(member_ID)
) ENGINE = INNODB;

-- 1 GroupOrder (order_ID, member_ID)

CREATE TABLE GroupOrder
(
    order_ID VARCHAR(30),
    member_ID VARCHAR(30) NOT NULL,
    PRIMARY KEY(order_ID),
    FOREIGN KEY(member_ID) REFERENCES Member(member_ID)
) ENGINE = INNODB;

-- 2 Package (package_ID, owner_ID, order_ID, weight)

CREATE TABLE Package
(
    package_ID VARCHAR(30) NOT NULL,
    owner_ID VARCHAR(30) NOT NULL,
    order_ID VARCHAR(30) NOT NULL,
    weight INT(10) NOT NULL,
    PRIMARY KEY(package_ID),
    FOREIGN KEY(owner_ID) REFERENCES Member(member_ID),
    FOREIGN KEY(order_ID) REFERENCES GroupOrder(order_ID)
) ENGINE = INNODB;

-- 3 HomeDeliveryOrder (order_ID, address)

CREATE TABLE HomeDeliveryOrder
(
    order_ID VARCHAR(30) NOT NULL,
    address VARCHAR(100) NOT NULL,
    PRIMARY KEY (order_ID),
    FOREIGN KEY (order_ID) REFERENCES GroupOrder(order_ID)
) ENGINE = INNODB;

-- 5 ServiceArea (service_area_ID, name, parent_area_ID)

CREATE TABLE ServiceArea
(
    service_area_ID VARCHAR(30),
    name VARCHAR(50) NOT NULL,
    parent_area_ID VARCHAR(30),
    PRIMARY KEY (service_area_ID),
    FOREIGN KEY (parent_area_ID) REFERENCES ServiceArea(service_area_ID)
) ENGINE = INNODB;

-- 6 Locker (locker_ID, service_area_ID, name)

CREATE TABLE Locker
(
    locker_ID VARCHAR(30) NOT NULL,
    service_area_ID VARCHAR(30) NOT NULL,
    name VARCHAR(50) NOT NULL,
    PRIMARY KEY (locker_ID),
    FOREIGN KEY (service_area_ID) REFERENCES ServiceArea(service_area_ID)
) ENGINE = INNODB;

-- 4 LockerPickupOrder (order_ID, locker_ID, cell_number, arrival_datetime, collect_datetime)

CREATE TABLE LockerPickupOrder
(
    order_ID VARCHAR(30) NOT NULL,
    locker_ID  VARCHAR(30) NOT NULL,
    cell_number VARCHAR(30),
    arrival_datetime TIMESTAMP NULL DEFAULT NULL,
    collect_datetime TIMESTAMP NULL DEFAULT NULL ,
    PRIMARY KEY (order_ID),
    FOREIGN KEY (order_ID) REFERENCES GroupOrder(order_ID),
    FOREIGN KEY (locker_ID) REFERENCES Locker(locker_ID)
) ENGINE = INNODB;

