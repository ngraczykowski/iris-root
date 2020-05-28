package com.silenteight.sep.base.common.support.hibernate;

import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.FailedToGenerateJsonException;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.FailedToParseJsonException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.persistence.AttributeConverter;

import static java.util.Collections.emptyList;

public class StringListConverter
    implements AttributeConverter<List<String>, String> {

  private static final ObjectMapper OBJECT_MAPPER = JsonConversionHelper.INSTANCE.objectMapper();
  private static final CollectionType LIST_TYPE =
      OBJECT_MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, String.class);

  @Override
  @Nullable
  public String convertToDatabaseColumn(@Nullable List<String> list) {
    if (list == null)
      return null;

    try {
      return JsonConversionHelper.INSTANCE.objectMapper().writeValueAsString(list);
    } catch (JsonProcessingException e) {
      throw new FailedToGenerateJsonException(list, e);
    }
  }

  @Override
  @Nullable
  public List<String> convertToEntityAttribute(@Nullable String json) {
    if (json == null)
      return null;

    // XXX(ahaczewski): Really? Maybe empty json should also be treated as null instead of empty
    //  list, because empty list is not empty json ('[]')?
    if (json.isEmpty())
      return emptyList();

    try {
      return OBJECT_MAPPER.readValue(json, LIST_TYPE);
    } catch (IOException e) {
      // FIXME(ahaczewski): Don't use not own exceptions!
      throw new FailedToParseJsonException(json, e);
    }
  }
}
