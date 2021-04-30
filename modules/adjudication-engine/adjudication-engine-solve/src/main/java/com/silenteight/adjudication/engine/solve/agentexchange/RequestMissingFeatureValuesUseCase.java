package com.silenteight.adjudication.engine.solve.agentexchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
TODO tkleszcz:
      // spring integration gateway
      // agentRequestGateway.sendRequest();
      // gateway wysyla na kanal inbound
      // z kanalu pobieranie i handlowanie
      // ScreeningEngineFacade
      // 1. tworzenie AgentExchangeEntity.
      // 2. dodanie domain event AgentExchangeRequest
      // 3. dodać @TransactionalEventListener na eventa i wyslac do gatewaya
      // @TransactionalEventListener
 */
@Service
@Slf4j
@RequiredArgsConstructor
class RequestMissingFeatureValuesUseCase {
  //
  //  private final MissingFeatureValueQueryRepository missingFeatureValueQueryRepository;
  //  private final MissingFeatureValueMatchesQueryRepository
  //      missingFeatureValueMatchesQueryRepository;
  //  private final AgentExchangeRepository agentExchangeRepository;
  //  private final RabbitTemplate rabbitTemplate;
  //  private final SolveProperties solveProperties;
  //  private final AgentRequestGateway agentRequestGateway;

  /*
  for (feature, agentConfig, priority in (grouped by feature, agentConfig, priority) {
    for (matchId in findMatchIds(feature, agentConfig, priority)) {

    }
  }
   */

  @Transactional
  public void invoke() {
    //    Stream<MissingFeatureValueQueryEntity> s =
    //        missingFeatureValueQueryRepository.findAllByOrderByIdPriorityDesc();
    //    s.forEachOrdered(e -> Iterators.partition(
    //        missingFeatureValueMatchesQueryRepository
    //            .findByIdFeatureAndIdAgentConfigAndIdPriority(
    //                e.getId().getFeature(), e.getId().getAgentConfig(), e.getId().getPriority())
    //            .iterator(),
    //        solveProperties.getAgentRequestChunkSize()).forEachRemaining(this::process));
  }

  //  private void process(List<MissingFeatureValueMatchesQueryEntity> list) {
  //    log.info("Marking missing feature value: {} as requested", list);
  //    AgentExchange agentExchange;
  //    try {
  //      agentExchange = markAsRequested(list);
  //    } catch (DataIntegrityViolationException e) {
  //      if (e.getCause() instanceof ConstraintViolationException) {
  //        if (((ConstraintViolationException) e.getCause()).getConstraintName()
  //            .equals("uq_ae_agent_exchange_match_features_feature_agent_config_match_")) {
  //          log.info("Skipping executing agent requests for this chunk because one of the"
  //              + " Agent Request was submitted concurrently in a different transaction in"
  //              + " the same time: {}", e.getMessage());
  //          return;
  //        }
  //      }
  //      throw e;
  //    }
  //    log.info(
  //        "Requesting missing feature value: {}, correlation id: {}", list,
  //        agentExchange.getId());
  //    sendAgentRequest(agentExchange);
  //  }

  //  @SneakyThrows
  //  @Transactional(propagation = Propagation.REQUIRES_NEW)
  //  private AgentExchange markAsRequested(
  //      String feature, String agentConfig, List<MissingFeatureValueMatchesQueryEntity> list) {
  //    var r = agentExchangeRepository.save(AgentExchange
  //        .builder()
  //        .feature()
  //        .matchFeatures(list.stream().map(e -> AgentExchangeMatchFeature
  //            .builder()
  //            .matchId(e.getId()
  //                .getMatchId())
  //            .feature(e.getId().getFeature())
  //            .agentConfig(e.getId().getAgentConfig())
  //            .build()).collect(Collectors.toList()))
  //        .build());
  //    // todo removesimulate sleep to test concurrency
  //    Thread.sleep(10000);
  //    return r;
  //  }

  //  public void sendAgentRequest(AgentExchange e) {
  //    AgentExchangeRequest r = AgentExchangeRequest
  //        .newBuilder()
  //        .addFeatures(e.getMatchFeatures().get(0).getFeature())
  //        .addAllMatches(e
  //            .getMatchFeatures()
  //            .stream()
  //            .map(AgentExchangeMatchFeature::getMatchId)
  //            .map(String::valueOf)
  //            .collect(Collectors.toList()))
  //        .build();
  //    log.info("Submitting agent request: {}", r);
  //    // rabbitTemplate.convertAndSend(solveProperties.getAgentExchange(),
  //    //     e.getMatchFeatures().get(0).getAgentConfig(),
  //    //     r, new CorrelationData(e.getId().toString()));
  //    agentRequestGateway.sendRequest(r);
  //  }
}
