package utils.datageneration.webapp;

import lombok.experimental.UtilityClass;

import java.util.UUID;

import static java.util.Collections.emptySet;
@UtilityClass
public class WebAppGenerationService {

  public static CreateUser createUser() {
    return CreateUser.builder()
        .userName(UUID.randomUUID().toString().substring(0, 30))
        .displayName(UUID.randomUUID().toString())
        .password(UUID.randomUUID().toString())
        .countryGroups(emptySet())
        .roles(emptySet())
        .build();
  }
}
