package raisetech.Student.Management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "申し込み状況")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Status {

  @Schema(description = "申込状況ID", example = "1")
  private Integer id;
  @Schema(description = "受講生コース情報ID", example = "1")
  private Integer studentCourseId;
  @Schema(description = "申込状況", example = "仮申込")
  private String status;
}
