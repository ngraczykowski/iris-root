package utils.datageneration.governance;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class CreateFeatureLogic {
  List<FeaturesLogic> featuresLogic;
}
