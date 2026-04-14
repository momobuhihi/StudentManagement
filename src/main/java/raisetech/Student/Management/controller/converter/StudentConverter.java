package raisetech.Student.Management.controller.converter;

import java.util.List;
import org.springframework.stereotype.Component;
import raisetech.Student.Management.data.CourseList;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.domain.StudentDetail;

@Component
public class StudentConverter {

  /**
   * 受講生に紐づく受講生コース情報をマッピングする。受講生コース情報は受講生に対して複数存在するのでループを回して受講生詳細情報を組み立てる。
   *
   * @param students 受講生一覧
   * @param courses  受講生コース情報のリスト
   * @return　受講生詳細情報のリスト
   */
  public List<StudentDetail> convertStudentDetails(List<Student> students,
      List<CourseList> courses) {
    return students.stream()
        .map(student -> {
          List<CourseList> convertCourses = courses.stream()
              .filter(course -> student.getId().equals(course.getStudentPk()))
              .toList();

          return new StudentDetail(student, convertCourses);
        })
        .toList();
  }
}
