package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.*;

import com.silenteight.adjudication.api.v1.AnalysisAlert;
import com.silenteight.adjudication.engine.common.resource.ResourceName;
import com.silenteight.sep.base.common.entity.BaseEntity;

import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import static com.silenteight.adjudication.engine.common.protobuf.TimestampConverter.fromOffsetDateTime;
import static com.silenteight.solving.api.utils.Timestamps.toOffsetDateTime;
import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "AnalysisAlert")
@Builder(access = PACKAGE)
class AnalysisAlertEntity extends BaseEntity {

  @EmbeddedId
  private AnalysisAlertKey id;

  @Column(updatable = false)
  private OffsetDateTime deadlineAt;

  public static AnalysisAlertEntity fromAnalysisAlert(
      long analysisId, AnalysisAlert analysisAlert) {
    long alertId = ResourceName.create(analysisAlert.getAlert()).getLong("alerts");
    return AnalysisAlertEntity
        .builder()
        .id(new AnalysisAlertKey(analysisId, alertId))
        .deadlineAt(toOffsetDateTime(analysisAlert.getDeadlineTime()))
        .build();
  }

  public AnalysisAlert toAnalysisAlert() {
    return AnalysisAlert
        .newBuilder()
        .setName(id.toName())
        .setAlert("alerts/" + id.getAlertId())
        .setCreateTime(fromOffsetDateTime(getCreatedAt()))
        .setDeadlineTime(fromOffsetDateTime(getDeadlineAt()))
        .build();
  }

  public String toAlertName() {
    return id.toName();
  }
}
