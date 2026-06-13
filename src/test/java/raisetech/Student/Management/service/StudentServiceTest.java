package raisetech.Student.Management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.Student.Management.data.Course;
import raisetech.Student.Management.data.Status;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.domain.StatusDetail;
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

    StudentDetail actual = sut.register(studentDetail);

    assertEquals(sID, actual.getStudent().getId());
    assertEquals("Javaコース", actual.getStudentsCourse().get(0).getCourseName());

    verify(repository).insertStudent(student);
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

    Student student = new Student();
    student.setId(sID);

    Course course = new Course();
    course.setCourseName("Javaコース");

    StudentDetail studentDetail = new StudentDetail(student, List.of(course));

    sut.register(studentDetail);

    assertEquals(sID, course.getStudentPk());
    assertEquals("Javaコース", course.getCourseName());
    assertNotNull(course.getStartDate());
    assertNotNull(course.getEndDate());

    verify(repository).insertCourse(course);
  }

  @Test
  void 複数コースを登録できること() {
    Student student = new Student();
    student.setId(1);

    Course java = new Course();
    java.setCourseName("Javaコース");

    Course aws = new Course();
    aws.setCourseName("AWSコース");

    StudentDetail studentDetail =
        new StudentDetail(student, List.of(java, aws));

    sut.register(studentDetail);

    verify(repository, times(2)).insertCourse(any(Course.class));
  }

  @Test
  void 登録時に仮申込が登録されること() {

    Student student = new Student();
    student.setId(1);

    Course course = new Course();
    course.setId(10);
    course.setCourseName("Javaコース");

    StudentDetail studentDetail =
        new StudentDetail(student, List.of(course));

    sut.register(studentDetail);

    ArgumentCaptor<Status> captor =
        ArgumentCaptor.forClass(Status.class);

    verify(repository).insertStatus(captor.capture());

    Status status = captor.getValue();

    assertEquals(10, status.getStudentCourseId());
    assertEquals("仮申込", status.getStatus());
  }

  @Test
  void 仮申込から本申込に更新できること() {
    Status request = new Status();
    request.setStudentCourseId(33);
    request.setStatus("本申込");

    Status currentStatus = new Status();
    currentStatus.setStudentCourseId(33);
    currentStatus.setStatus("仮申込");

    StatusDetail statusDetail = new StatusDetail();
    statusDetail.setStudentId(4);
    statusDetail.setStudentName("山田太郎");
    statusDetail.setCourseName("AWSコース");
    statusDetail.setStatus("本申込");

    when(repository.searchStatus(33)).thenReturn(currentStatus);
    when(repository.searchStatusDetail(33)).thenReturn(statusDetail);

    StatusDetail result = sut.updateStatus(request);

    assertEquals(4, result.getStudentId());
    assertEquals("山田太郎", result.getStudentName());
    assertEquals("AWSコース", result.getCourseName());
    assertEquals("本申込", result.getStatus());

    verify(repository).updateStatus(request);
    verify(repository).searchStatus(33);
    verify(repository).searchStatusDetail(33);
  }

  @Test
  void 本申込から受講中に更新できること() {
    Status request = new Status();
    request.setStudentCourseId(33);
    request.setStatus("受講中");

    Status currentStatus = new Status();
    currentStatus.setStudentCourseId(33);
    currentStatus.setStatus("本申込");

    StatusDetail statusDetail = new StatusDetail();
    statusDetail.setStudentId(4);
    statusDetail.setStudentName("山田太郎");
    statusDetail.setCourseName("AWSコース");
    statusDetail.setStatus("受講中");

    when(repository.searchStatus(33)).thenReturn(currentStatus);
    when(repository.searchStatusDetail(33)).thenReturn(statusDetail);

    StatusDetail result = sut.updateStatus(request);

    assertEquals("受講中", result.getStatus());

    verify(repository).updateStatus(request);
  }

  @Test
  void 受講中から受講終了に更新できること() {
    Status request = new Status();
    request.setStudentCourseId(33);
    request.setStatus("受講終了");

    Status currentStatus = new Status();
    currentStatus.setStudentCourseId(33);
    currentStatus.setStatus("受講中");

    StatusDetail statusDetail = new StatusDetail();
    statusDetail.setStudentId(4);
    statusDetail.setStudentName("山田太郎");
    statusDetail.setCourseName("AWSコース");
    statusDetail.setStatus("受講終了");

    when(repository.searchStatus(33)).thenReturn(currentStatus);
    when(repository.searchStatusDetail(33)).thenReturn(statusDetail);

    StatusDetail result = sut.updateStatus(request);

    assertEquals("受講終了", result.getStatus());

    verify(repository).updateStatus(request);
  }

  @Test
  void 本申込から仮申込には更新できないこと() {
    Status request = new Status();
    request.setStudentCourseId(33);
    request.setStatus("仮申込");

    Status currentStatus = new Status();
    currentStatus.setStudentCourseId(33);
    currentStatus.setStatus("本申込");

    when(repository.searchStatus(33)).thenReturn(currentStatus);

    assertThrows(IllegalArgumentException.class, () -> {
      sut.updateStatus(request);
    });

    verify(repository, never()).updateStatus(any(Status.class));
    verify(repository, never()).searchStatusDetail(anyInt());
  }

  @Test
  void 受講終了から受講中には更新できないこと() {
    Status request = new Status();
    request.setStudentCourseId(33);
    request.setStatus("受講中");

    Status currentStatus = new Status();
    currentStatus.setStudentCourseId(33);
    currentStatus.setStatus("受講終了");

    when(repository.searchStatus(33)).thenReturn(currentStatus);

    assertThrows(IllegalArgumentException.class, () -> {
      sut.updateStatus(request);
    });

    verify(repository, never()).updateStatus(any(Status.class));
    verify(repository, never()).searchStatusDetail(anyInt());
  }

  @Test
  void 存在しない受講生コース情報IDの場合は例外が発生すること() {
    Status request = new Status();
    request.setStudentCourseId(999);
    request.setStatus("本申込");

    when(repository.searchStatus(999)).thenReturn(null);

    assertThrows(IllegalArgumentException.class, () -> {
      sut.updateStatus(request);
    });

    verify(repository, never()).updateStatus(any(Status.class));
  }

  @Test
  void 不正な申込状況の場合は例外が発生すること() {
    Status request = new Status();
    request.setStudentCourseId(33);
    request.setStatus("不明な状態");

    Status currentStatus = new Status();
    currentStatus.setStudentCourseId(33);
    currentStatus.setStatus("仮申込");

    when(repository.searchStatus(33)).thenReturn(currentStatus);

    assertThrows(IllegalArgumentException.class, () -> {
      sut.updateStatus(request);
    });

    verify(repository, never()).updateStatus(any(Status.class));
  }

  @Test
  void 仮申込から受講終了には更新できないこと() {
    Status request = new Status();
    request.setStudentCourseId(33);
    request.setStatus("受講終了");

    Status currentStatus = new Status();
    currentStatus.setStudentCourseId(33);
    currentStatus.setStatus("仮申込");

    when(repository.searchStatus(33)).thenReturn(currentStatus);

    assertThrows(IllegalArgumentException.class, () -> {
      sut.updateStatus(request);
    });

    verify(repository, never()).updateStatus(any(Status.class));
    verify(repository, never()).searchStatusDetail(anyInt());
  }
}