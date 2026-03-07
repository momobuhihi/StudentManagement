package raisetech.Student.Management.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Student {

  private Integer id;
  private String studentName;
  private String furigana;
  private String nickname;
  private String phoneNumber;
  private String mailaddress;
  private String region;
  private int age;
  private String gender;
  private String remark;
  private boolean isDeleted;


}
