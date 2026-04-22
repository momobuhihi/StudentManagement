package raisetech.Student.Management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コース情報")
@Getter
@Setter
public class CourseList {

  private Integer courseId;
  private Integer studentPk;

  @NotBlank
  private String courseName;
  private LocalDate startDate;
  private LocalDate endDate;
}
