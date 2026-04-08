package raisetech.Student.Management.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.Student.Management.data.CourseList;
import raisetech.Student.Management.data.Student;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  private Student student;
  private List<CourseList> studentsCourse;
}
