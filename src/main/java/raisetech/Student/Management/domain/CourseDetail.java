package raisetech.Student.Management.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import raisetech.Student.Management.data.Course;

@Getter
@Setter
public class CourseDetail {

  private String courseId;
  private String courseName;
  private List<Course> enrollments;
  ;

}
