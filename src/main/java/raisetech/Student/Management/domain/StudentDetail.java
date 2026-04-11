package raisetech.Student.Management.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

  @NotNull
  @Valid
  private Student student;
  @NotEmpty
  @Valid
  private List<CourseList> studentsCourse;
}
