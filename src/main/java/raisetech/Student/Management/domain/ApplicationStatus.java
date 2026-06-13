package raisetech.Student.Management.domain;

import lombok.Getter;

@Getter
public enum ApplicationStatus {

  TEMPORARY("仮申込"),
  REGISTERED("本申込"),
  TAKING("受講中"),
  COMPLETED("受講終了");

  private final String label;

  ApplicationStatus(String label) {
    this.label = label;
  }

  public boolean canTransitionTo(ApplicationStatus next) {
    return switch (this) {
      case TEMPORARY -> next == REGISTERED;
      case REGISTERED -> next == TAKING;
      case TAKING -> next == COMPLETED;
      case COMPLETED -> false;
    };
  }

  public static ApplicationStatus fromLabel(String label) {

    for (ApplicationStatus status : values()) {
      if (status.label.equals(label)) {
        return status;
      }
    }
    throw new IllegalArgumentException(
        "不正な申込状況です: " + label);
  }
}
