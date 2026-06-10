package raisetech.Student.Management.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.Student.Management.data.Course;
import raisetech.Student.Management.data.Student;

@MybatisTest
class StudentRepositoryTest {

  @Autowired
  private StudentRepository sut;

  @Test
  void 受講生の在籍検索が行えること() {
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(4);
  }

  @Test
  void 受講生のコース情報の全件検索が行えること() {
    List<Course> actual = sut.searchCourses();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 在籍受講生の検索が行えること() {
    Student actual = sut.searchStudent(1);
    assertThat(actual).isNotNull();
    assertThat(actual.getId()).isEqualTo(1);
  }

  @Test
  void 受講生IDに紐づく受講生コース情報の検索が行えること() {
    List<Course> actual = sut.searchStudentCourses(1);
    assertThat(actual.size()).isEqualTo(1);
  }

  @Test
  void コース名からコースIDを取得できること() {
    Integer actual = sut.findCourseIdByName("Javaコース");
    assertThat(actual).isEqualTo(1);
  }

  @Test
  void 受講生の登録が行えること() {
    Student student = new Student();
    student.setStudentName("山田太郎");
    student.setFurigana("ヤマダタロウ");
    student.setNickname("タロー");
    student.setPhoneNumber("09012345678");
    student.setMailaddress("test@example.com");
    student.setRegion("東京都");
    student.setAge(20);
    student.setGender("男性");
    student.setRemark("");
    student.setDeleted(false);

    sut.insertStudent(student);
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(5);
  }

  @Test
  void 受講生コース情報を新規登録できること() {
    Course course = new Course();
    course.setCourseId(1);
    course.setStudentPk(1);
    course.setCourseName("Javaコース");
    course.setStartDate(LocalDate.of(2026, 1, 1));
    course.setEndDate(LocalDate.of(2026, 7, 1));

    sut.insertCourse(course);
    List<Course> actual = sut.searchStudentCourses(1);
    assertThat(actual.size()).isEqualTo(2);
  }

  @Test
  void 受講生を更新できること() {
    Student student = sut.searchStudent(1);
    student.setStudentName("更新太郎");
    sut.updateStudent(student);
    Student actual = sut.searchStudent(1);
    assertThat(actual.getStudentName()).isEqualTo("更新太郎");
  }

  @Test
  void 受講生コース情報のコース名を更新できること() {
    Course course = sut.searchStudentCourses(1).get(0);
    course.setCourseName("AWSコース");
    sut.updateCourse(course);
    Course actual = sut.searchStudentCourses(1).get(0);
    assertThat(actual.getCourseName()).isEqualTo("AWSコース");
  }

  @Test
  void 受講生を論理削除できること() {
    sut.deleteStudent(1);
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(3);
  }

  @Test
  void 論理削除した受講生を復元できること() {
    sut.deleteStudent(1);
    sut.restoreStudent(1);
    List<Student> actual = sut.search();
    assertThat(actual.size()).isEqualTo(4);
  }
}