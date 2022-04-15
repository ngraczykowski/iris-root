package com.silenteight.simulator.management.common;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;

class SimulationResourceTest {

  private static final String RESOURCE_ID = "558ecea2-a1d5-11eb-bcbc-0242ac130002";
  private static final String RESOURCE_PREFIX = "simulations/";

  @Test
  void shouldTransformIdToResourceName() {
    //given
    UUID resourceId = fromString(RESOURCE_ID);

    //when
    String resourceName = SimulationResource.toResourceName(resourceId);

    //then
    assertThat(resourceName).isEqualTo(RESOURCE_PREFIX + RESOURCE_ID);
  }
}
