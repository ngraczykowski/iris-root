package utils.datageneration.governance;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class ActivateSolvingModel {
  String id;
  String modelName;
  String comment;
}
