package raisetech.Student.Management;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Course {

  private String courseId;
  private String courseName;
  private LocalDate startDate;
  private LocalDate endDate;
}
