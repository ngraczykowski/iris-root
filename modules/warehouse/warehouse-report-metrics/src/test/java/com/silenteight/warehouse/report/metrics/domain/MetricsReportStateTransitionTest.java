package com.silenteight.warehouse.report.metrics.domain;

import com.silenteight.warehouse.report.metrics.domain.exception.WrongReportStateException;

import org.junit.jupiter.api.Test;

import static com.silenteight.warehouse.report.metrics.MetricsReportTestFixtures.REPORT_RANGE;
import static com.silenteight.warehouse.report.metrics.domain.ReportState.DONE;
import static com.silenteight.warehouse.report.metrics.domain.ReportState.FAILED;
import static com.silenteight.warehouse.report.metrics.domain.ReportState.GENERATING;
import static com.silenteight.warehouse.report.metrics.domain.ReportState.NEW;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;

class MetricsReportStateTransitionTest {

  private static final String FAIL_MESSAGE =
      "Cannot change state from %s to %s for Metrics Report with id: %s";

  @Test
  void shouldChangeStateWhenValidFlow() {
    MetricsReport metricsReport = MetricsReport.of(REPORT_RANGE);
    assertThat(metricsReport).extracting(MetricsReport::getState).isEqualTo(NEW);

    metricsReport.generating();
    assertThat(metricsReport).extracting(MetricsReport::getState).isEqualTo(GENERATING);

    metricsReport.done();
    assertThat(metricsReport).extracting(MetricsReport::getState).isEqualTo(DONE);
  }

  @Test
  void shouldChangeStateWhenValidFlow2() {
    MetricsReport metricsReport = MetricsReport.of(REPORT_RANGE);
    assertThat(metricsReport).extracting(MetricsReport::getState).isEqualTo(NEW);

    metricsReport.generating();
    assertThat(metricsReport).extracting(MetricsReport::getState).isEqualTo(GENERATING);

    metricsReport.failed();
    assertThat(metricsReport).extracting(MetricsReport::getState).isEqualTo(FAILED);
  }

  @Test
  void shouldChangeStateWhenValidFlow3() {
    MetricsReport metricsReport = MetricsReport.of(REPORT_RANGE);
    assertThat(metricsReport).extracting(MetricsReport::getState).isEqualTo(NEW);

    metricsReport.failed();
    assertThat(metricsReport).extracting(MetricsReport::getState).isEqualTo(FAILED);
  }

  @Test
  void shouldThrowWrongReportStateExceptionWhenInvalidFlow() {
    MetricsReport metricsReport = MetricsReport.of(REPORT_RANGE);
    assertThat(metricsReport).extracting(MetricsReport::getState).isEqualTo(NEW);

    assertThatThrownBy(metricsReport::done)
        .isInstanceOf(WrongReportStateException.class)
        .hasMessage(format(FAIL_MESSAGE, NEW, DONE, null));
  }

  @Test
  void shouldThrowWrongReportStateExceptionWhenInvalidFlow2() {
    MetricsReport metricsReport = MetricsReport.of(REPORT_RANGE);
    assertThat(metricsReport).extracting(MetricsReport::getState).isEqualTo(NEW);

    metricsReport.generating();
    metricsReport.done();

    assertThatThrownBy(metricsReport::generating)
        .isInstanceOf(WrongReportStateException.class)
        .hasMessage(format(FAIL_MESSAGE, DONE, GENERATING, null));
  }

  @Test
  void shouldThrowWrongReportStateExceptionWhenInvalidFlow3() {
    MetricsReport metricsReport = MetricsReport.of(REPORT_RANGE);
    assertThat(metricsReport).extracting(MetricsReport::getState).isEqualTo(NEW);

    metricsReport.generating();
    metricsReport.done();

    assertThatThrownBy(metricsReport::generating)
        .isInstanceOf(WrongReportStateException.class)
        .hasMessage(format(FAIL_MESSAGE, DONE, GENERATING, null));
  }

  @Test
  void shouldThrowWrongReportStateExceptionWhenInvalidFlow4() {
    MetricsReport metricsReport = MetricsReport.of(REPORT_RANGE);
    assertThat(metricsReport).extracting(MetricsReport::getState).isEqualTo(NEW);

    metricsReport.generating();
    metricsReport.failed();

    assertThatThrownBy(metricsReport::generating)
        .isInstanceOf(WrongReportStateException.class)
        .hasMessage(format(FAIL_MESSAGE, FAILED, GENERATING, null));
  }
}
