package utils.datageneration.simulations;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class CreateDataset {

  private String id;
  private String datasetName;
  private String description;
  private Query query;
}
