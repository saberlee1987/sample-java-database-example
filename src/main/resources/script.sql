CREATE TABLE students
(
    id            int         not null primary key auto_increment,
    firstName     varchar(75) not null,
    lastName      varchar(95) not null,
    score         DECIMAL     not null,
    year          int         not null,
    studentNumber varchar(20) not null unique,
    address       text        not null,
    nationalCode  varchar(10) not null unique,
    isDeleted     boolean default false
) engine = InnoDB
  default character set utf8;

###############################################
CREATE TABLE capacity
(
    id    int not null primary key auto_increment,
    year  int not null,
    cnt int not null
)engine = InnoDB default character set utf8;
