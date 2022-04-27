package com.silenteight.scb.ingest.adapter.incomming.cbs.quartz;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertIdReaderContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.ExternalAlertIdReader;

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

    externalAlertIdReader.read(context(jobDataMap));

    if (log.isDebugEnabled())
      log.debug(
          "Queuing job: {} has been finished.", jobDataMap.getString(QueuingJobConstants.NAME));
  }

  private static AlertIdReaderContext context(JobDataMap jobDataMap) {
    return (AlertIdReaderContext) jobDataMap.get(QueuingJobConstants.CONTEXT);
  }
}
