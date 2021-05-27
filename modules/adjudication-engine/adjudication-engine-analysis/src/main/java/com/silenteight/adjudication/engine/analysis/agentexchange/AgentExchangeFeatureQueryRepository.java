package com.silenteight.adjudication.engine.analysis.agentexchange;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;
import java.util.stream.Stream;

interface AgentExchangeFeatureQueryRepository extends Repository<AgentExchangeFeatureQuery, Long> {

  Stream<AgentExchangeFeatureQuery> findAllByRequestId(UUID requestId);

  // NOTE(ahaczewski): The method itself turned out to not be necessary because the code just
  //  assumes the request does not exist when the `findAllByRequestId` returns an empty stream.
  //  This method is left here for the possibility to use more sophisticated logic when determining
  //  what to do when we receive a message that we haven't requested.
  //
  //  If no such opportunity arises (i.e., you find this note few months down the road, and the
  //  only users of this method are tests, InMemory implementation and a single IF, then feel free
  //  to remove it.
  //
  //  The real solution shall be to send requests with reply-to header pointing to the right
  //  queue that AE is listening on. Then AE could treat any message received on that queue
  //  as unexpected and error out, instead of silently accepting such a situation.
  @Query(value = "SELECT EXISTS (SELECT 1 FROM ae_agent_exchange WHERE request_id = :request_id)",
      nativeQuery = true)
  boolean agentExchangeRequestExists(@Param("request_id") UUID requestId);
}
