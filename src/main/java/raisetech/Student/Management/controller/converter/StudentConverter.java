package raisetech.Student.Management.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.Student.Management.data.Course;
import raisetech.Student.Management.data.Student;
import raisetech.Student.Management.domain.StudentDetail;

@Component
public class StudentConverter {

  public List<StudentDetail> convertStudentDetails(List<Student> students, List<Course> courses) {
    List<StudentDetail> studentDetails = new ArrayList<>();
    students.forEach(student -> {
      StudentDetail studentDetail = new StudentDetail();
      studentDetail.setStudent(student);

      List<Course> convertCourses = courses.stream()
          .filter(course -> student.getStudentId().equals(course.getStudentId()))
          .collect(Collectors.toList());
      studentDetail.setStudentsCourse((convertCourses));
      studentDetails.add(studentDetail);
    });
    return studentDetails;
  }
}
