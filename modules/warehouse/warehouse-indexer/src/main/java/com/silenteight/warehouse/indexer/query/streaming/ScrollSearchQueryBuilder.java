package com.silenteight.warehouse.indexer.query.streaming;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.common.QueryFilter;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;

import java.util.List;

import static com.silenteight.warehouse.common.opendistro.utils.OpendistroUtils.getRawField;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

@RequiredArgsConstructor
class ScrollSearchQueryBuilder {

  QueryBuilder buildQuery(FetchDataRequest request) {
    List<QueryFilter> queryFilters = request.getQueryFilters();
    BoolQueryBuilder query = boolQuery();
    queryFilters.forEach(queryFilter -> query.must(buildTermsQuery(queryFilter)));
    query.must(buildRangeQuery(request));
    return query;
  }

  private static TermsQueryBuilder buildTermsQuery(QueryFilter queryFilters) {
    return termsQuery(getRawField(queryFilters.getField()), queryFilters.getAllowedValues());
  }

  private static RangeQueryBuilder buildRangeQuery(FetchDataRequest request) {
    return rangeQuery(request.getDateField()).gte(request.getFrom()).lt(request.getTo());
  }
}
