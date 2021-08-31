package com.silenteight.adjudication.engine.analysis.analysis;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.AnalysisAlert;
import com.silenteight.adjudication.api.v1.AnalysisDataset;
import com.silenteight.adjudication.engine.analysis.analysis.domain.PolicyAndFeatureVectorElements;

import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AnalysisFacade {

  @NonNull
  private final CreateAndGetAnalysisUseCase createAndGetAnalysisUseCase;

  @NonNull
  private final GetAnalysisUseCase getAnalysisUseCase;

  @NonNull
  private final AddAndListDatasetInAnalysisUseCase addAndListDatasetInAnalysisUseCase;

  @NonNull
  private final AddAlertToAnalysisUseCase addAlertToAnalysisUseCase;

  @NonNull
  private final GetAnalysisAgentConfigsUseCase getAnalysisAgentConfigsUseCase;

  @NonNull
  private final FindAnalysisByPendingMatchesUseCase
      findAnalysisByPendingMatchesUseCase;

  @NonNull
  private final GetPolicyAndFeatureVectorElementsUseCase
      getPolicyAndFeatureVectorElementsUseCase;

  @NonNull
  private final GetAnalysisStrategyUseCase getAnalysisStrategyUseCase;

  public Analysis createAndGetAnalysis(Analysis analysis) {
    return createAndGetAnalysisUseCase.createAndGetAnalysis(analysis);
  }

  public List<AnalysisDataset> batchAddAndListDataset(String analysis, List<String> datasets) {
    return addAndListDatasetInAnalysisUseCase.batchAddAndListDataset(analysis, datasets);
  }

  public List<AnalysisAlert> batchAddAlert(String analysis, List<AnalysisAlert> alerts) {
    return addAlertToAnalysisUseCase.batchAddAlert(analysis, alerts);
  }

  public Analysis getAnalysis(String analysisName) {
    return getAnalysisUseCase.getAnalysis(analysisName);
  }

  public List<String> getAgentConfigs(String analysisName) {
    return getAnalysisAgentConfigsUseCase.getAnalysisAgentConfigs(analysisName);
  }

  public List<String> findAnalysisByPendingMatches(List<String> matches) {
    return findAnalysisByPendingMatchesUseCase.findAnalysisByPendingMatches(matches);
  }

  public PolicyAndFeatureVectorElements getAnalysisPolicyAndFeatureVectorElements(long analysisId) {
    return getPolicyAndFeatureVectorElementsUseCase.getPolicyAndFeatureVectorElements(analysisId);
  }

  public String getAnalysisStrategy(long analysisId) {
    return getAnalysisStrategyUseCase.getAnalysisStrategy(analysisId);
  }
}
