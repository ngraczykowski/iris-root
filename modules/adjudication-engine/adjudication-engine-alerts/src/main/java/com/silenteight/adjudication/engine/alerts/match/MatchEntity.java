package com.silenteight.adjudication.engine.alerts.match;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.adjudication.api.v1.Match;
import com.silenteight.adjudication.engine.common.protobuf.TimestampConverter;
import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import org.hibernate.annotations.Fetch;

import java.util.Map;
import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;
import static org.hibernate.annotations.FetchMode.SUBSELECT;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity(name = "Match")
@Builder(access = PACKAGE)
class MatchEntity extends BaseEntity implements IdentifiableEntity {

  @Id
  @Column(name = "match_id", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = IDENTITY)
  @Setter(PUBLIC)
  @Include
  private Long id;

  @Column(updatable = false, nullable = false)
  @Include
  @NonNull
  private Long alertId;

  @Column(updatable = false)
  @NonNull
  private String clientMatchIdentifier;

  @Column(updatable = false)
  @NonNull
  private Integer sortIndex;

  @ElementCollection(fetch = LAZY)
  @CollectionTable(joinColumns = @JoinColumn(name = "match_id"))
  @MapKeyColumn(name = "name")
  @Column(name = "value")
  @Fetch(SUBSELECT)
  @Singular
  private Map<String, String> labels;

  Match toMatch() {
    return Match.newBuilder()
        .setName("alerts/" + getAlertId() + "/matches/" + getId())
        .setMatchId(getClientMatchIdentifier())
        .setCreateTime(TimestampConverter.fromOffsetDateTime(getCreatedAt()))
        .setIndex(getSortIndex())
        .putAllLabels(labels)
        .build();
  }
}
