package com.silenteight.sep.usermanagement.keycloak.query.dto;

import lombok.*;

import com.silenteight.sep.usermanagement.api.configuration.dto.AuthConfigurationDto;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakAuthConfigurationDto extends AuthConfigurationDto {

  @NonNull
  private KeycloakDto keycloak;
}
