package raisetech.Student.Management.service;

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

  @Transactional
  public void register(StudentDetail studentDetail) {
    if (studentDetail == null || studentDetail.getStudent() == null) {
      throw new IllegalArgumentException("studentDetail or student is null");
    }
    repository.insertStudent(studentDetail.getStudent());
    Integer studentPk = studentDetail.getStudent().getId();
    Course course = studentDetail.getStudentsCourse().get(0);
    course.setStudentPk(studentPk);
    repository.insertCourse(course);
  }

}
