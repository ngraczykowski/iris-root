package com.silenteight.hsbc.datasource.datamodel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nullable;

import static com.silenteight.hsbc.datasource.datamodel.WatchlistType.CTRPPRHB_LIST_ENTITIES;
import static com.silenteight.hsbc.datasource.datamodel.WatchlistType.PRIVATE_LIST_ENTITIES;
import static com.silenteight.hsbc.datasource.datamodel.WatchlistType.WORLDCHECK_ENTITIES;
import static java.util.Objects.nonNull;

public interface EntityComposite {

  @Nullable
  CustomerEntity getCustomerEntity();

  List<WorldCheckEntity> getWorldCheckEntities();

  List<PrivateListEntity> getPrivateListEntities();

  List<CtrpScreening> getCtrpScreeningEntities();

  default boolean hasWorldCheckEntities() {
    return nonNull(getWorldCheckEntities()) && !getWorldCheckEntities().isEmpty();
  }

  default boolean hasPrivateListEntities() {
    return nonNull(getPrivateListEntities()) && !getPrivateListEntities().isEmpty();
  }

  default boolean hasCtrpScreeningEntities() {
    return nonNull(getCtrpScreeningEntities()) && !getCtrpScreeningEntities().isEmpty();
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
      return Optional.of(WORLDCHECK_ENTITIES);
    } else if (!getPrivateListEntities().isEmpty()) {
      return Optional.of(PRIVATE_LIST_ENTITIES);
    } else if (!getCtrpScreeningEntities().isEmpty()) {
      return Optional.of(CTRPPRHB_LIST_ENTITIES);
    } else
      return Optional.empty();
  }
}
