package utils.datageneration.governance;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class SolvingModel {

  private String uuid;
  private String policyUuid;
  private String activationUuid;
  private String creationPayload;
  private String activationPayload;
}
