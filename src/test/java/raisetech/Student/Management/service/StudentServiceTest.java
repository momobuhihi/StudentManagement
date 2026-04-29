package raisetech.Student.Management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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
import raisetech.Student.Management.controller.converter.StudentConverter;
import raisetech.Student.Management.data.Course;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.domain.StudentDetail;
import raisetech.Student.Management.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

  @Mock
  private StudentRepository repository;

  @Mock
  private StudentConverter converter;

  private StudentService sut;

  @BeforeEach
  void before() {
    repository = mock(StudentRepository.class);
    sut = new StudentService(repository);
  }

  @Test
  void 受講生詳細の全件検索が動作すること() {
    List<Student> expected = new ArrayList<>();
    when(repository.search()).thenReturn(expected);

    sut.searchStudentList();
  }

  @Test
  void 受講生の単一検索が動作すること() {
    int id = 1;

    Student student = new Student();
    List<Course> courses = new ArrayList<>();
    when(repository.searchStudent(id)).thenReturn(student);
    when(repository.searchStudentCourses(id)).thenReturn(courses);

    StudentDetail actual = sut.searchStudent(id);

    assertEquals(student, actual.getStudent());
    assertEquals(courses, actual.getStudentsCourse());

    verify(repository).searchStudent(id);
    verify(repository).searchStudentCourses(id);
  }

  @Test
  void 受講生の登録が動作すること() {
    Student student = new Student();
    student.setId(1);
    Course course = new Course();
    course.setCourseName("Javaコース");
    StudentDetail studentDetail = new StudentDetail(student, List.of(course));

    when(repository.findCourseIdByName("Javaコース")).thenReturn(1);
    StudentDetail actual = sut.register(studentDetail);
    assertEquals(studentDetail, actual);

    verify(repository).insertStudent(student);
    verify(repository).findCourseIdByName("Javaコース");
    verify(repository).insertCourse(course);
  }

  @Test
  void 受講生の更新が動作すること() {
    Student student = new Student();
    student.setId(1);
    Course course = new Course();
    course.setCourseName("Javaコース");
    StudentDetail studentDetail = new StudentDetail(student, List.of(course));

    sut.updateStudent(studentDetail);

    verify(repository).updateStudent(student);
    assertEquals(1, course.getStudentPk());
    verify(repository).updateCourse(course);
  }

  @Test
  void コースがnullの場合はコース更新が動作しないこと() {
    Student student = new Student();
    student.setId(1);
    StudentDetail studentDetail = new StudentDetail(student, null);

    sut.updateStudent(studentDetail);

    verify(repository).updateStudent(student);
    verify(repository, never()).updateCourse(any());
  }

  @Test
  void コースが空の場合はコース更新が動作しないこと() {
    Student student = new Student();
    student.setId(1);
    StudentDetail studentDetail = new StudentDetail(student, List.of());

    sut.updateStudent(studentDetail);

    verify(repository).updateStudent(student);
    verify(repository, never()).updateCourse(any());
  }

  @Test
  void コース情報が正しく初期化されること() {
    Student student = new Student();
    student.setId(1);
    Course course = new Course();
    course.setCourseName("Javaコース");
    StudentDetail studentDetail = new StudentDetail(student, List.of(course));

    when(repository.findCourseIdByName("Javaコース")).thenReturn(100);
    sut.register(studentDetail);

    assertEquals(1, course.getStudentPk());
    assertEquals(100, course.getCourseId());
    assertNotNull(course.getStartDate());
    assertNotNull(course.getEndDate());

    verify(repository).insertCourse(course);
  }
}