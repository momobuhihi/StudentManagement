package raisetech.Student.Management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生コース情報")
@Getter
@Setter
public class Course {

  @Schema(description = "コースID", example = "1")
  private Integer courseId;
  @Schema(description = "受講生ID", example = "1")
  private Integer studentPk;

  @Schema(description = "コース名", example = "Javaコース")
  @NotBlank
  private String courseName;
  @Schema(description = "受講開始日（yyyy-MM-dd形式）", example = "2026-01-01")
  private LocalDate startDate;
  @Schema(description = "修了日（yyyy-MM-dd形式）", example = "2026-04-30")
  private LocalDate endDate;
}
