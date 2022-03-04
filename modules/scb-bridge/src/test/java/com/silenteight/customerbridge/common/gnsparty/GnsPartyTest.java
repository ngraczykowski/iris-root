package com.silenteight.customerbridge.common.gnsparty;

import com.silenteight.commons.collections.MapBuilder;

import org.junit.Test;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.silenteight.customerbridge.common.gnsparty.GnsPartyCommonFields.ALTERNATE_NAMES;
import static org.assertj.core.api.Assertions.*;

public class GnsPartyTest {

  private static final String CUSTOMER_IDENTIFICATION_NO = "CUSTOMER_IDENTIFICATION_NO";
  private static final String SOURCE_SYSTEM_IDENTIFIER = "SOURCE_SYSTEM_IDENTIFIER";
  private static final Object VALUE = "value";
  private static final String FIELD_NAME = "test";
  private static final String VALUE_FIELD_NAME_1 = "field1";
  private static final Object VALUE_1 = "value1";
  private static final String VALUE_FIELD_NAME_2 = "field2";
  private static final Object VALUE_2 = "value2";
  private static final String KEY_FIELD_NAME_1 = "key1";
  private static final String KEY_FIELD_NAME_2 = "key2";
  private static final String KEY_VALUE_1 = "value1";
  private static final String KEY_VALUE_2 = "value2";
  private static final String CREATED_FIELD_NAME = "NEW_FIELD";
  private static final String NULL_FIELD_NAME = "NULL_FIELD";

  @Test
  public void createEmptyGnsParty_resultContainsNoValues() {
    //when
    GnsParty result = GnsParty.empty();

    //then
    assertThat(result.getSourceSystemIdentifier()).isNull();
    assertThat(result.getCustomerIdentificationNo()).isNull();
    assertThat(result.getFields()).isEmpty();
  }

  @Test
  public void createGnsParty_resultContainsCustIdenNoAndSystemId() {
    //when
    GnsParty result = GnsParty.create(SOURCE_SYSTEM_IDENTIFIER, CUSTOMER_IDENTIFICATION_NO);

    //then
    assertThat(result.getSourceSystemIdentifier()).isEqualTo(SOURCE_SYSTEM_IDENTIFIER);
    assertThat(result.getCustomerIdentificationNo()).isEqualTo(CUSTOMER_IDENTIFICATION_NO);
    assertThat(result.getFields()).contains(
        javaMapEntry("sourceSystemIdentifier", SOURCE_SYSTEM_IDENTIFIER),
        javaMapEntry("customerIdentificationNo", CUSTOMER_IDENTIFICATION_NO));
  }

  private static <K, V> Map.Entry<K, V> javaMapEntry(K key, V value) {
    return new SimpleImmutableEntry<>(key, value);
  }

  @Test
  public void addValue_resultContainingCustIdNoAndSystemIdAndValue() {
    //given
    GnsParty result = GnsParty.create(SOURCE_SYSTEM_IDENTIFIER, CUSTOMER_IDENTIFICATION_NO);

    //when
    result.add(FIELD_NAME, VALUE);

    //then
    assertThat(result.getSourceSystemIdentifier()).isEqualTo(SOURCE_SYSTEM_IDENTIFIER);
    assertThat(result.getCustomerIdentificationNo()).isEqualTo(CUSTOMER_IDENTIFICATION_NO);
    assertThat(result.getFields()).contains(
        javaMapEntry("sourceSystemIdentifier", SOURCE_SYSTEM_IDENTIFIER),
        javaMapEntry("customerIdentificationNo", CUSTOMER_IDENTIFICATION_NO),
        javaMapEntry(FIELD_NAME, VALUE));
    assertThat(result.getValue(FIELD_NAME)).isEqualTo(VALUE);
  }

