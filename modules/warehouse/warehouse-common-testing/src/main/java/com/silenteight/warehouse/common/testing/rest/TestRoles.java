package com.silenteight.warehouse.common.testing.rest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestRoles {

  public static final String USER_ADMINISTRATOR = "USER_ADMINISTRATOR";
  public static final String APPROVER = "APPROVER";
  public static final String AUDITOR = "AUDITOR";
  public static final String MODEL_TUNER = "MODEL_TUNER";
  public static final String QA = "QA";
  public static final String QA_ISSUE_MANAGER = "QA_ISSUE_MANAGER";
}
