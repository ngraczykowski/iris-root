package com.silenteight.sep.usermanagement.keycloak.query;

import lombok.Data;

import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
@ConstructorBinding
class RealmRoleFilterProperties {

  private final List<String> roles;
}
