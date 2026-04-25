package raisetech.Student.Management.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.Student.Management.data.Course;
import raisetech.Student.Management.data.Student;

@Schema(description = "受講生詳細")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {

  @Schema(description = "単一受講生詳細")
  @NotNull
  @Valid
  private Student student;

  @Schema(description = "全受講生コース情報一覧")
  @NotEmpty
  @Valid
  private List<Course> studentsCourse;
}
