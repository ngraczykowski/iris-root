package com.silenteight.qco.adapter.outgoing.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.qco.domain.model.MatchSolution;
import com.silenteight.qco.domain.model.QcoRecommendationMatch;
import com.silenteight.qco.domain.port.outgoing.QcoOverriddenRecommendationService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.silenteight.qco.adapter.outgoing.jpa.QcoOverriddenRecommendationMapper.toQcoOverriddenRecommendation;

@Slf4j
@Service
@RequiredArgsConstructor
class QcoOverriddenRecommendationDefaultService implements QcoOverriddenRecommendationService {

  private final QcoOverriddenRecommendationJpaRepository recommendationRepository;

  @Transactional("transactionManager")
  @Override
  public void storeQcoOverriddenRecommendation(
      QcoRecommendationMatch match, MatchSolution matchSolution) {
    recommendationRepository.save(toQcoOverriddenRecommendation(match, matchSolution.solution()));
    log.info("The processed match={} has been stored into DB", match.matchName());
  }
}
