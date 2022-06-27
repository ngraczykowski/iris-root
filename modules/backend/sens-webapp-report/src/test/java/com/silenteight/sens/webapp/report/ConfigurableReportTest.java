package com.silenteight.sens.webapp.report;

import lombok.NonNull;

import com.silenteight.sens.webapp.report.list.FilterType;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfigurableReportTest {

  private static final String REPORT_NAME = "test-configurable-report";
  private static final String REPORT_LABEL = "Test configurable report";
  private static final FilterType FILTER_DATE_RANGE = FilterType.DATE_RANGE;
  private static final boolean REPORT_ENABLED = true;
  private final ReportProperties reportProperties = mock(ReportProperties.class);
  private ConfigurableReport underTest = new TestConfigurableReport(reportProperties);

  @Test
  void reportShouldBeConfigurable() {
    when(reportProperties.getName()).thenReturn(REPORT_NAME);
    when(reportProperties.getLabel()).thenReturn(REPORT_LABEL);
    when(reportProperties.getFilterType()).thenReturn(FILTER_DATE_RANGE);
    when(reportProperties.isEnabled()).thenReturn(REPORT_ENABLED);

    assertThat(underTest).isInstanceOf(ConfigurableReport.class);
    assertThat(underTest.getName()).isEqualTo(REPORT_NAME);
    assertThat(underTest.getLabel()).isEqualTo(REPORT_LABEL);
    assertThat(underTest.getFilterType()).isEqualTo(FILTER_DATE_RANGE);
    assertThat(underTest.isEnabled()).isEqualTo(REPORT_ENABLED);
  }

  private static class TestConfigurableReport extends AbstractConfigurableReport {

    TestConfigurableReport(@NonNull ReportProperties reportProperties) {
      super(reportProperties);
    }
  }
}
