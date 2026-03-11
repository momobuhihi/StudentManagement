package raisetech.Student.Management.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.Student.Management.data.Course;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.domain.StudentDetail;
import raisetech.Student.Management.repository.StudentRepository;

// Serviceは何かしらの処理をする場所

@Service
public class StudentService {

  private StudentRepository repository;

  @Autowired
  public StudentService(StudentRepository repository) {
    this.repository = repository;
  }

  public List<Student> searchStudentList() {
    return repository.search();
  }

  public List<Course> searchCourseList() {
    return repository.searchCourses();
  }

  public StudentDetail searchStudent(int id) {
    Student student = repository.searchStudent(id);
    StudentDetail detail = new StudentDetail();
    detail.setStudent(student);
    return detail;
  }


  @Transactional
  public void register(StudentDetail studentDetail) {
    if (studentDetail == null || studentDetail.getStudent() == null) {
      throw new IllegalArgumentException("studentDetail or student is null");
    }
    // 学生登録
    repository.insertStudent(studentDetail.getStudent());
    Integer studentPk = studentDetail.getStudent().getId();
    // コース取り出し
    Course course = studentDetail.getStudentsCourse().get(0);
    course.setStudentPk(studentPk);
    // course_id埋め
    Integer courseId = repository.findCourseIdByName(course.getCourseName());
    if (courseId == null) {
      throw new IllegalArgumentException("存在しないコース名です: " + course.getCourseName());
    }
    course.setCourseId(courseId);

    LocalDate start = LocalDate.now();
    course.setStartDate(start);
    course.setEndDate(start.plusMonths(6));
    repository.insertCourse(course);
  }

  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    repository.updateStudent(studentDetail.getStudent());
  }
}
