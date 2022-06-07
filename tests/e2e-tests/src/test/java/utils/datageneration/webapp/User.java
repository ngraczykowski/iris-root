package utils.datageneration.webapp;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder(toBuilder = true)
public class User {

  private String userName;

  private String password;

  private String displayName;

  private Set<String> roles;

  private Set<String> countryGroups;

}
