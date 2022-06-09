package com.silenteight.hsbc.bridge.json.external.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class MultiValueFieldDeserializer extends JsonDeserializer<List<String>>  {

  @Override
  public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException,
      JsonProcessingException {
    String value = p.getText();

    if (JsonToken.START_ARRAY.asString().equals(value)) {
      return ctxt.readValue(
          p, ctxt.getTypeFactory()
              .constructType(new TypeReference<List<String>>() {}));
    }

    return Optional.ofNullable(value)
        .map(keyword -> Arrays.stream(keyword.split(";")).collect(Collectors.toList()))
        .orElse(List.of());
  }
}
