package utils.datageneration.warehouse;

import lombok.experimental.UtilityClass;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

@UtilityClass
public class WarehouseGenerationService {

  public static CreateCountryGroup createCountryGroup(UUID id) {
    return CreateCountryGroup.builder().id(id).name(RandomStringUtils.randomAlphabetic(10)).build();
  }
}
