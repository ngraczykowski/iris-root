package com.silenteight.sens.webapp.common.util;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.*;

class WhitelistPredicateTest {

  private static final String BATCH_TYPE_WHITELISTED = "WHITELIST";
  private static final String BATCH_TYPE_OTHER = "OTHER";

  @Test
  public void emptyWhitelist_noBatchTypeBlocked() {
    WhitelistPredicate whitelistPredicate = new WhitelistPredicate(emptyList());

    assertThat(whitelistPredicate.test(BATCH_TYPE_OTHER)).isTrue();
  }

  @Test
  public void batchTypeWithWhitelistedBatchType_batchTypeNonFiltered() {
    WhitelistPredicate whitelistPredicate = new WhitelistPredicate(
        asList(BATCH_TYPE_WHITELISTED, BATCH_TYPE_OTHER));

    assertThat(whitelistPredicate.test(BATCH_TYPE_WHITELISTED)).isTrue();
  }

  @Test
  public void batchTypeWithNoWhitelistedBatchType_batchTypeFiltered() {
    WhitelistPredicate whitelistPredicate = new WhitelistPredicate(
        singletonList(BATCH_TYPE_WHITELISTED));

    assertThat(whitelistPredicate.test(BATCH_TYPE_OTHER)).isFalse();
  }
}
