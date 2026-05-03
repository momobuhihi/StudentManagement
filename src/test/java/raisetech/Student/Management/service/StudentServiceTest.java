package raisetech.Student.Management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.Student.Management.data.Course;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.domain.StudentDetail;
import raisetech.Student.Management.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  private StudentService sut;

  @BeforeEach
  void before() {
    sut = new StudentService(repository);
  }

  @Test
  void 受講生詳細の全件検索が動作すること() {
    List<Student> expected = new ArrayList<>();
    when(repository.search()).thenReturn(expected);

    List<Student> actual = sut.searchStudentList();

    assertEquals(expected, actual);
    verify(repository).search();
  }

  @Test
  void 受講生の単一検索が動作すること() {
    int sID = 1;

    Student student = new Student();
    List<Course> courses = new ArrayList<>();
    when(repository.searchStudent(sID)).thenReturn(student);
    when(repository.searchStudentCourses(sID)).thenReturn(courses);

    StudentDetail actual = sut.searchStudent(sID);

    assertEquals(student, actual.getStudent());
    assertEquals(courses, actual.getStudentsCourse());

    verify(repository).searchStudent(sID);
    verify(repository).searchStudentCourses(sID);
  }

  @Test
  void 受講生の登録が動作すること() {
    int sID = 100;
    Student student = new Student();
    student.setId(sID);
    Course course = new Course();
    course.setCourseName("Javaコース");
    StudentDetail studentDetail = new StudentDetail(student, List.of(course));

    when(repository.findCourseIdByName("Javaコース")).thenReturn(sID);
    StudentDetail actual = sut.register(studentDetail);
    assertEquals(sID, actual.getStudent().getId());
    assertEquals("Javaコース", actual.getStudentsCourse().get(0).getCourseName());

    verify(repository).insertStudent(student);
    verify(repository).findCourseIdByName("Javaコース");
    verify(repository).insertCourse(course);
  }

  @Test
  void studentDetailがnullの場合は例外が発生すること() {
    assertThrows(IllegalArgumentException.class, () -> {
      sut.register(null);
    });
    verify(repository, never()).insertStudent(any());
  }

  @Test
  void studentがnullの場合は例外が発生すること() {
    StudentDetail studentDetail = new StudentDetail(null, List.of(new Course()));

    assertThrows(IllegalArgumentException.class, () -> {
      sut.register(studentDetail);
    });
    verify(repository, never()).insertStudent(any());
  }

  @Test
  void 受講生の更新が動作すること() {
    int sID = 100;
    Student student = new Student();
    student.setId(sID);
    Course course = new Course();
    course.setCourseName("Javaコース");
    StudentDetail studentDetail = new StudentDetail(student, List.of(course));

    sut.updateStudent(studentDetail);

    verify(repository).updateStudent(student);
    assertEquals(sID, course.getStudentPk());
    verify(repository).updateCourse(course);
  }

  @Test
  void コースがnullの場合はコース更新が動作しないこと() {
    int sID = 100;
    Student student = new Student();
    student.setId(sID);
    StudentDetail studentDetail = new StudentDetail(student, null);

    sut.updateStudent(studentDetail);

    verify(repository).updateStudent(student);
    verify(repository, never()).updateCourse(any());
  }

  @Test
  void コースが空の場合はコース更新が動作しないこと() {
    int sID = 100;
    Student student = new Student();
    student.setId(sID);
    StudentDetail studentDetail = new StudentDetail(student, List.of());

    sut.updateStudent(studentDetail);

    verify(repository).updateStudent(student);
    verify(repository, never()).updateCourse(any());
  }

  @Test
  void コース情報が正しく初期化されること() {
    int sID = 100;
    int cID = 900;
    Student student = new Student();
    student.setId(sID);
    Course course = new Course();
    course.setCourseName("Javaコース");
    StudentDetail studentDetail = new StudentDetail(student, List.of(course));

    when(repository.findCourseIdByName("Javaコース")).thenReturn(cID);
    sut.register(studentDetail);

    assertEquals(sID, course.getStudentPk());
    assertEquals(cID, course.getCourseId());
    assertNotNull(course.getStartDate());
    assertNotNull(course.getEndDate());

    verify(repository).insertCourse(course);
  }

  @Test
  void 存在しないコース名の場合は例外が発生すること() {
    int sID = 1;
    Student student = new Student();
    student.setId(sID);

    Course course = new Course();
    course.setCourseName("存在しないコース");

    StudentDetail studentDetail = new StudentDetail(student, List.of(course));

    when(repository.findCourseIdByName("存在しないコース")).thenReturn(null);

    assertThrows(IllegalArgumentException.class, () -> {
      sut.register(studentDetail);
    });

    verify(repository).insertStudent(student);
    verify(repository).findCourseIdByName("存在しないコース");
    verify(repository, never()).insertCourse(any());
  }
}