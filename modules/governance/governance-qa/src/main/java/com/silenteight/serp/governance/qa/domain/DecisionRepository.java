package com.silenteight.serp.governance.qa.domain;

import com.silenteight.serp.governance.qa.analysis.details.dto.AlertAnalysisDetailsDto;
import com.silenteight.serp.governance.qa.analysis.list.dto.AlertAnalysisDto;
import com.silenteight.serp.governance.qa.validation.details.dto.AlertValidationDetailsDto;
import com.silenteight.serp.governance.qa.validation.list.dto.AlertValidationDto;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

interface DecisionRepository extends Repository<Decision, Long> {
  String FIND_ALL_BY_LEVEL_AND_STATES_NEWER_THAN_QUERY_VALUE = ""
      + "SELECT a.alert_name as alertName, d.state AS state, d.decided_at AS decisionAt, "
      + "d.decided_by AS decisionBy, d.comment AS decisionComment, a.created_at AS addedAt "
      + "FROM governance_qa_decision AS d "
      + "JOIN governance_qa_alert AS a ON a.id = d.alert_id "
      + "WHERE d.state IN (:states) AND d.level = :level AND a.created_at > :createdAt "
      + "ORDER BY a.created_at ASC "
      + "LIMIT :limit";

  String FIND_DETAILS_QUERY_VALUE = ""
      + "SELECT a.alertName AS alertName, d.state AS state, "
      + "d.decidedAt AS decisionAt, d.decidedBy AS decisionBy, d.comment AS decisionComment, "
      + "a.createdAt AS addedAt "
      + "FROM Decision AS d "
      + "JOIN Alert AS a ON a.id = d.alertId AND a.alertName = :name "
      + "WHERE d.level = :level";

  @Query("SELECT d FROM Decision AS d "
      + "JOIN Alert AS a ON a.id = d.alertId AND a.alertName = :alertName "
      + "WHERE d.level = :level")
  Optional<Decision> findByAlertNameAndLevel(String alertName, Integer level);

  Decision save(Decision decision);

  @Query(value = "SELECT count(d) AS size "
      + "FROM Decision AS d "
      + "JOIN Alert AS a ON a.id = d.alertId "
      + "WHERE d.state IN (:states) AND d.level = :level")
  int countAllByLevelAndStates(Integer level, List<DecisionState> states);

  @Query(value = FIND_DETAILS_QUERY_VALUE)
  AlertAnalysisDetailsDto findAnalysisDetails(String name, Integer level);

  @Query(value =  FIND_ALL_BY_LEVEL_AND_STATES_NEWER_THAN_QUERY_VALUE, nativeQuery = true)
  List<AlertAnalysisDto> findAllAnalysisByStatesNewerThan(Integer level, List<String> states,
      OffsetDateTime createdAt, Integer limit);

  @Query(value = FIND_ALL_BY_LEVEL_AND_STATES_NEWER_THAN_QUERY_VALUE, nativeQuery = true)
  List<AlertValidationDto> findAllValidationByStatesNewerThan(Integer level, List<String> states,
      OffsetDateTime createdAt, Integer limit);

  @Query(value = FIND_DETAILS_QUERY_VALUE)
  AlertValidationDetailsDto findValidationDetails(String name, Integer level);
}
