package raisetech.Student.Management.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.Student.Management.data.CourseList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseDetail {

  private Integer courseId;
  private String courseName;
  private List<CourseList> enrollments;

}
