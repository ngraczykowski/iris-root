package com.silenteight.sep.usermanagement.keycloak.query;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static java.util.Collections.emptyList;

@Data
@ConfigurationProperties(prefix = "keycloak.users-list-filter")
@ConstructorBinding
@Validated
class UsersListFilterProperties {

  private final AttributeFilterProperties attributeFilter;

  private final RealmRoleFilterProperties realmRoleFilter;

  List<Attribute> getAttributeFilters() {
    if (attributeFilter == null)
      return emptyList();

    return attributeFilter.getAttributeFilters();
  }

  List<String> getRealmRoleFilterRoles() {
    if (attributeFilter == null)
      return emptyList();

    return realmRoleFilter.getRoles();
  }
}
