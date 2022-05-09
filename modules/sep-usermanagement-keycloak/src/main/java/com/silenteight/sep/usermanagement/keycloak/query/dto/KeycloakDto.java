package com.silenteight.sep.usermanagement.keycloak.query.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakDto {

  @NonNull
  private String url;
  @NonNull
  private String realm;
  @NonNull
  private String clientId;
}
