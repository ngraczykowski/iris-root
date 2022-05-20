/*
 * HSCB-Bridge-batch API
 * HSCB-Bridge-batch API
 *
 * OpenAPI spec version: 1.0.0
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.silenteight.hsbc.bridge.model.rest.input;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Gets or Sets ModelType
 */
@JsonAdapter(ModelType.Adapter.class)
public enum ModelType {
  MODEL("MODEL"),
  IS_PEP_PROCEDURAL("IS_PEP_PROCEDURAL"),
  IS_PEP_HISTORICAL("IS_PEP_HISTORICAL"),
  NAME_ALIASES("NAME_ALIASES"),
  HISTORICAL_DECISIONS_ALERTED_PARTY("HISTORICAL_DECISIONS_ALERTED_PARTY"),
  HISTORICAL_DECISIONS_WATCHLIST_PARTY("HISTORICAL_DECISIONS_WATCHLIST_PARTY"),
  HISTORICAL_DECISIONS_MATCH("HISTORICAL_DECISIONS_MATCH");

  private String value;

  ModelType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  public static ModelType fromValue(String text) {
    for (ModelType b : ModelType.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }

  public static class Adapter extends TypeAdapter<ModelType> {

    @Override
    public void write(final JsonWriter jsonWriter, final ModelType enumeration) throws IOException {
      jsonWriter.value(enumeration.getValue());
    }

    @Override
    public ModelType read(final JsonReader jsonReader) throws IOException {
      Object value = jsonReader.nextString();
      return ModelType.fromValue(String.valueOf(value));
    }
  }
}
