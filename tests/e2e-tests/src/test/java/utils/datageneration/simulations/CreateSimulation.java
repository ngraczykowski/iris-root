package utils.datageneration.simulations;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class CreateSimulation {

  private String simulationName;
  private String description;
  private List<String> datasets;
  private String model;
  private String id;
}
