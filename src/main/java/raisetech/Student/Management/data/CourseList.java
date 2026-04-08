package raisetech.Student.Management.data;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseList {

  private Integer courseId;
  private Integer studentPk;
  private String courseName;
  private LocalDate startDate;
  private LocalDate endDate;
}
