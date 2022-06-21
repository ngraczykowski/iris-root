/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package utils.datageneration.namescreening.ftcc;

import utils.CommonUtils;

public class FtccAlertGenerationService {

  public FtccAlert generate(String process) {
    String template;
    if (process.equals("solving")) {
      template = CommonUtils.getJsonTemplate("alertTemplates/foxtrot", "solvingAlertTemplate");
    } else {
      template = CommonUtils.getJsonTemplate("alertTemplates/foxtrot", "learningAlertTemplate");
    }
    return FtccAlert.builder().payload(template).build();
  }
}