  @Test
  public void createListValue_resultContainsListWithValuesFromFields() {
    //given
    GnsParty result = GnsParty.create(SOURCE_SYSTEM_IDENTIFIER, CUSTOMER_IDENTIFICATION_NO);
    result.add(VALUE_FIELD_NAME_1, VALUE_1);
    result.add(VALUE_FIELD_NAME_2, VALUE_2);

    //when
    result.createList(
        CREATED_FIELD_NAME,
        Arrays.asList(VALUE_FIELD_NAME_1, VALUE_FIELD_NAME_2, NULL_FIELD_NAME));

    //then
    assertThat((List<Object>) result.getValue(CREATED_FIELD_NAME)).contains(VALUE_1, VALUE_2);
  }

  @Test
  public void createMapValue_resultContainsMapWithFieldAndValuesFromFields() {
    //given
    GnsParty result = GnsParty.create(SOURCE_SYSTEM_IDENTIFIER, CUSTOMER_IDENTIFICATION_NO);
    result.add(KEY_FIELD_NAME_1, KEY_VALUE_1);
    result.add(VALUE_FIELD_NAME_1, VALUE_1);
    result.add(KEY_FIELD_NAME_2, KEY_VALUE_2);
    result.add(VALUE_FIELD_NAME_1, VALUE_1);

    //when
    result.createMap(CREATED_FIELD_NAME, MapBuilder.from(KEY_FIELD_NAME_1, VALUE_FIELD_NAME_1,
        KEY_FIELD_NAME_2, VALUE_FIELD_NAME_1,
        NULL_FIELD_NAME, NULL_FIELD_NAME));

    //then
    assertThat((Map<String, Object>) result.getValue(CREATED_FIELD_NAME)).contains(
        javaMapEntry(KEY_VALUE_1, VALUE_1),
        javaMapEntry(KEY_VALUE_2, VALUE_1));
  }

  @Test
  public void createNamedMapValue_resultContainsMapWithFieldFromMapAndValuesFromFields() {
    //given
    GnsParty result = GnsParty.create(SOURCE_SYSTEM_IDENTIFIER, CUSTOMER_IDENTIFICATION_NO);
    result.add(VALUE_FIELD_NAME_1, VALUE_1);
    result.add(VALUE_FIELD_NAME_1, VALUE_1);

    //when
    result.createNamedMap(
        CREATED_FIELD_NAME,
        MapBuilder.from(KEY_FIELD_NAME_1, VALUE_FIELD_NAME_1,
            KEY_FIELD_NAME_2, VALUE_FIELD_NAME_1,
            NULL_FIELD_NAME, NULL_FIELD_NAME));

    //then
    assertThat((Map<String, Object>) result.getValue(CREATED_FIELD_NAME)).contains(
        javaMapEntry(KEY_FIELD_NAME_1, VALUE_1),
        javaMapEntry(KEY_FIELD_NAME_1, VALUE_1));
  }

  @Test
  public void createEmpty_resultEmpty() {
    //given
    GnsParty gnsParty = GnsParty.empty();

    //when
    boolean isEmpty = gnsParty.isEmpty();

    //then
    assertThat(isEmpty).isTrue();
  }

  @Test
  public void createNamedParty_resultNotEmpty() {
    //given
    GnsParty gnsParty = GnsParty.create(SOURCE_SYSTEM_IDENTIFIER, CUSTOMER_IDENTIFICATION_NO);

    //when
    boolean isEmpty = gnsParty.isEmpty();

    //then
    assertThat(isEmpty).isFalse();
  }

  @Test
  public void getAlternateNames_resultContainsListWithAlternateNames() {
    //given
    GnsParty result = GnsParty.create(SOURCE_SYSTEM_IDENTIFIER, CUSTOMER_IDENTIFICATION_NO);
    result.add(VALUE_FIELD_NAME_1, VALUE_1);
    result.add(VALUE_FIELD_NAME_2, VALUE_2);

    //when
    result.createList(
        ALTERNATE_NAMES,
        Arrays.asList(VALUE_FIELD_NAME_1, VALUE_FIELD_NAME_2));

    //then
    assertThat(result.getAlternateNames()).contains(VALUE_1.toString(), VALUE_2.toString());
  }
}
