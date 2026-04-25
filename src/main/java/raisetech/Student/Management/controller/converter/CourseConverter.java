package raisetech.Student.Management.controller.converter;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.Student.Management.data.Course;
import raisetech.Student.Management.domain.CourseDetail;

@Component
public class CourseConverter {

  public List<CourseDetail> convertCourseDetails(List<Course> enrollments) {

    return enrollments.stream()
        .collect(Collectors.groupingBy(Course::getCourseId))
        .entrySet().stream()
        .map(entry -> new CourseDetail(
            entry.getKey(),
            entry.getValue().get(0).getCourseName(),
            entry.getValue()
        ))
        .toList();
  }
}
