package com.silenteight.warehouse.report.reporting;

import com.silenteight.warehouse.report.reporting.exception.InvalidDateFromParameterException;
import com.silenteight.warehouse.report.reporting.exception.InvalidDateRangeParametersOrderException;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static com.silenteight.sep.base.common.time.DefaultTimeSource.INSTANCE;
import static com.silenteight.warehouse.report.ReportFixture.LOCAL_DATE_FROM;
import static com.silenteight.warehouse.report.ReportFixture.LOCAL_DATE_TO;
import static com.silenteight.warehouse.report.ReportFixture.OFFSET_DATE_TIME_FROM;
import static com.silenteight.warehouse.report.ReportFixture.OFFSET_DATE_TIME_TO;
import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.*;

class ReportRangeTest {

  @Test
  void shouldCreateReportRangeWhenSameDay() {
    // when
    ReportRange reportRange = of(OFFSET_DATE_TIME_FROM, OFFSET_DATE_TIME_FROM);

    // then
    assertThat(reportRange.getFrom()).isEqualTo(OFFSET_DATE_TIME_FROM);
    assertThat(reportRange.getTo()).isEqualTo(OFFSET_DATE_TIME_FROM);
  }

  @Test
  void shouldReturnRangeAsLocalDate() {
    // when
    ReportRange reportRange = of(OFFSET_DATE_TIME_FROM, OFFSET_DATE_TIME_TO);

    // then
    assertThat(reportRange.getFromAsLocalDate()).isEqualTo(LOCAL_DATE_FROM);
    assertThat(reportRange.getToAsLocalDate()).isEqualTo(LOCAL_DATE_TO);
  }

  @Test
  void shouldThrowInvalidDateRangeParametersOrderException() {
    assertThatThrownBy(() -> of(OFFSET_DATE_TIME_TO, OFFSET_DATE_TIME_FROM))
        .isInstanceOf(InvalidDateRangeParametersOrderException.class)
        .hasMessageContaining("Date 'from' must be before date 'to'.");
  }

  @Test
  void shouldThrowInvalidDateFromParameterException() {
    OffsetDateTime tomorrowDate = INSTANCE.localDateTime().atOffset(UTC).plusDays(1);
    assertThatThrownBy(() -> of(tomorrowDate, tomorrowDate))
        .isInstanceOf(InvalidDateFromParameterException.class)
        .hasMessageContaining("Date 'from' cannot be future.");
  }
}
