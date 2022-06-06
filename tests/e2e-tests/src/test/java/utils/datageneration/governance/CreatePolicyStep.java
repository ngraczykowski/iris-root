package utils.datageneration.governance;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class CreatePolicyStep {

  String id;
  String name;
  String description;
  String solution;
  String type;
}
