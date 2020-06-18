package com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.report;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.report.exception.DecisionTreeNotFoundException;
import com.silenteight.sens.webapp.common.support.csv.CsvBuilder;
import com.silenteight.sens.webapp.common.support.csv.LinesSupplier;
import com.silenteight.sens.webapp.report.Report;
import com.silenteight.sens.webapp.report.ReportGenerator;
import com.silenteight.sens.webapp.report.exception.IllegalParameterException;
import com.silenteight.sep.base.common.time.DateFormatter;
import com.silenteight.sep.base.common.time.TimeSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.stream.Stream.of;

@RequiredArgsConstructor
class ReasoningBranchReportGenerator implements ReportGenerator {

  private static final String REPORT_NAME = "reasoning-branch-report";
  private static final String DECISION_TREE_ID_PARAM = "decisionTreeId";
  private static final String REASONING_BRANCH_ID = "Reasoning branch ID";
  private static final String UPDATED_AT = "Updated at";
  private static final String RECOMMENDATION = "Recommendation";
  private static final String STATUS = "Status";
  private static final String ENABLED = "ENABLED";
  private static final String DISABLED = "DISABLED";

  @NonNull
  private final ReasoningBranchesReportQuery reasoningBranchesByTreeQuery;
  @NonNull
  private final FeatureQuery featureQuery;
  @NonNull
  private final TimeSource timeSource;
  @NonNull
  private final DateFormatter fileNameDateFormatter;
  @NonNull
  private final DateFormatter rowDateFormatter;

  @Override
  public Report generateReport(Map<String, String> parameters) {
    return new ReasoningBranchReport(fileName(), reportData(decisionTreeIdFrom(parameters)));
  }

  private String fileName() {
    return format("%s-%s.csv", REPORT_NAME, formattedCurrentTime());
  }

  private static long decisionTreeIdFrom(Map<String, String> parameters) {
    String decisionTreeId = parameters.get(DECISION_TREE_ID_PARAM);
    if (decisionTreeId == null)
      throw new IllegalParameterException(DECISION_TREE_ID_PARAM + " not provided");

    try {
      return parseLong(decisionTreeId);
    } catch (NumberFormatException e) {
      throw new IllegalParameterException(DECISION_TREE_ID_PARAM + " must be numeric", e);
    }
  }

  private LinesSupplier reportData(long decisionTreeId) {
    List<BranchWithFeaturesDto> branches = branchesOf(decisionTreeId);
    CsvBuilder<BranchWithFeaturesDto> csvBuilder =
        csvWithFixedColumns(decisionTreeId, branches.stream());
    addFeatureColumns(csvBuilder, featureNamesOf(branches));

    return () -> of(csvBuilder.build());
  }

  private List<BranchWithFeaturesDto> branchesOf(long decisionTreeId) {
    try {
      return reasoningBranchesByTreeQuery.findByTreeId(decisionTreeId);
    } catch (DecisionTreeNotFoundException e) {
      throw new IllegalParameterException(DECISION_TREE_ID_PARAM + " unknown", e);
    }
  }

  private CsvBuilder<BranchWithFeaturesDto> csvWithFixedColumns(
      long decisionTreeId, Stream<BranchWithFeaturesDto> branches) {

    return new CsvBuilder<>(branches)
        .cell(REASONING_BRANCH_ID, branch ->
            fullReasoningBranchId(decisionTreeId, branch.getReasoningBranchId()))
        .cell(UPDATED_AT, branch -> rowDateFormatter.format(branch.getUpdatedAt()))
        .cell(RECOMMENDATION, BranchWithFeaturesDto::getAiSolution)
        .cell(STATUS, branch -> branch.isActive() ? ENABLED : DISABLED);
  }

  private String formattedCurrentTime() {
    return fileNameDateFormatter.format(timeSource.now());
  }

  private static CsvBuilder<BranchWithFeaturesDto> addFeatureColumns(
      CsvBuilder<BranchWithFeaturesDto> csvBuilder, List<String> featureNames) {

    int featureIdx = 0;
    for (String featureName : featureNames) {
      final int finalFeatureIdx = featureIdx;
      csvBuilder = csvBuilder.cell(featureName, b -> b.featureValue(finalFeatureIdx));
      featureIdx++;
    }
    return csvBuilder;
  }

  private List<String> featureNamesOf(List<BranchWithFeaturesDto> branches) {
    return branches.isEmpty() ?
           emptyList() :
           featureQuery.findFeaturesNames(branches.get(0).getReasoningBranchId());
  }

  private static String fullReasoningBranchId(long decisionTreeId, long reasoningBranchId) {
    return String.format("%d-%d", decisionTreeId, reasoningBranchId);
  }

  @Override
  public String getName() {
    return REPORT_NAME;
  }
}
