package com.silenteight.serp.governance.qa.manage.common;

import org.junit.jupiter.api.Test;

import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.FAILED;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.NEW;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionState.VIEWING;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

class DecisionStateConverterTest {

  @Test
  void shouldConvertDecisionStatesToStringList() {
    assertThat(DecisionStateConverter.asStringList(of(NEW, VIEWING, FAILED)))
        .isEqualTo(of("NEW", "VIEWING", "FAILED"));
  }
}
