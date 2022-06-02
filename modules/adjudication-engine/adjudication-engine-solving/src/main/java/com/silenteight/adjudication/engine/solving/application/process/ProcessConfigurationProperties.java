/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ae.solving.process")
class ProcessConfigurationProperties {

  private CommentInputProcess commentInputProcess = new CommentInputProcess();
  private CategoryProcess categoryProcess = new CategoryProcess();
  private GovernanceProcess governanceProcess = new GovernanceProcess();
  private SolvedAlertProcess solvedAlertProcess = new SolvedAlertProcess();

  @Data
  static class CommentInputProcess {
    private int poolSize = 1;
  }

  @Data
  static class CategoryProcess {
    private int poolSize = 30;
  }

  @Data
  static class GovernanceProcess {
    private int poolSize = 15;
  }

  @Data
  static class SolvedAlertProcess {
    private String commentTemplate = "alert";
    private String matchCommentTemplate = "match-template";
  }
}
