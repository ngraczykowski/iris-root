package com.silenteight.serp.governance.branchquery;

import com.silenteight.serp.governance.branchquery.ReasoningBranchQuery.ReasoningBranchQueryId;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.Repository;

interface ReasoningBranchQueryRepository
    extends Repository<ReasoningBranchQuery, ReasoningBranchQueryId>,
    QuerydslPredicateExecutor<ReasoningBranchQuery> {

}
