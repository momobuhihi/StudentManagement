package raisetech.Student.Management.controller.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import raisetech.Student.Management.data.Course;
import raisetech.Student.Management.domain.CourseDetail;

@Component
public class CourseConverter {

  public List<CourseDetail> convertCourseDetails(List<Course> enrollments) {

    Map<String, List<Course>> grouped =
        enrollments.stream()
            .collect(Collectors.groupingBy(Course::getCourseId));

    List<CourseDetail> result = new ArrayList<>();

    grouped.forEach((courseId, list) -> {

      CourseDetail detail = new CourseDetail();
      detail.setCourseId(courseId);
      detail.setCourseName(list.get(0).getCourseName());
      detail.setEnrollments(list);

      result.add(detail);
    });

    return result;
  }
}
