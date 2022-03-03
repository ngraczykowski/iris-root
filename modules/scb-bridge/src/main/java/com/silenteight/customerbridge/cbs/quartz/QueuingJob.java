package com.silenteight.customerbridge.cbs.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.cbs.alertid.AlertIdContext;
import com.silenteight.customerbridge.cbs.alertid.ExternalAlertIdReader;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@DisallowConcurrentExecution
@Slf4j
public class QueuingJob implements Job {

  private final ExternalAlertIdReader externalAlertIdReader;

  @Override
  public void execute(JobExecutionContext jobExecutionContext) {
    JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

    externalAlertIdReader.read(getAlertIdContext(jobDataMap));

    if (log.isDebugEnabled())
      log.debug(
          "Queuing job: {} has been finished.", jobDataMap.getString(QueuingJobConstants.NAME));
  }

  private static AlertIdContext getAlertIdContext(JobDataMap jobDataMap) {
    return (AlertIdContext) jobDataMap.get(QueuingJobConstants.CONTEXT);
  }
}
