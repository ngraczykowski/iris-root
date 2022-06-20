package com.silenteight.adjudication.engine.solving.data.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solving.data.AlertAggregate;
import com.silenteight.adjudication.engine.solving.data.FeatureAggregate;
import com.silenteight.adjudication.engine.solving.data.MatchAggregate;
import com.silenteight.adjudication.engine.solving.data.MatchFeatureDataAccess;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
class JdbcMatchFeaturesDataAccess implements MatchFeatureDataAccess {
  private final SelectAnalysisFeaturesQuery selectAnalysisFeaturesQuery;
  private final SelectAnalysisMatchFeaturesQuery selectAnalysisMatchFeaturesQuery;
  private final SelectAnalysisMatchCategoriesQuery selectAnalysisMatchCategoriesQuery;
  private final SelectAnalysisAlertLabelsQuery selectAnalysisAlertLabelsQuery;

  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public Map<Long, AlertAggregate> findAnalysisFeatures(Set<Long> analysis, Set<Long> alerts) {
    return selectAnalysisFeaturesQuery.findAlertAnalysisFeatures(analysis, alerts);
  }

  @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
  public AlertAggregate findAnalysisAlertAndAggregate(Long analysis, Long alert) {
    var matchFeaturesDaos =
        selectAnalysisMatchFeaturesQuery.findAlertMatchFeatures(analysis, alert);
    var matchCategoriesDaos =
        selectAnalysisMatchCategoriesQuery.findAlertMatchCategories(analysis, alert);
    var alertLabels = selectAnalysisAlertLabelsQuery.finalAlertLabels(alert);

    var matches = new HashMap<Long, MatchAggregate>();
    var agentFeatures = new HashMap<String, Set<String>>();

    matchFeaturesDaos.forEach(
        m -> {
          var aggregate = matches.getOrDefault(m.matchId(), createMatchAggregate(m));
          aggregate.features().putIfAbsent(m.featureName(), createFeatureAggregate(m));
          matches.putIfAbsent(m.matchId(), aggregate);

          if (matchCategoriesDaos.containsKey(m.matchId())) {
            var matchCategories = matchCategoriesDaos.get(m.matchId());
            matchCategories.stream()
                .forEach(
                    dao -> aggregate.categories().put(dao.category(), dao.toCategoryAggeregate()));
          }

          var agent = agentFeatures.getOrDefault(m.agentConfig(), new HashSet<>());
          agent.add(m.featureName());
          agentFeatures.putIfAbsent(m.agentConfig(), agent);
        });

    var policyAndStrategy = matchFeaturesDaos.stream().findAny();

    if (policyAndStrategy.isEmpty()) {
      throw new NoMatchFeatureFound("MatchFeaturesDao objects found for alert");
    }

    return AlertAggregate.builder()
        .analysisId(analysis)
        .matches(matches)
        .alertId(alert)
        .priority(policyAndStrategy.get().priority())
        .labels(alertLabels)
        .agentFeatures(agentFeatures)
        .strategy(policyAndStrategy.get().strategy())
        .policy(policyAndStrategy.get().policy())
        .build();
  }

  @NotNull
  private MatchAggregate createMatchAggregate(MatchFeatureDao m) {
    return new MatchAggregate(m.matchId(), m.clientMatchId(), new HashMap<>(), new HashMap<>());
  }

  @NotNull
  private FeatureAggregate createFeatureAggregate(MatchFeatureDao m) {
    return new FeatureAggregate(
        m.featureConfigId(), m.featureName(), m.agentConfig(), m.featureValue(), m.featureReason());
  }
}
