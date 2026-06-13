CREATE TABLE IF NOT EXISTS students
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_name VARCHAR(100),
    furigana VARCHAR(100),
    nickname VARCHAR(100),
    phone_number VARCHAR(20),
    mailaddress VARCHAR(100),
    region VARCHAR(100),
    age INT,
    gender VARCHAR(10),
    remark VARCHAR(255),
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS students_courses
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_id VARCHAR(20),
    student_pk INT NOT NULL,
    course_name VARCHAR(100),
    start_date DATE,
    end_date DATE
);

CREATE TABLE application_statuses (
  id INT AUTO_INCREMENT PRIMARY KEY,
  student_course_id INT NOT NULL,
  status VARCHAR(20) NOT NULL,
  FOREIGN KEY (student_course_id)
  REFERENCES students_courses(id),
  UNIQUE (student_course_id)
);

