package com.silenteight.serp.governance.model.common;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModelResourceTest {

  private static final String RESOURCE_ID = "558ecea2-a1d5-11eb-bcbc-0242ac130002";
  private static final String RESOURCE_PREFIX = "solvingModels/";

  @Test
  void shouldExtractIdFromResourceName() {
    //given
    String resourceName = RESOURCE_PREFIX + RESOURCE_ID;
    //when
    UUID resourceId = ModelResource.fromResourceName(resourceName);
    //then
    assertThat(resourceId).isEqualTo(fromString(RESOURCE_ID));
  }

  @Test
  void whenExtractIdFromResourceNameShouldThrowIllegalArgumentException() {
    //when
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      ModelResource.fromResourceName("notExistingPrefix/" + RESOURCE_ID);
    });
    //then
    String expected = "UUID string too large";
    assertTrue(exception.getMessage().contains(expected));
  }
}