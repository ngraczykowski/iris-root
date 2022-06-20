/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.generator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import javax.validation.Valid;

import static com.google.common.primitives.Ints.constrainToRange;

@UtilityClass
class AlertRecordWithRandomSystemIdMapper {

  private static final Mapper MAPPER = new Mapper();

  AlertRecord mapResultSet(@NonNull ResultSet resultSet) throws SQLException {
    return MAPPER.mapAlertRecordFromDatabase(resultSet);
  }

  @RequiredArgsConstructor
  private static final class Mapper {

    @Valid
    AlertRecord mapAlertRecordFromDatabase(ResultSet resultSet) throws SQLException {
      var charSep = (char) constrainToRange(resultSet.getInt("char_sep"),
          Character.MIN_VALUE, Character.MAX_VALUE);
      var unit = resultSet.getString("unit");

      return AlertRecord.builder()
          .systemId(Mapper.generateNewUniqueSystemId(unit))
          .batchId(resultSet.getString("batch_id"))
          .details(resultSet.getString("details"))
          .lastDecBatchId(resultSet.getString("last_dec_batch_id"))
          .unit(unit)
          .fmtName(resultSet.getString("fmt_name"))
          .dbAccount(resultSet.getString("db_account"))
          .dbCity(resultSet.getString("db_city"))
          .dbCountry(resultSet.getString("db_country"))
          .dbDob(resultSet.getString("db_dob"))
          .dbName(resultSet.getString("db_name"))
          .dbPob(resultSet.getString("db_pob"))
          .filteredString(resultSet.getString("filtered"))
          .recordId(resultSet.getString("record_id"))
          .typeOfRec(resultSet.getString("type_of_rec"))
          .record(resultSet.getString("record"))
          .charSep(charSep)
          .build();
    }

    private static String generateNewUniqueSystemId(String unit) {
      var pseudoUuid = generatePseudoUuid();
      return unit + "!" + pseudoUuid;
    }

    private static String generatePseudoUuid() {
      return UUID.randomUUID()
          .toString()
          .substring(0, 35)
          .toUpperCase();
    }
  }
}
