package utils.datageneration.governance;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class FeaturesLogic {
  List<Feature> features;
  int toFulfill;
}
