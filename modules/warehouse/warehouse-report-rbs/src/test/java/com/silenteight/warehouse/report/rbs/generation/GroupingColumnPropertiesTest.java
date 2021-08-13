package com.silenteight.warehouse.report.rbs.generation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GroupingColumnPropertiesTest {

  private GroupingColumnProperties underTest;

  @Test
  void shouldReturnLabelsFromValuesWhenGroupingLabelEmpty() {
    underTest = new GroupingColumnProperties("alert_analyst_decision", "",
        of("FP", "TP", "PTP"), true);
    assertThat(underTest.getLabels()).isEqualTo(of("count", "FP", "TP", "PTP"));
  }

  @Test
  void shouldReturnLabelsFromValuesAndLabel() {
    underTest = new GroupingColumnProperties("alert_analyst_decision",
        "action", of("FP", "TP", "PTP"), true);
    assertThat(underTest.getLabels())
        .isEqualTo(of("action_count", "action_FP", "action_TP", "action_PTP"));
  }
}
