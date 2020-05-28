package com.silenteight.sens.webapp.backend.support;

import lombok.NonNull;

import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// We should improve this class and markAsUsed filter provider or MixIn
public class ObjectNodeMapper {

  @NonNull
  private final List<String> fieldNamesToRemove;

  public ObjectNodeMapper(String... ignoreFields) {
    this.fieldNamesToRemove = Arrays.asList(ignoreFields);
  }

  public ObjectNode map(Object object) {
    return Optional.ofNullable(object)
                   .map(JsonConversionHelper.INSTANCE::serializeObject)
                   .map(this::getNodeWithoutFilteredFields)
                   .orElse(null);
  }

  private ObjectNode getNodeWithoutFilteredFields(ObjectNode n) {
    n.remove(fieldNamesToRemove);
    return n;
  }
}
