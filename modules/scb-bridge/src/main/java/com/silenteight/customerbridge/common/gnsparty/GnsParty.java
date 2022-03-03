package com.silenteight.customerbridge.common.gnsparty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import static com.silenteight.customerbridge.common.gnsparty.GnsPartyCommonFields.ALTERNATE_NAMES;
import static com.silenteight.customerbridge.common.gnsparty.GnsPartyCommonFields.NAME;
import static com.silenteight.customerbridge.common.gnsparty.GnsPartyCommonFields.NATIONAL_IDS;
import static com.silenteight.customerbridge.common.gnsparty.GnsPartyCommonFields.PASSPORT_NUMBERS;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

/**
 * Represents customer of SCB.
 */
@Data
@EqualsAndHashCode(of = { "sourceSystemIdentifier", "customerIdentificationNo" })
public class GnsParty {

  private String sourceSystemIdentifier;
  private String customerIdentificationNo;

  private Map<String, Object> fields;

  private GnsParty() {
    fields = new HashMap<>();
  }

  static GnsParty empty() {
    return new GnsParty();
  }

  public static GnsParty create(
      @NonNull String sourceSystemIdentifier,
      @NonNull String customerIdentificationNo) {

    GnsParty gnsParty = new GnsParty();
    gnsParty.sourceSystemIdentifier = sourceSystemIdentifier;
    gnsParty.customerIdentificationNo = customerIdentificationNo;
    gnsParty.fields.put("sourceSystemIdentifier", sourceSystemIdentifier);
    gnsParty.fields.put("customerIdentificationNo", customerIdentificationNo);
    return gnsParty;
  }

  public boolean isEmpty() {
    return isNull(sourceSystemIdentifier) || isNull(customerIdentificationNo);
  }

  public GnsParty mapString(String fieldName, Consumer<String> consumer) {
    if (fields.containsKey(fieldName)) {
      String value = (String) fields.get(fieldName);
      if (StringUtils.isNotEmpty(value))
        consumer.accept(value);
    }
    return this;
  }

  @SuppressWarnings("unchecked")
  public GnsParty mapCollection(String fieldName, Consumer<Collection<String>> consumer) {
    if (fields.containsKey(fieldName)) {
      Collection<String> collection = (Collection<String>) fields.get(fieldName);
      if (CollectionUtils.isNotEmpty(collection))
        consumer.accept(collection);
    }
    return this;
  }

  void copyField(String name, String target) {
    ofNullable(getValue(name)).ifPresent(v -> this.add(target, v));
  }

  void add(String fieldName, Object value) {
    fields.put(fieldName, value);
  }

  Object getValue(String fieldName) {
    return fields.get(fieldName);
  }

  public void createList(String listName, List<String> fields) {
    List<Object> listValues = fields
        .stream()
        .map(this::getValue)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    this.add(listName, listValues);
  }

  void createMap(String mapName, Map<String, String> fieldsKeyValue) {
    Map<String, Object> map = new HashMap<>();
    fieldsKeyValue.forEach((key, value) -> addToMapIfKeyAndValueNotNull(map, key, value));
    this.add(mapName, map);
  }

  private void addToMapIfKeyAndValueNotNull(Map<String, Object> map, String key, String value) {
    Object fieldKey = getValue(key);
    Object fieldValue = getValue(value);
    if (fieldKey == null || fieldValue == null)
      return;

    map.put(String.valueOf(fieldKey), fieldValue);
  }

  void createNamedMap(String mapName, Map<String, String> fieldsKeyValue) {
    Map<String, Object> map = new HashMap<>();
    fieldsKeyValue.forEach((key, value) -> addToMapIfValueNotNull(map, key, value));
    this.add(mapName, map);
  }

  private void addToMapIfValueNotNull(Map<String, Object> map, String key, String value) {
    Object fieldValue = getValue(value);
    if (fieldValue == null)
      return;

    map.put(String.valueOf(key), fieldValue);
  }

  public Set<String> getAlternateNames() {
    return getCollectionAsSet(ALTERNATE_NAMES);
  }

  @Nonnull
  public Set<String> getCollectionAsSet(String fieldName) {
    Collection<String> values = getCollectionValue(fieldName);
    return CollectionUtils.isEmpty(values) ? emptySet() : new TreeSet<>(values);
  }

  @SuppressWarnings("unchecked")
  private Collection<String> getCollectionValue(String fieldName) {
    return (Collection<String>) fields.get(fieldName);
  }

  public Optional<String> getName() {
    return getStringField(NAME);
  }

  Optional<String> getStringField(String name) {
    return ofNullable(fields.get(name))
        .map(v -> (String) v)
        .filter(StringUtils::isNotEmpty);
  }

  public void addPassportNumbers(List<String> values) {
    add(PASSPORT_NUMBERS, values);
  }

  public List<String> getPassportNumbers() {
    return getCollectionAsList(PASSPORT_NUMBERS);
  }

  @Nonnull
  public List<String> getCollectionAsList(String fieldName) {
    Collection<String> values = getCollectionValue(fieldName);
    return CollectionUtils.isEmpty(values) ? emptyList() : new ArrayList<>(values);
  }

  public void addNationalIds(List<String> values) {
    add(NATIONAL_IDS, values);
  }

  public List<String> getNationalIds() {
    return getCollectionAsList(NATIONAL_IDS);
  }
}
