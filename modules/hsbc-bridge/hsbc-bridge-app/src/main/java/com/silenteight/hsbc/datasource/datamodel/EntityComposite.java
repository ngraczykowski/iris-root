package com.silenteight.hsbc.datasource.datamodel;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface EntityComposite {

  List<CustomerEntity> getCustomerEntities();

  List<WorldCheckEntity> getWorldCheckEntities();

  List<PrivateListEntity> getPrivateListEntities();

  List<CtrpScreening> getCtrpScreeningEntities();

  List<NegativeNewsScreeningEntities> getNnsEntities();

  default boolean hasWorldCheckEntities() {
    return CollectionUtils.isNotEmpty(getWorldCheckEntities());
  }

  default boolean hasPrivateListEntities() {
    return CollectionUtils.isNotEmpty(getPrivateListEntities());
  }

  default boolean hasCtrpScreeningEntities() {
    return CollectionUtils.isNotEmpty(getCtrpScreeningEntities());
  }

  default boolean hasNnsEntities() {
    return CollectionUtils.isNotEmpty(getNnsEntities());
  }

  default Optional<String> getEntityWatchlistId() {
    var listRecordId =
        Stream.of(
                getWorldCheckEntities().stream(),
                getPrivateListEntities().stream(),
                getNnsEntities().stream())
            .flatMap(stream -> stream)
            .map(ListRecordId::getListRecordId)
            .findFirst();

    return listRecordId.or(this::getCtrpScreeningEntityId);
  }

  private Optional<String> getCtrpScreeningEntityId() {
    return getCtrpScreeningEntities().stream()
        .map(CtrpScreening::getCountryCode)
        .findFirst();
  }

  default Optional<WatchlistType> getEntityWatchlistType() {
    if (!getWorldCheckEntities().isEmpty()) {
      return Optional.of(WatchlistType.WORLDCHECK_ENTITIES);
    } else if (!getPrivateListEntities().isEmpty()) {
      return Optional.of(WatchlistType.PRIVATE_LIST_ENTITIES);
    } else if (!getCtrpScreeningEntities().isEmpty()) {
      return Optional.of(WatchlistType.CTRPPRHB_LIST_ENTITIES);
    } else if (!getNnsEntities().isEmpty()) {
      return Optional.of(WatchlistType.NNS_LIST_ENTITIES);
    } else return Optional.empty();
  }
}
