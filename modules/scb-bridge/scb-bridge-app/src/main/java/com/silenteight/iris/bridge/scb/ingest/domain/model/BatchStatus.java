/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain.model;

public enum BatchStatus {
  /**
   * Batch is ready for processing.
   */
  QUEUED,

  /**
   * Batch is registered in core-bridge & uds, we wait for the response.
   */
  REGISTERED,

  /**
   * Batch has finished successfully.
   */
  COMPLETED,

  /**
   * Batch failed.
   */
  ERROR
}
