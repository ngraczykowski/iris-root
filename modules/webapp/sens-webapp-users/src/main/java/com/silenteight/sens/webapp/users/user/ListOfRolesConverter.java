package com.silenteight.sens.webapp.users.user;

import lombok.NonNull;

import com.silenteight.sens.webapp.common.support.jackson.JsonConversionHelper;
import com.silenteight.sens.webapp.common.support.jackson.JsonConversionHelper.FailedToGenerateJsonException;
import com.silenteight.sens.webapp.common.support.jackson.JsonConversionHelper.FailedToParseJsonException;
import com.silenteight.sens.webapp.kernel.security.authority.Role;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.util.List;
import javax.persistence.AttributeConverter;

import static java.util.stream.Collectors.toList;

class ListOfRolesConverter implements AttributeConverter<List<Role>, String> {

  @Override
  public String convertToDatabaseColumn(List<Role> list) {
    try {
      return JsonConversionHelper.INSTANCE.objectMapper().writeValueAsString(list);
    } catch (JsonProcessingException e) {
      throw new FailedToGenerateJsonException(list, e);
    }
  }

  @Override
  public List<Role> convertToEntityAttribute(@NonNull String json) {
    try {
      List<String> roleNames = parseRoleNames(json);
      List<String> validRoles = Role.getRoleNames();
      return convertToRoles(roleNames, validRoles);
    } catch (IOException e) {
      throw new FailedToParseJsonException(json, e);
    }
  }

  private static List<String> parseRoleNames(String json) throws IOException {
    return JsonConversionHelper
        .INSTANCE
        .objectMapper()
        .readValue(json, new TypeReference<List<String>>() {
        });
  }

  private static List<Role> convertToRoles(List<String> roleNames, List<String> validRoles) {
    return roleNames
        .stream()
        .filter(validRoles::contains)
        .map(Role::valueOf)
        .collect(toList());
  }
}
