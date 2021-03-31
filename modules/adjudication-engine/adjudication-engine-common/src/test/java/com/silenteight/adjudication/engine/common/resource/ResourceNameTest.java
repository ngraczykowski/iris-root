package com.silenteight.adjudication.engine.common.resource;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ResourceNameTest {

  String name = "alerts/1/datasets/3";


  @Test
  void shouldGetAndCreatePathFromResource() {
    var path = ResourceName.getResource(name).getPath();
    assertThat(path).isEqualTo(name);
  }

  @Test
  void shouldGetAlertIdFromToken() {
    var alertId = ResourceName.getResource(name).getId("alerts");
    assertThat(alertId).isEqualTo(1);
  }

  @Test
  void shouldReplaceId() {
    var resource = ResourceName.getResource(name).replaceId("datasets", 5);
    assertThat(resource.getId("datasets")).isEqualTo(5);
  }

  @Test
  void shouldReplaceToken() {
    var resource = ResourceName.getResource(name).replaceName("datasets", "matches", 7);
    assertThat(resource.getId("matches")).isEqualTo(7);
    assertThatThrownBy(() -> resource.getId("datasets"))
        .hasMessage("ResourceName could not find token for name: datasets");
    assertThat(resource.getPath()).isEqualTo("alerts/1/matches/7");

  }

  @Test
  void shouldRemoveToken() {
    var resource = ResourceName.getResource(name).remove("datasets");
    assertThat(resource.getPath()).isEqualTo("alerts/1");
  }


  @Test
  void shouldCopyAndReplaceWithoutImpactOnOryginal() {
    var resource1 = ResourceName.getResource(name);
    var resource2 = resource1.copy();

    assertThat(resource2.replaceId("alerts", 2).getId("alerts")).isEqualTo(2);
    assertThat(resource1.getId("alerts")).isEqualTo(1);
  }
}
