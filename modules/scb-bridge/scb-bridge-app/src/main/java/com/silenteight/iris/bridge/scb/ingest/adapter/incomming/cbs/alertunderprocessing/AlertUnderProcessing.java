/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertunderprocessing;

import lombok.*;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing.AlertUnderProcessingKey;
import com.silenteight.sep.base.common.entity.BaseEntity;

import org.hibernate.annotations.Type;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "scb_cbs_alert_under_processing")
@Setter(AccessLevel.NONE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@IdClass(AlertUnderProcessingKey.class)
public class AlertUnderProcessing extends BaseEntity {

  @NonNull
  @Id
  private String systemId;

  @NonNull
  @Id
  private String batchId;

  @NonNull
  @Enumerated(EnumType.STRING)
  private State state = State.IN_PROGRESS;

  @Size(max = 1000)
  private String error;

  @NonNull
  private int priority;

  @NonNull
  @Lob
  @Type(type = "org.hibernate.type.BinaryType")
  private byte[] payload;

  public AlertUnderProcessing(String systemId, String batchId, ScbAlertIdContext alertIdContext) {
    this.systemId = systemId;
    this.batchId = batchId;
    this.priority = alertIdContext.getPriority();
    this.payload = alertIdContext.toByteArray();
  }

  public enum State {
    IN_PROGRESS,
    ACK,
    ERROR
  }

  @Data
  public static class AlertUnderProcessingKey implements Serializable {

    private static final long serialVersionUID = -2744880301702129020L;

    private String systemId;
    private String batchId;
  }
}

