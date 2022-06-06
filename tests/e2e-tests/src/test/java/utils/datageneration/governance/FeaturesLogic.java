package utils.datageneration.governance;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@Builder(toBuilder = true)
public class FeaturesLogic {
  ArrayList<Feature> features;
  int toFulfill;
}
