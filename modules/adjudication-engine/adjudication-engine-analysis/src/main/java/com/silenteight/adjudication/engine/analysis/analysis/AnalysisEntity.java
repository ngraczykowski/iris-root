package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.sep.base.common.entity.BaseEntity;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import org.hibernate.annotations.Fetch;

import java.util.List;
import java.util.Map;
import javax.persistence.*;

import static com.silenteight.adjudication.engine.common.protobuf.TimestampConverter.fromOffsetDateTime;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;
import static org.hibernate.annotations.FetchMode.SUBSELECT;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@Builder(access = PACKAGE)
class AnalysisEntity extends BaseEntity implements IdentifiableEntity {

  @Id
  @Column(name = "analysis_id", insertable = false, updatable = false, nullable = false)
  @GeneratedValue(strategy = IDENTITY)
  @Setter(PUBLIC)
  @Include
  private Long id;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String policy;

  @Column(updatable = false, nullable = false)
  @NonNull
  private String strategy;

  @ElementCollection(fetch = LAZY)
  @CollectionTable(joinColumns = @JoinColumn(name = "analysis_id"))
  @MapKeyColumn(name = "name")
  @Column(name = "value")
  @Fetch(SUBSELECT)
  @Singular
  private Map<String, String> labels;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "analysis_id", referencedColumnName = "analysis_id", nullable = false)
  @OrderBy("id")
  @Fetch(SUBSELECT)
  @Singular
  private List<AnalysisCategory> categories;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "analysis_id", referencedColumnName = "analysis_id", nullable = false)
  @OrderBy("id")
  @Fetch(SUBSELECT)
  @Singular
  private List<AnalysisFeature> features;

  @Column(updatable = false, nullable = false)
  private boolean attachMetadata;

  @Column(updatable = false, nullable = false)
  private boolean attachRecommendation;

  @Transient
  String getName() {
    return "analysis/" + getId();
  }

  Analysis.Builder updateBuilder(Analysis.Builder builder) {
    return builder
        .setName(getName())
        .setPolicy(getPolicy())
        .setStrategy(getStrategy())
        .setCreateTime(fromOffsetDateTime(getCreatedAt()))
        .putAllLabels(getLabels())
        .setNotificationFlags(Analysis.NotificationFlags.newBuilder()
            .setAttachMetadata(this.attachMetadata)
            .setAttachRecommendation(this.attachRecommendation).build());
  }
}
