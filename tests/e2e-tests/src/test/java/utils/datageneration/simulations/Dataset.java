package utils.datageneration.simulations;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class Dataset {

  private String uuid;
  private String name;
  private String from;
  private String to;
  private String creationPayload;
}
