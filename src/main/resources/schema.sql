CREATE TABLE users (
  id IDENTITY,
  name VARCHAR(50) NOT NULL
);

CREATE TABLE matches (
  id IDENTITY,
  user1 INT,
  user2 INT,
  user1Hand VARCHAR(10),
  user2Hand VARCHAR(10),
  winner INT,
  isActive BOOLEAN
);

CREATE TABLE matchinfo (
  id IDENTITY,
  user1 INT,
  user2 INT,
  user1Hand VARCHAR(10),
  isActive BOOLEAN
);
