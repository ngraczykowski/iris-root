package utils.datageneration.simulations;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class Simulation {

  private String name;
  private String description;
  private Dataset dataset;
  private String solvingModel;
  private String uuid;
  private String creationPayload;
}
