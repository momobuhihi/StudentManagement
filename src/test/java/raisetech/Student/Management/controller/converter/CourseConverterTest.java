package raisetech.Student.Management.controller.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import raisetech.Student.Management.data.Course;
import raisetech.Student.Management.domain.CourseDetail;

class CourseConverterTest {

  private final CourseConverter sut = new CourseConverter();

  @Test
  void コース一覧をコースIDごとにグループ化してコース詳細一覧に変換できること() {
    Course java1 = new Course();
    java1.setCourseId(1);
    java1.setCourseName("Javaコース");
    java1.setStudentPk(1);

    Course java2 = new Course();
    java2.setCourseId(1);
    java2.setCourseName("Javaコース");
    java2.setStudentPk(2);

    Course aws = new Course();
    aws.setCourseId(2);
    aws.setCourseName("AWSコース");
    aws.setStudentPk(3);

    List<Course> enrollments = List.of(java1, java2, aws);

    List<CourseDetail> actual = sut.convertCourseDetails(enrollments);

    assertEquals(2, actual.size());

    CourseDetail javaDetail = actual.stream()
        .filter(detail -> detail.getCourseId().equals(1))
        .findFirst()
        .orElseThrow();

    assertEquals(1, javaDetail.getCourseId());
    assertEquals("Javaコース", javaDetail.getCourseName());
    assertEquals(2, javaDetail.getEnrollments().size());
    assertEquals(java1, javaDetail.getEnrollments().get(0));
    assertEquals(java2, javaDetail.getEnrollments().get(1));

    CourseDetail awsDetail = actual.stream()
        .filter(detail -> detail.getCourseId().equals(2))
        .findFirst()
        .orElseThrow();

    assertEquals(2, awsDetail.getCourseId());
    assertEquals("AWSコース", awsDetail.getCourseName());
    assertEquals(1, awsDetail.getEnrollments().size());
    assertEquals(aws, awsDetail.getEnrollments().get(0));
  }

  @Test
  void 空の受講情報リストを渡した場合空のリストが返ること() {

    List<CourseDetail> actual = sut.convertCourseDetails(List.of());

    assertEquals(0, actual.size());
  }
}