package raisetech.Student.Management.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.Student.Management.data.Course;
import raisetech.Student.Management.data.Status;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.domain.ApplicationStatus;
import raisetech.Student.Management.domain.StatusDetail;
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

  public List<Course> searchCourseList() {
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
    List<Course> courses = repository.searchStudentCourses(id);
    return new StudentDetail(student, courses);
  }

  /**
   * 受講生をDBに登録し、自動採番されたIDを取得する
   *
   * @param studentDetail 登録対象の受講生情報
   * @return 登録後に採番された受講生ID
   */
  private Integer insertStudent(StudentDetail studentDetail) {
    repository.insertStudent(studentDetail.getStudent());
    return studentDetail.getStudent().getId();
  }

  /**
   * 受講生とコース情報を登録する 受講生IDは自動採番され、コース情報に紐付けられる
   *
   * @param studentDetail 登録する受講生情報
   * @return　登録された受講生
   */
  @Transactional
  public StudentDetail register(StudentDetail studentDetail) {
    if (studentDetail == null || studentDetail.getStudent() == null) {
      throw new IllegalArgumentException("studentDetail or student is null");
    }
    if (studentDetail.getStudentsCourse() == null || studentDetail.getStudentsCourse().isEmpty()) {
      throw new IllegalArgumentException("studentsCourse is null or empty");
    }
    Integer studentPk = insertStudent(studentDetail);
    for (Course course : studentDetail.getStudentsCourse()) {
      initStudentCourse(course, studentPk);
      repository.insertCourse(course);

      Status status = new Status();
      status.setStudentCourseId(course.getId());
      status.setStatus(ApplicationStatus.TEMPORARY.getLabel());
      repository.insertStatus(status);
    }
    return studentDetail;
  }

  /**
   * 受講生コース情報を登録する際の初期情報を設定する。
   *
   * @param course
   * @param studentPk
   * @return
   */
  private void initStudentCourse(Course course, Integer studentPk) {
    course.setStudentPk(studentPk);

    LocalDate start = LocalDate.now();
    course.setStartDate(start);
    course.setEndDate(start.plusMonths(6));
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
      for (Course course : studentDetail.getStudentsCourse()) {
        course.setStudentPk(student.getId());
        repository.updateCourse(course);
      }
    }
  }

  @Transactional
  public void deleteStudent(int id) {
    repository.deleteStudent(id);
  }

  @Transactional
  public void restoreStudent(int id) {
    repository.restoreStudent(id);
  }

  public StatusDetail updateStatus(Status status) {

    Status currentStatus =
        repository.searchStatus(status.getStudentCourseId());
    if (currentStatus == null) {
      throw new IllegalArgumentException(
          "申込状況が存在しません");
    }
    ApplicationStatus current =
        ApplicationStatus.fromLabel(currentStatus.getStatus());

    ApplicationStatus next =
        ApplicationStatus.fromLabel(status.getStatus());

    if (!current.canTransitionTo(next)) {
      throw new IllegalArgumentException(
          current.getLabel() + "から" +
              next.getLabel() + "には変更できません");
    }

    repository.updateStatus(status);
    return repository.searchStatusDetail(
        status.getStudentCourseId());
  }
}
