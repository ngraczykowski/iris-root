package utils.datageneration.governance;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class CreateSolvingModel {
  String id;
  String policy;
}
