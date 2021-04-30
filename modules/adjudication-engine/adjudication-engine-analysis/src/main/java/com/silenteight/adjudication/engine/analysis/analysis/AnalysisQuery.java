package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.*;
import lombok.EqualsAndHashCode.Include;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.sep.base.common.entity.IdentifiableEntity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.Immutable;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.*;

import static java.util.stream.Collectors.toUnmodifiableList;
import static lombok.AccessLevel.*;
import static org.hibernate.annotations.FetchMode.SUBSELECT;

@Data
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
@Setter(NONE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Immutable
@Entity
@Builder(access = PACKAGE)
class AnalysisQuery implements IdentifiableEntity {

  @Id
  @Column(name = "analysis_id")
  @Setter(PUBLIC)
  @Include
  private Long id;

  @NonNull
  private Long pendingAlerts;

  @NonNull
  private Long alertCount;

  @OneToOne(cascade = { CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH })
  @JoinColumn(name = "analysis_id", referencedColumnName = "analysis_id")
  @NonNull
  private AnalysisEntity analysis;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "analysis_id", referencedColumnName = "analysis_id")
  @OrderBy("id")
  @Fetch(SUBSELECT)
  @Singular
  private List<AnalysisCategoryQuery> categoryQueries;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "analysis_id", referencedColumnName = "analysis_id")
  @OrderBy("id")
  @Fetch(SUBSELECT)
  @Singular
  private List<AnalysisFeatureQuery> featureQueries;

  Analysis toAnalysis() {
    var builder = analysis.updateBuilder(Analysis.newBuilder())
        .setAlertCount(alertCount)
        .setPendingAlerts(pendingAlerts);

    builder.addAllCategories(
        categoryQueries.stream().map(AnalysisCategoryQuery::getName).collect(toUnmodifiableList()));
    builder.addAllFeatures(
        featureQueries.stream().map(AnalysisFeatureQuery::toFeature).collect(toUnmodifiableList()));

    return builder.build();
  }
}
