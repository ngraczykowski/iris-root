package com.silenteight.qco

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@TestConfiguration
@EntityScan(basePackages = "com.silenteight.qco.adapter.outgoing.jpa")
@EnableJpaRepositories(basePackages = "com.silenteight.qco.adapter.outgoing.jpa")
@EnableTransactionManagement
class QcoDbConfiguration {

}