CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255)
);

CREATE TABLE matches (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user1 INT,
  user2 INT,
  user1Hand VARCHAR(255),
  user2Hand VARCHAR(255),
  isActive BOOLEAN
);

CREATE TABLE matchinfo (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user1 INT,
  user2 INT,
  user1Hand VARCHAR(255),
  isActive BOOLEAN
);
