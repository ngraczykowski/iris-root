package com.silenteight.customerbridge.common.hitdetails.builder;

import lombok.AllArgsConstructor;
import lombok.Data;

import com.silenteight.customerbridge.common.hitdetails.builder.consumerrepo.ConsumerRepository;
import com.silenteight.customerbridge.common.hitdetails.builder.consumerrepo.HitDetailsAttributeConsumerRepository;
import com.silenteight.customerbridge.common.hitdetails.builder.consumerrepo.SuspectAttributeConsumerRepository;
import com.silenteight.customerbridge.common.hitdetails.builder.consumerrepo.SuspectSynonymConsumerRepository;
import com.silenteight.customerbridge.common.hitdetails.model.HitDetails;
import com.silenteight.customerbridge.common.hitdetails.model.Suspect;
import com.silenteight.customerbridge.common.hitdetails.model.Synonym;

@Data
@AllArgsConstructor
public class HitDetailsBuilderConfig {

  private static final ConsumerRepository<HitDetails, String>
      DEFAULT_HIT_DETAILS_ATTR_CONSUMER_REPO = new HitDetailsAttributeConsumerRepository();
  private static final ConsumerRepository<Suspect, String>
      DEFAULT_SUSPECT_ATTR_CONSUMER_REPO = new SuspectAttributeConsumerRepository();
  private static final ConsumerRepository<Suspect, Synonym>
      DEFAULT_SUSPECT_SYNONYM_CONSUMER_REPO = new SuspectSynonymConsumerRepository();

  private final ConsumerRepository<HitDetails, String>
      hitDetailsAttributeConsumerRepo;
  private final ConsumerRepository<Suspect, String>
      suspectAttributeConsumerRepo;
  private final ConsumerRepository<Suspect, Synonym>
      suspectSynonymConsumerRepo;

  public HitDetailsBuilderConfig() {
    this(
        DEFAULT_HIT_DETAILS_ATTR_CONSUMER_REPO,
        DEFAULT_SUSPECT_ATTR_CONSUMER_REPO,
        DEFAULT_SUSPECT_SYNONYM_CONSUMER_REPO);
  }
}
