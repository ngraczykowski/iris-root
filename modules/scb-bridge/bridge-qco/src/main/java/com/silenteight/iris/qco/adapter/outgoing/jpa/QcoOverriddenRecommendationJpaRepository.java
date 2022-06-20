/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.adapter.outgoing.jpa;

import org.springframework.data.repository.CrudRepository;

public interface QcoOverriddenRecommendationJpaRepository
    extends CrudRepository<QcoOverriddenRecommendation, Long> {
}
