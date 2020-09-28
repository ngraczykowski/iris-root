package com.silenteight.sens.webapp.backend.reasoningbranch.report;

import com.silenteight.sens.webapp.backend.reasoningbranch.feature.name.FeatureNamesQuery;
import com.silenteight.sens.webapp.backend.reasoningbranch.report.exception.DecisionTreeNotFoundException;
import com.silenteight.sens.webapp.report.Report;
import com.silenteight.sens.webapp.report.exception.IllegalParameterException;
import com.silenteight.sep.base.common.time.DigitsOnlyDateFormatter;
import com.silenteight.sep.base.common.time.IsoOffsetDateFormatter;
import com.silenteight.sep.base.testing.time.MockTimeSource;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.time.Instant.parse;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReasoningBranchReportGeneratorTest {

  private static final String STATIC_PART_OF_THE_HEADER =
      "Decision Tree ID,Feature Vector ID,Updated At,Recommendation,Status";
  @Mock
  private ReasoningBranchesReportQuery reasoningBranchesReportQuery;

  @Mock
  private FeatureNamesQuery featureNamesQuery;

  @Test
  void reportNameContainsTimestamp() {
    Report report = reportGeneratorWithTimeSourceOf("2019-07-03T11:45:22Z")
        .generateReport(mapWithDecisionTreeId(1));

    assertThat(report.getReportFileName()).isEqualTo("reasoning-branch-report-20190703114522.csv");
  }

  @Test
  void generatesEmptyReportWithHeader() {

    Report report = reportGenerator().generateReport(mapWithDecisionTreeId(1));

    List<String> reportLines = linesOf(report);

    assertThat(reportLines).hasSize(1);
    assertThat(reportLines.get(0))
        .isEqualTo(STATIC_PART_OF_THE_HEADER);
  }

  @Test
  void generatesReportWithOneBranch() {
    int treeId = 9;
    int reasoningBranchId = 5;
    when(featureNamesQuery.findFeatureNames(reasoningBranchId)).thenReturn(
        List.of("Feature A", "Feature B"));

    when(reasoningBranchesReportQuery.findByTreeId(treeId)).thenReturn(
        List.of(BranchWithFeaturesDto.builder()
            .reasoningBranchId(reasoningBranchId)
            .updatedAt(Instant.parse("2020-04-15T10:58:53.451667Z"))
            .aiSolution("FALSE_POSITIVE")
            .isActive(false)
            .featureValues(List.of("feat value1", "feat value2"))
            .build()));

    Report report = reportGenerator().generateReport(mapWithDecisionTreeId(treeId));

    List<String> reportLines = linesOf(report);

    assertThat(reportLines).hasSize(2);
    assertThat(reportLines.get(0))
        .isEqualTo(STATIC_PART_OF_THE_HEADER + ",Feature A,Feature B");
    assertThat(reportLines.get(1))
        .isEqualTo(
            "9,5,2020-04-15 10:58:53.451667Z,FALSE_POSITIVE,DISABLED,feat value1,feat value2");
  }

  @Test
  void handlesMissingUpdateAt() {
    int treeId = 9;
    int reasoningBranchId = 5;
    when(featureNamesQuery.findFeatureNames(reasoningBranchId)).thenReturn(emptyList());

    when(reasoningBranchesReportQuery.findByTreeId(treeId)).thenReturn(
        List.of(BranchWithFeaturesDto.builder()
            .reasoningBranchId(reasoningBranchId)
            .createdAt(Instant.parse("2020-04-14T10:25:53.451667Z"))
            .build()));

    Report report = reportGenerator().generateReport(mapWithDecisionTreeId(treeId));

    assertThat(linesOf(report).get(1)).isEqualTo("9,5,2020-04-14 10:25:53.451667Z,,DISABLED");
  }


  @Test
  void handlesMissingUpdateAtAndCreatedAt() {
    int treeId = 9;
    int reasoningBranchId = 5;
    when(featureNamesQuery.findFeatureNames(reasoningBranchId)).thenReturn(emptyList());

    when(reasoningBranchesReportQuery.findByTreeId(treeId)).thenReturn(
        List.of(BranchWithFeaturesDto.builder()
            .reasoningBranchId(reasoningBranchId)
            .build()));

    Report report = reportGenerator().generateReport(mapWithDecisionTreeId(treeId));

    assertThat(linesOf(report).get(1)).isEqualTo("9,5,,,DISABLED");
  }

  @Test
  void generatesReportWithTwoBranches() {
    int treeId = 15;
    int firstReasoningBranchId = 7;
    when(featureNamesQuery.findFeatureNames(anyLong())).thenReturn(
        List.of("Feat A", "Feat B", "Feat C"));
    when(reasoningBranchesReportQuery.findByTreeId(anyLong())).thenReturn(
        List.of(
            BranchWithFeaturesDto.builder()
                .reasoningBranchId(firstReasoningBranchId)
                .updatedAt(Instant.parse("2018-07-12T11:15:31.123456Z"))
                .aiSolution("HINTED_FALSE_POSITIVE")
                .isActive(true)
                .featureValues(List.of("feature value 1", "feature value 2", "feature value 3"))
                .build(),
            BranchWithFeaturesDto.builder()
                .reasoningBranchId(8)
                .updatedAt(Instant.parse("2019-11-30T21:15:00.654321Z"))
                .aiSolution("POTENTIAL_FALSE_POSITIVE")
                .isActive(false)
                .featureValues(List.of("feature value 4", "feature value 5", "feature value 6"))
                .build()));

    Report report = reportGenerator().generateReport(mapWithDecisionTreeId(treeId));

    List<String> reportLines = linesOf(report);

    assertThat(reportLines).hasSize(3);
    assertThat(reportLines.get(0))
        .isEqualTo(STATIC_PART_OF_THE_HEADER + ",Feat A,Feat B,Feat C");
    assertThat(reportLines.get(1)).isEqualTo(
        "15,7,2018-07-12 11:15:31.123456Z,HINTED_FALSE_POSITIVE,ENABLED," +
            "feature value 1,feature value 2,feature value 3");
    assertThat(reportLines.get(2)).isEqualTo(
        "15,8,2019-11-30 21:15:00.654321Z,POTENTIAL_FALSE_POSITIVE,DISABLED," +
            "feature value 4,feature value 5,feature value 6");
  }

  @Test
  void generatesEmptyReportIfQueryReturnsEmptyList() {
    when(reasoningBranchesReportQuery.findByTreeId(anyLong())).thenReturn(emptyList());

    Report report = reportGenerator().generateReport(mapWithDecisionTreeId(1));

    List<String> reportLines = linesOf(report);

    assertThat(reportLines).hasSize(1);
    assertThat(reportLines.get(0))
        .isEqualTo(STATIC_PART_OF_THE_HEADER);
  }

  @Test
  void throwsExceptionIfNoDecisionTreeId() {
    ThrowingCallable generateReportCall = () -> reportGenerator().generateReport(emptyMap());

    assertThatThrownBy(generateReportCall)
        .isInstanceOf(IllegalParameterException.class)
        .hasMessage("decisionTreeId not provided");
  }

  @Test
  void throwsExceptionIfDecisionTreeIdNotNumeric() {
    ThrowingCallable generateReportCall =
        () -> reportGenerator().generateReport(mapWithDecisionTreeId("abc"));

    assertThatThrownBy(generateReportCall)
        .isInstanceOf(IllegalParameterException.class)
        .hasMessage("decisionTreeId must be numeric");
  }

  @Test
  void throwsExceptionIfQueryReturnsException() {
    long treeId = 1234;

    when(reasoningBranchesReportQuery.findByTreeId(treeId)).thenThrow(
        new DecisionTreeNotFoundException());

    assertThatThrownBy(() -> reportGenerator().generateReport(mapWithDecisionTreeId(treeId)))
        .isInstanceOf(IllegalParameterException.class)
        .hasMessage("decisionTreeId unknown");
  }

  private Map<String, String> mapWithDecisionTreeId(Object treeId) {
    return Map.of("decisionTreeId", String.valueOf(treeId));
  }

  private ReasoningBranchReportGenerator reportGenerator() {
    return reportGeneratorWithTimeSourceOf("2020-03-03T00:00:00Z");
  }

  private ReasoningBranchReportGenerator reportGeneratorWithTimeSourceOf(String dateTime) {
    return new ReasoningBranchReportGenerator(
        reasoningBranchesReportQuery, featureNamesQuery,
        new MockTimeSource(parse(dateTime)),
        DigitsOnlyDateFormatter.INSTANCE,
        IsoOffsetDateFormatter.INSTANCE);
  }

  private List<String> linesOf(Report report) {
    Pattern newLinePattern = Pattern.compile("\\n");
    return report
        .getReportContent()
        .lines()
        .flatMap(newLinePattern::splitAsStream)
        .collect(toList());
  }
}
