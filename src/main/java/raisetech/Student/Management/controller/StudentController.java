package raisetech.Student.Management.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.Student.Management.controller.converter.CourseConverter;
import raisetech.Student.Management.controller.converter.StudentConverter;
import raisetech.Student.Management.data.CourseList;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.domain.CourseDetail;
import raisetech.Student.Management.domain.StudentDetail;
import raisetech.Student.Management.service.StudentService;

/**
 * 受講生の検索や登録、更新などを行うREST　APIとして実行されるControllerです。
 */
@Validated
@RestController
public class StudentController {

  private StudentService service;
  private StudentConverter converter;
  private CourseConverter courseconverter;

  @Autowired
  public StudentController(StudentService service, StudentConverter converter,
      CourseConverter courseconverter) {
    this.service = service;
    this.converter = converter;
    this.courseconverter = courseconverter;
  }

  /**
   * 受講生一覧検索です。 全件検索を行うので、条件指定は行わないものになります。
   *
   * @return 受講生一覧（全件）
   */
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    List<Student> students = service.searchStudentList();
    List<CourseList> courses = service.searchCourseList();
    return converter.convertStudentDetails(students, courses);
  }

  /**
   * 受講生検索です。 IDに紐づく任意の受講生の情報を取得します。
   *
   * @param id 受講生ID
   * @return　受講生
   */
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable @Positive int id) {
    return service.searchStudent(id);
  }

  /**
   * コース情報の全件検索です。
   *
   * @return
   */
  @GetMapping("/courseList")
  public List<CourseDetail> getCourseList() {
    return courseconverter.convertCourseDetails(service.searchCourseList());
  }

  /**
   * 受講生の登録を行います。
   *
   * @param studentDetail 受講生詳細
   * @return　登録情報を付与した受講生詳細
   */
  @PostMapping("/registerStudent")
  public ResponseEntity<StudentDetail> registerStudent(
      @RequestBody @Valid StudentDetail studentDetail) {
    StudentDetail responseStudentDetail = service.register(studentDetail);
    return ResponseEntity.ok(responseStudentDetail);
  }

  /**
   * 受講生詳細の更新を行います。
   *
   * @param studentDetail 受講生詳細
   * @return　実行結果
   */
  @PutMapping("/updateStudent")
  public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail) {
    service.updateStudent(studentDetail);
    return ResponseEntity.ok("更新処理が成功しました。");
  }

  /**
   * 受講生のキャンセルフラグの更新を行います。
   *
   * @param id 受講生ID
   * @return　実行結果
   */
  @PostMapping("/deleteStudent/{id}")
  public ResponseEntity<String> deleteStudent(@PathVariable @Positive int id) {
    service.deleteStudent(id);
    return ResponseEntity.ok("削除処理が成功しました。");
  }

  /**
   * 受講生の復元を行います。
   *
   * @param id 受講生ID
   * @return　実行結果
   */
  @PostMapping("/restoreStudent/{id}")
  public ResponseEntity<String> restoreStudent(@PathVariable @Positive int id) {
    service.restoreStudent(id);
    return ResponseEntity.ok("復元処理が成功しました。");
  }
}
