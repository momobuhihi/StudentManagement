package raisetech.Student.Management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import raisetech.Student.Management.controller.converter.CourseConverter;
import raisetech.Student.Management.controller.converter.StudentConverter;
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
  @Operation(summary = "一覧検索", description = "受講生の一覧を検索します。", tags = {"Student"})
  @GetMapping("/studentList")
  public List<StudentDetail> getStudentList() {
    return converter.convertStudentDetails(service.searchStudentList(), service.searchCourseList());
  }

  /**
   * 受講生検索です。 IDに紐づく任意の受講生の情報を取得します。
   *
   * @param id 受講生ID
   * @return　受講生
   */
  @Operation(summary = "単体検索", description = "IDを指定して学生情報を1件取得します。", tags = {
      "Student"})
  @GetMapping("/student/{id}")
  public StudentDetail getStudent(@PathVariable @Positive int id) {
    return service.searchStudent(id);
  }

  /**
   * コース情報の全件検索です。
   *
   * @return
   */
  @Operation(summary = "コース情報一覧検索", description = "コース情報一覧を検索します。", tags = {
      "Course"})
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
  @Operation(summary = "受講生登録", description = "受講生を登録します。", tags = {"Student"})
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
  @Operation(summary = "受講生更新", description = "受講生を更新します。", tags = {
      "Student"}, responses = {@ApiResponse(responseCode = "200", description = "更新成功")})
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
  @Operation(summary = "受講生削除", description = "受講生を論理的削除します。", tags = {"Student"})
  @DeleteMapping("/deleteStudent/{id}")
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
  @Operation(summary = "受講生復元", description = "論理的削除した受講生を復元します。", tags = {
      "Student"})
  @PatchMapping("/restoreStudent/{id}")
  public ResponseEntity<String> restoreStudent(@PathVariable @Positive int id) {
    service.restoreStudent(id);
    return ResponseEntity.ok("復元処理が成功しました。");
  }
}
