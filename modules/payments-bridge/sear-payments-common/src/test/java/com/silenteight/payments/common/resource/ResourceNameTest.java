package com.silenteight.payments.common.resource;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.silenteight.payments.common.resource.ResourceName.create;
import static org.assertj.core.api.Assertions.*;

class ResourceNameTest {

  private static final String NAME = "alerts/1/datasets/3";

  @Test
  void shouldGetAndCreatePathFromResource() {
    var path = create(NAME).getPath();
    assertThat(path).isEqualTo(NAME);
  }

  @Test
  void shouldGetAlertIdFromToken() {
    var alertId = create(NAME).getLong("alerts");
    assertThat(alertId).isEqualTo(1);
  }

  @Test
  void shouldReplaceId() {
    var resource = create(NAME).replaceLong("datasets", 5);
    assertThat(resource.getLong("datasets")).isEqualTo(5);
  }

  @Test
  void shouldReplaceToken() {
    var resource = create(NAME).replaceName("datasets", "matches", "7");
    assertThat(resource.getLong("matches")).isEqualTo(7);
    assertThatThrownBy(() -> resource.getLong("datasets"))
        .hasMessage("Resource name has no part 'datasets'.");
    assertThat(resource.getPath()).isEqualTo("alerts/1/matches/7");
  }

  @Test
  void shouldRemoveToken() {
    var resource = create(NAME).remove("datasets");
    assertThat(resource.getPath()).isEqualTo("alerts/1");
  }

  @Test
  void shouldCopyAndReplaceWithoutImpactOnOriginal() {
    var resource1 = create(NAME);
    var resource2 = resource1.copy();

    assertThat(resource2.replaceLong("alerts", 2).getLong("alerts")).isEqualTo(2);
    assertThat(resource1.getLong("alerts")).isEqualTo(1);
  }

  @Test
  void shouldNotFailForOddNumberOfTokens() {
    assertThatNoException().isThrownBy(() -> create("odd/number/of/name/tokens"));
  }

  @Test
  void shouldContainName() {
    var resource = create(NAME);
    assertThat(resource.contains("datasets")).isTrue();
    assertThat(resource.contains("dummy")).isFalse();
  }

  @Test
  void shouldParseUuid() {
    var uuid = UUID.randomUUID();
    var resource = create("id/" + uuid);
    assertThat(resource.getUuid("id")).isEqualTo(uuid);
  }
}
