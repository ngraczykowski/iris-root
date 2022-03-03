package com.silenteight.customerbridge.cbs.quartz;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.cbs.alertid.AlertIdContext;

import org.quartz.*;

import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import static java.util.Collections.emptyList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@Builder
public class QueuingJobsLauncher {

  private final List<QueuingJobProperties> queuingJobs;
  private final Scheduler scheduler;

  @PostConstruct
  public void initialize() throws SchedulerException {
    for (QueuingJobProperties properties : getEnabledQueuingJobs()) {
      JobDetail jobDetail = JobBuilder
          .newJob(QueuingJob.class)
          .withIdentity(properties.getName(), "queuingJob")
          .setJobData(createQueuingJobDataMap(properties))
          .build();
      Trigger cronTrigger = createCronTrigger(jobDetail.getKey(), properties);

      scheduler.scheduleJob(jobDetail, cronTrigger);
    }
  }

  private static JobDataMap createQueuingJobDataMap(QueuingJobProperties properties) {
    JobDataMap jobDataMap = new JobDataMap();
    jobDataMap.put(QueuingJobConstants.CONTEXT, createContext(properties));
    jobDataMap.put(QueuingJobConstants.NAME, properties.getName());
    return jobDataMap;
  }

  private static AlertIdContext createContext(QueuingJobProperties properties) {
    return AlertIdContext.builder()
        .ackRecords(properties.isAckRecords())
        .hitDetailsView(properties.getHitDetailsView())
        .priority(properties.getPriority())
        .recordsView(properties.getRecordsView())
        .watchlistLevel(properties.isWatchlistLevel())
        .build();
  }

  static Trigger createCronTrigger(JobKey jobKey, QueuingJobProperties properties) {
    return TriggerBuilder
        .newTrigger()
        .forJob(jobKey)
        .withPriority(properties.getPriority())
        .withSchedule(CronScheduleBuilder.cronSchedule(properties.getCronExpression()))
        .build();
  }

  private List<QueuingJobProperties> getEnabledQueuingJobs() {
    if (isEmpty(queuingJobs))
      return emptyList();

    return queuingJobs.stream()
        .filter(QueuingJobProperties::isEnabled)
        .collect(Collectors.toList());
  }
}
