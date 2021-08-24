package com.silenteight.warehouse.indexer.query.index;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.opendistro.elastic.OpendistroElasticClient;
import com.silenteight.warehouse.common.opendistro.elastic.QueryDto;
import com.silenteight.warehouse.common.opendistro.elastic.QueryResultDto;
import com.silenteight.warehouse.common.opendistro.elastic.QueryResultDto.SchemaEntry;
import com.silenteight.warehouse.indexer.query.sql.SqlBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class QueryIndexService {

  private static final String COLUMN_NAME_KEY = "COLUMN_NAME";

  @NonNull
  private final SqlBuilder sqlBuilder;

  @NonNull
  private final OpendistroElasticClient opendistroElasticClient;

  public List<String> getFieldsList(String indexName) {
    String describeTableQuery = sqlBuilder.describeTables(indexName);

    QueryDto queryDto = QueryDto.builder()
        .query(describeTableQuery)
        .build();

    QueryResultDto queryResultDto = opendistroElasticClient.executeSql(queryDto);
    int columnNameIndex = queryResultDto.getSchema().indexOf(new SchemaEntry(COLUMN_NAME_KEY));
    if (columnNameIndex < 0) {
      throw new RuntimeException(
          "Error while discovering index mapping. Column not found: " + COLUMN_NAME_KEY);
    }

    return queryResultDto.getDatarows().stream()
        .map(dataRow -> dataRow.get(columnNameIndex))
        .map(Object::toString)
        .collect(Collectors.toList());
  }
}
