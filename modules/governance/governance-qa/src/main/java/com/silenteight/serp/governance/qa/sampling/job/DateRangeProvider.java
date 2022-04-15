package com.silenteight.serp.governance.qa.sampling.job;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.serp.governance.qa.sampling.domain.dto.DateRangeDto;
import com.silenteight.serp.governance.qa.sampling.job.exception.CronExpressionProcessingException;

import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import java.time.ZonedDateTime;

import static com.cronutils.model.CronType.QUARTZ;
import static com.cronutils.model.definition.CronDefinitionBuilder.instanceDefinitionFor;
import static com.cronutils.model.time.ExecutionTime.forCron;

class DateRangeProvider {

  private static final CronParser CRON_PARSER = new CronParser(instanceDefinitionFor(QUARTZ));

  private final ExecutionTime executionTime;
  private final TimeSource timeSource;

  DateRangeProvider(String cronExpression, TimeSource timeSource) {
    executionTime = forCron(CRON_PARSER.parse(cronExpression));
    this.timeSource = timeSource;
  }

  DateRangeDto latestDateRange() {
    ZonedDateTime lastExecutionTime = executionTime
        .lastExecution(
            timeSource.offsetDateTime().atZoneSameInstant(timeSource.timeZone().toZoneId()))
        .orElseThrow(CronExpressionProcessingException::new);

    ZonedDateTime penultimateExecutionTime = executionTime
        .lastExecution(lastExecutionTime.minusSeconds(1))
        .orElseThrow(CronExpressionProcessingException::new);

    return new DateRangeDto(
        penultimateExecutionTime.toOffsetDateTime(), lastExecutionTime.toOffsetDateTime());
  }
}
