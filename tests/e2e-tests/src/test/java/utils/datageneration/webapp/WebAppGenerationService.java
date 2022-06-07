package utils.datageneration.webapp;

import lombok.experimental.UtilityClass;

import java.util.UUID;

import static java.util.Collections.emptySet;

@UtilityClass
public class WebAppGenerationService {

  public static User createUser(String label) {
    return User.builder()
        .userName(UUID.randomUUID().toString().substring(0, 30))
        .displayName(label + " - " + UUID.randomUUID())
        .password(UUID.randomUUID().toString())
        .countryGroups(emptySet())
        .roles(emptySet())
        .build();
  }
}
