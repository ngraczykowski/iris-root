package com.silenteight.warehouse.report.reporting;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.silenteight.sep.base.common.time.DefaultTimeSource.INSTANCE;
import static com.silenteight.warehouse.report.ReportFixture.LOCAL_DATE_FROM;
import static com.silenteight.warehouse.report.ReportFixture.LOCAL_DATE_TO;
import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static org.assertj.core.api.Assertions.*;

class ReportRangeTest {

  @Test
  void shouldCreateReportRangeWhenSameDay() {
    // when
    ReportRange reportRange = of(LOCAL_DATE_FROM, LOCAL_DATE_FROM);

    // then
    assertThat(reportRange.getFrom().toLocalDate()).isEqualTo(LOCAL_DATE_FROM);
    assertThat(reportRange.getTo().toLocalDate()).isEqualTo(LOCAL_DATE_FROM);
  }

  @Test
  void shouldThrowInvalidDateRangeParametersOrderException() {
    assertThatThrownBy(() -> of(LOCAL_DATE_TO, LOCAL_DATE_FROM))
        .isInstanceOf(InvalidDateRangeParametersOrderException.class)
        .hasMessageContaining("Date 'from' must be before date 'to'.");
  }

  @Test
  void shouldThrowInvalidDateFromParameterException() {
    LocalDate tomorrowDate = INSTANCE.localDateTime().toLocalDate().plusDays(1);
    assertThatThrownBy(() -> of(tomorrowDate, tomorrowDate))
        .isInstanceOf(InvalidDateFromParameterException.class)
        .hasMessageContaining("Date 'from' cannot be future.");
  }
}
