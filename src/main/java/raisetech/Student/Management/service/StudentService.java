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

    // 検索処理
    // 絞り込み。年齢が30代の人のみを抽出する。
    // 抽出したリストをコントローラーに返す。
    return repository.search().stream()
        .filter(s -> s.getAge() >= 30 && s.getAge() <= 39)
        .toList();
  }

  public List<Course> searchCourseList() {
    // 絞り込み検索「Javaコース」のコース情報のみを抽出する。
    // 抽出したリストをコントローラーに返す。
    return repository.searchCourses().stream()
        .filter(c -> c.getCourseName().equals("Javaコース"))
        .toList();
  }
}
