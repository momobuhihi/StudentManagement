package raisetech.Student.Management.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生")
@Getter
@Setter
public class Student {

  @Schema(description = "受講生ID", example = "1")
  private Integer id;

  @Schema(description = "名前", example = "田中太郎")
  @NotBlank
  @Size(max = 50)
  private String studentName;

  @Schema(description = "ふりがな", example = "たなかたろう")
  @NotBlank
  @Size(max = 50)
  private String furigana;

  @Schema(description = "ニックネーム", example = "たろ")
  @NotBlank
  @Size(max = 50)
  private String nickname;

  @Schema(description = "電話番号", example = "090-1234-5678")
  @Pattern(regexp = "^(0\\d{9,10}|0\\d{1,4}-\\d{1,4}-\\d{4})$")
  private String phoneNumber;

  @Schema(description = "メールアドレス", example = "zyukousei@example.com")
  @NotBlank
  @Email
  private String mailaddress;

  @Schema(description = "地域", example = "大阪府")
  @NotBlank
  private String region;

  @Schema(description = "年齢", example = "30")
  @Min(0)
  @Max(150)
  private int age;

  @Schema(description = "性別", example = "男性")
  @NotBlank
  private String gender;

  @Schema(description = "備考", example = "特になし")
  @Size(max = 255)
  private String remark;

  @JsonProperty("deleted")
  @Schema(description = "削除フラグ（true:削除済み, false:有効）", example = "false", accessMode = Schema.AccessMode.READ_ONLY)
  private boolean isDeleted;


}
