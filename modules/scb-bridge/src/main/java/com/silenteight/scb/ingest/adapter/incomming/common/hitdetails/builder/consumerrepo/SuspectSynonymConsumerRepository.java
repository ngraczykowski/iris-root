package com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.builder.consumerrepo;

import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Synonym;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class SuspectSynonymConsumerRepository
    implements ConsumerRepository<Suspect, Synonym> {

  private final MappingConsumerRepository<Suspect, Synonym> mappingRepository =
      new MappingConsumerRepository<>();

  public SuspectSynonymConsumerRepository() {
    register("NAME", Suspect::getNameSynonyms);
    register("ADDRESS", Suspect::getAddressSynonyms);
    register("CITY", Suspect::getCitySynonyms);
    register("COUNTRY", Suspect::getCountrySynonyms);
    register("STATE", Suspect::getStateSynonyms);
  }

  private void register(String key, Function<Suspect, List<Synonym>> function) {
    mappingRepository.register(key, (sus, syn) -> function.apply(sus).add(syn));
  }

  @Override
  public Optional<BiConsumer<Suspect, Synonym>> find(String key) {
    return mappingRepository.find(key);
  }
}
