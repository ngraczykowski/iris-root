package utils.datageneration.governance;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class Policy {

  private String uuid;
  private String name;
  private String state;
  private List<PolicyStep> steps;
  private String creationPayload;
  private List<String> stepAdditionPayloads;
}
