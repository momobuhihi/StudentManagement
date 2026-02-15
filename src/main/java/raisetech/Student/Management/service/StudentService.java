package raisetech.Student.Management.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import raisetech.Student.Management.data.Course;
import raisetech.Student.Management.data.Student;
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
}
