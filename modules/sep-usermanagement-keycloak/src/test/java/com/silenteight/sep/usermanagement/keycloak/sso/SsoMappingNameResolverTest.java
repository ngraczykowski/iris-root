package com.silenteight.sep.usermanagement.keycloak.sso;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SsoMappingNameResolverTest {

  private String canonicalMappingName = "Mapper_ID_1c2239ea-7b4b-4605-a4de-be209e32084c_SUB_4";

  @Test
  void buildTest() {
    //given
    final UUID mappingId = UUID.randomUUID();
    final Integer mappingIndex = 7;

    //when
    var actual = SsoMappingNameResolver
        .build("My role mapping", mappingId, mappingIndex);

    //then
    assertEquals("My role mapping_ID_" + mappingId + "_SUB_7", actual);
  }

  @Test
  void isLegacyName_returnTrueIfTheNameDoesntFollowSpecification() {
    assertTrue(SsoMappingNameResolver.isLegacyName("Some old mapping"));
  }

  @Test
  void isLegacyName_returnTrueIfTheNameFollowsSpecification() {
    assertTrue(SsoMappingNameResolver.isLegacyName("Some old mapper name"));
  }

  @Test
  void extractId() {
    assertEquals("1c2239ea-7b4b-4605-a4de-be209e32084c",
        SsoMappingNameResolver.extractId(canonicalMappingName));
  }

  @Test
  void extractSharedName() {
    assertEquals(SsoMappingNameResolver.extractSharedName(canonicalMappingName), "Mapper");
  }
}