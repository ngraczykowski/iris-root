package com.silenteight.sens.webapp.scb.report;

import com.silenteight.sep.base.common.time.TimeSource;

import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

import java.time.ZonedDateTime;

import static com.cronutils.model.CronType.QUARTZ;
import static com.cronutils.model.definition.CronDefinitionBuilder.instanceDefinitionFor;
import static com.cronutils.model.time.ExecutionTime.forCron;

/*
Provides recently elapsed date range based on crone expression
 */
class DateRangeProvider {

  private static final CronParser CRON_PARSER = new CronParser(instanceDefinitionFor(QUARTZ));

  private final ExecutionTime executionTime;
  private final TimeSource timeSource;

  DateRangeProvider(String cronExpression, TimeSource timeSource) {
    executionTime = forCron(CRON_PARSER.parse(cronExpression));
    this.timeSource = timeSource;
  }

  DateRange latestDateRange() {
    ZonedDateTime lastExecutionTime = executionTime.lastExecution(
        timeSource.offsetDateTime().atZoneSameInstant(timeSource.timeZone().toZoneId())).get();
    ZonedDateTime penultimateExecutionTime =
        executionTime.lastExecution(lastExecutionTime.minusSeconds(1)).get();

    return new DateRange(
        penultimateExecutionTime.toOffsetDateTime(), lastExecutionTime.toOffsetDateTime());
  }
}
