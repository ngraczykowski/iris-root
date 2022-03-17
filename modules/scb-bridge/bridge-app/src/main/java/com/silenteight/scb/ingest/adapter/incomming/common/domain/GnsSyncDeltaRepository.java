package com.silenteight.scb.ingest.adapter.incomming.common.domain;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

interface GnsSyncDeltaRepository extends Repository<GnsSyncDelta, String> {

  // NOTE(ahaczewski): Using "projection" and manual query to skip EntityManager 1st level cache.
  //  See: https://stackoverflow.com/a/27106926/72268
  @Query("SELECT "
      + "new com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncDeltaProjection("
      + "    d.alertExternalId,"
      + "    d.watchlistId,"
      + "    d.decisionsCount)"
      + " FROM GnsSyncDelta d"
      + " WHERE d.alertExternalId in :ids"
      + " AND d.deltaJobName = :deltaJobName")
  List<GnsSyncDeltaProjection> findAllByAlertExternalIdInAndDeltaJobName(
      Iterable<String> ids, String deltaJobName);

  @Query("SELECT "
      + "new com.silenteight.scb.ingest.adapter.incomming.common.domain.GnsSyncDeltaProjection("
      + "    d.alertExternalId,"
      + "    d.watchlistId,"
      + "    d.decisionsCount)"
      + " FROM GnsSyncDelta d"
      + " WHERE d.alertExternalId in :ids"
      + " AND d.watchlistId in :watchlistIds"
      + " AND d.deltaJobName = :deltaJobName")
  List<GnsSyncDeltaProjection> findAllByExternalIdInAndDeltaJobName(
      Iterable<String> ids, Iterable<String> watchlistIds, String deltaJobName);

  @Modifying
  @Query("DELETE FROM GnsSyncDelta d"
      + " WHERE d.alertExternalId in :ids"
      + " AND d.deltaJobName = :deltaJobName")
  void deleteAllByAlertExternalIdInAndDeltaJobName(Iterable<String> ids, String deltaJobName);
}
