package com.silenteight.scb.ingest.adapter.incomming.common.recommendation;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.sep.base.common.entity.BaseEntity;

import org.apache.commons.lang3.StringUtils;

import java.time.OffsetDateTime;
import javax.annotation.Nullable;
import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PUBLIC;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter(PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Table(name = "ScbRecommendation")
public class ScbRecommendation extends BaseEntity {

  public static final String CONCATENATION_ELEMENT = " - ";
  @Id
  @Column(name = "scbRecommendationId", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = IDENTITY)
  @Setter(PUBLIC)
  @Include
  private Long id;
  private String systemId;
  private String alertName;
  @Nullable
  private String watchlistId;
  private String discriminator;
  private String decision;
  private String comment;
  private OffsetDateTime recommendedAt;
  @Nullable
  private String recomStatus;

  public String requireAlertName() {
    if (StringUtils.isBlank(alertName)) {
      throw new IllegalStateException(
          "AlertName on Recommendation: " + this + " must not be empty");
    }
    return alertName;
  }
}
