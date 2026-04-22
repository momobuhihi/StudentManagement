package raisetech.Student.Management.data;

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

  private Integer id;

  @NotBlank
  @Size(max = 50)
  private String studentName;

  @NotBlank
  @Size(max = 50)
  private String furigana;

  @NotBlank
  @Size(max = 50)
  private String nickname;

  @Pattern(regexp = "^(0\\d{9,10}|0\\d{1,4}-\\d{1,4}-\\d{4})$")
  private String phoneNumber;

  @NotBlank
  @Email
  private String mailaddress;

  @NotBlank
  private String region;

  @Min(0)
  @Max(150)
  private int age;

  @NotBlank
  private String gender;

  @Size(max = 255)
  private String remark;

  private boolean isDeleted;


}
