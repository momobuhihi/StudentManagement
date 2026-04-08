package raisetech.Student.Management.service;

import java.time.LocalDate;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.Student.Management.data.CourseList;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.domain.StudentDetail;
import raisetech.Student.Management.repository.StudentRepository;

/**
 * 受講生情報を取り扱うサービスです。
 */
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

  public List<CourseList> searchCourseList() {
    return repository.searchCourses();
  }

  /**
   * 受講生検索です。 IDに紐づく受講生情報を取得した後、その受講生に紐づくコース情報を取得して設定します。
   *
   * @param id 受講生ID
   * @return　受講生
   */
  public StudentDetail searchStudent(int id) {
    Student student = repository.searchStudent(id);
    List<CourseList> courses = repository.searchStudentCourses(id);
    return new StudentDetail(student, courses);
  }


  @Transactional
  public StudentDetail register(StudentDetail studentDetail) {
    if (studentDetail == null || studentDetail.getStudent() == null) {
      throw new IllegalArgumentException("studentDetail or student is null");
    }
    Integer studentPk = insertStudent(studentDetail);
    CourseList courseList = initstudentcourse(studentDetail, studentPk);
    repository.insertCourse(courseList);
    return studentDetail;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   *
   * @param studentDetail
   * @param studentPk
   * @return
   */
  private @NonNull CourseList initstudentcourse(StudentDetail studentDetail, Integer studentPk) {
    CourseList course = studentDetail.getStudentsCourse().get(0);
    course.setStudentPk(studentPk);

    Integer courseId = repository.findCourseIdByName(course.getCourseName());
    if (courseId == null) {
      throw new IllegalArgumentException("存在しないコース名です: " + course.getCourseName());
    }
    course.setCourseId(courseId);
    LocalDate start = LocalDate.now();
    course.setStartDate(start);
    course.setEndDate(start.plusMonths(6));
    return course;
  }

  private Integer insertStudent(StudentDetail studentDetail) {
    repository.insertStudent(studentDetail.getStudent());
    return studentDetail.getStudent().getId();
  }

  /**
   * 受講生詳細の更新を行います。 受講生と受講生コース情報をそれぞれ更新します。
   *
   * @param studentDetail
   */
  @Transactional
  public void updateStudent(StudentDetail studentDetail) {
    Student student = studentDetail.getStudent();
    repository.updateStudent(student);
    if (studentDetail.getStudentsCourse() != null && !studentDetail.getStudentsCourse().isEmpty()) {
      CourseList courseList = studentDetail.getStudentsCourse().get(0);
      courseList.setStudentPk(student.getId());
      repository.updateCourse(courseList);
    }
  }

  @Transactional
  public void deleteStudent(int id) {
    repository.deleteStudent(id);
  }
}
