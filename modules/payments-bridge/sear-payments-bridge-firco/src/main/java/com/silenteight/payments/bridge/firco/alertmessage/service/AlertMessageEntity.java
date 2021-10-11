package com.silenteight.payments.bridge.firco.alertmessage.service;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.payments.bridge.firco.alertmessage.model.FircoAlertMessage;
import com.silenteight.sep.base.common.entity.BaseEntity;

import java.time.OffsetDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PUBLIC;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "AlertMessage")
class AlertMessageEntity extends BaseEntity {

  @Id
  @Column(name = "alert_message_id", insertable = false, updatable = false, nullable = false)
  @Setter(PUBLIC)
  @Include
  private UUID id;

  @Column(nullable = false, updatable = false)
  private OffsetDateTime receivedAt;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String dataCenter;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String unit;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String businessUnit;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String messageId;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String systemId;

  @Column(updatable = false)
  private String decisionUrl;

  @Column(updatable = false, nullable = false)
  @NonNull
  private Integer priority = 5;

  @Column(updatable = false, nullable = false)
  @NonNull
  private Integer numberOfHits;

  AlertMessageEntity(FircoAlertMessage message) {
    id = message.getId();
    receivedAt = message.getReceivedAt();
    dataCenter = message.getDataCenter();
    decisionUrl = message.getDecisionUrl();
    var alert = message.getAlertMessage();

    unit = alert.getUnit();
    businessUnit = alert.getBusinessUnit();
    messageId = alert.getMessageID();
    systemId = alert.getSystemID();
    numberOfHits = alert.getHits().size();
  }
}
