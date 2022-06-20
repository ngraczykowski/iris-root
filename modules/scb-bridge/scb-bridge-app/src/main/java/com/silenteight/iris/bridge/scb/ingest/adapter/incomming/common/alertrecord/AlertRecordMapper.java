/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.validation.Valid;

import static com.google.common.primitives.Ints.constrainToRange;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlertRecordMapper {

  private static final Mapper MAPPER = new Mapper();

  // NOTE(ahaczewski): It is intentional that this method delegates to internal object, as
  //  Bean Validation prohibits validating static methods. Without this, ValidationAspect
  //  fails with an exception.
  //
  //  Note, that it is impossible to prohibit using @Valid on static methods. The code could either:
  //   - silently prohibit from weaving the aspect,
  //   - silently ignore static methods in the aspect,
  //   - log a warning about call to static method.
  //  The choice to throw in such a case was choosen to detect this types of errors as soon
  //  as possible.
  public static AlertRecord mapResultSet(@NonNull ResultSet resultSet) throws SQLException {
    return MAPPER.mapAlertRecordFromDatabase(resultSet);
  }

  @RequiredArgsConstructor
  private static final class Mapper {

    @Valid
    AlertRecord mapAlertRecordFromDatabase(ResultSet resultSet) throws SQLException {
      char charSep = (char) constrainToRange(resultSet.getInt("char_sep"),
          Character.MIN_VALUE, Character.MAX_VALUE);

      return AlertRecord.builder()
          .systemId(resultSet.getString("system_id"))
          .batchId(resultSet.getString("batch_id"))
          .details(resultSet.getString("details"))
          .lastDecBatchId(resultSet.getString("last_dec_batch_id"))
          .unit(resultSet.getString("unit"))
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
  }
}
