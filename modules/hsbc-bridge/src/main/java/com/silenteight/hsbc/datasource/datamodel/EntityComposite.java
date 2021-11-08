package com.silenteight.hsbc.datasource.datamodel;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public interface EntityComposite {

  List<CustomerEntity> getCustomerEntities();

  List<WorldCheckEntity> getWorldCheckEntities();

  List<PrivateListEntity> getPrivateListEntities();

  List<CtrpScreening> getCtrpScreeningEntities();

  default boolean hasWorldCheckEntities() {
    return Objects.nonNull(getWorldCheckEntities()) && !getWorldCheckEntities().isEmpty();
  }

  default boolean hasPrivateListEntities() {
    return Objects.nonNull(getPrivateListEntities()) && !getPrivateListEntities().isEmpty();
  }

  default boolean hasCtrpScreeningEntities() {
    return Objects.nonNull(getCtrpScreeningEntities()) && !getCtrpScreeningEntities().isEmpty();
  }

  default Optional<String> getEntityWatchlistId() {
    var listRecordId =
        Stream.concat(getWorldCheckEntities().stream(), getPrivateListEntities().stream())
            .map(ListRecordId::getListRecordId).findFirst();

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
    } else
      return Optional.empty();
  }
}
