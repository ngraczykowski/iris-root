package com.silenteight.customerbridge.cbs.alertunderprocessing;

import lombok.*;

import com.silenteight.customerbridge.cbs.alertunderprocessing.AlertUnderProcessing.AlertUnderProcessingKey;
import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.sep.base.common.entity.BaseEntity;

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
  private byte[] payload;

  public AlertUnderProcessing(String systemId, String batchId, ScbAlertIdContext alertIdContext) {
    this.systemId = systemId;
    this.batchId = batchId;
    this.priority = alertIdContext.getPriority();
    this.payload = alertIdContext.toByteArray();
  }

  @Data
  public static class AlertUnderProcessingKey implements Serializable {

    private static final long serialVersionUID = -2744880301702129020L;

    private String systemId;
    private String batchId;
  }

  public enum State {
    IN_PROGRESS,
    ERROR
  }
}

