/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@TestConfiguration
@EntityScan(basePackages = "com.silenteight.iris.qco.adapter.outgoing.jpa")
@EnableJpaRepositories(basePackages = "com.silenteight.iris.qco.adapter.outgoing.jpa")
@EnableTransactionManagement
class QcoDbConfiguration {

}
