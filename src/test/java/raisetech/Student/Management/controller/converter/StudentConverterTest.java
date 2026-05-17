package raisetech.Student.Management.controller.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import raisetech.Student.Management.data.Course;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.domain.StudentDetail;

class StudentConverterTest {

  private final StudentConverter sut = new StudentConverter();

  @Test
  void 受講生一覧とコース一覧を受講生詳細一覧に変換できること() {
    Student student1 = new Student();
    student1.setId(1);
    student1.setStudentName("山田太郎");

    Student student2 = new Student();
    student2.setId(2);
    student2.setStudentName("田中花子");

    Course javaCourse = new Course();
    javaCourse.setStudentPk(1);
    javaCourse.setCourseName("Javaコース");

    Course awsCourse = new Course();
    awsCourse.setStudentPk(1);
    awsCourse.setCourseName("AWSコース");

    Course designCourse = new Course();
    designCourse.setStudentPk(2);
    designCourse.setCourseName("デザインコース");

    List<Student> students = List.of(student1, student2);
    List<Course> courses = List.of(javaCourse, awsCourse, designCourse);

    List<StudentDetail> actual = sut.convertStudentDetails(students, courses);

    assertEquals(2, actual.size());

    StudentDetail studentDetail1 = actual.get(0);
    assertEquals(student1, studentDetail1.getStudent());
    assertEquals(2, studentDetail1.getStudentsCourse().size());
    assertEquals(javaCourse, studentDetail1.getStudentsCourse().get(0));
    assertEquals(awsCourse, studentDetail1.getStudentsCourse().get(1));

    StudentDetail studentDetail2 = actual.get(1);
    assertEquals(student2, studentDetail2.getStudent());
    assertEquals(1, studentDetail2.getStudentsCourse().size());
    assertEquals(designCourse, studentDetail2.getStudentsCourse().get(0));
  }

  @Test
  void コース一覧が空の場合空のコース一覧を持つ受講生詳細が返ること() {

    Student student = new Student();
    student.setId(1);

    List<StudentDetail> actual =
        sut.convertStudentDetails(List.of(student), List.of());

    assertEquals(1, actual.size());
    assertEquals(0, actual.get(0).getStudentsCourse().size());
  }

  @Test
  void 紐づかないコースは含まれないこと() {

    Student student = new Student();
    student.setId(1);

    Course course = new Course();
    course.setStudentPk(999);

    List<StudentDetail> actual =
        sut.convertStudentDetails(
            List.of(student),
            List.of(course)
        );

    assertEquals(0,
        actual.get(0).getStudentsCourse().size());
  }
}