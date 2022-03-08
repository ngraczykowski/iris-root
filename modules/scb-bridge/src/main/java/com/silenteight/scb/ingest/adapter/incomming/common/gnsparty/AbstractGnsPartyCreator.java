package com.silenteight.scb.ingest.adapter.incomming.common.gnsparty;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
abstract class AbstractGnsPartyCreator implements GnsPartyCreator {

  private final List<String> fields;
  private final Map<String, String> fieldNameMap;

  @Override
  public boolean supports(String[] values) {
    return values.length == getFieldsCount();
  }

  private int getFieldsCount() {
    return fields.size();
  }

  @Override
  public GnsParty create(String[] values) {
    return createGnsParty(values);
  }

  private GnsParty createGnsParty(String[] values) {
    String sourceSystemIdentifier = values[0].trim();
    String customerIdentificationNo = values[1].trim();
    GnsParty result = GnsParty.create(sourceSystemIdentifier, customerIdentificationNo);

    for (int i = 2; i < values.length; i++) {
      String value = values[i].trim();
      addNotEmpty(result, i, value);
    }

    return createAdditionalFields(result);
  }

  private void addNotEmpty(GnsParty result, int i, String value) {
    if (value.isEmpty())
      return;

    result.add(getMappedField(fields.get(i)), value);
  }

  private String getMappedField(String fieldName) {
    return this.fieldNameMap.getOrDefault(fieldName, fieldName);
  }

  protected abstract GnsParty createAdditionalFields(GnsParty result);
}
