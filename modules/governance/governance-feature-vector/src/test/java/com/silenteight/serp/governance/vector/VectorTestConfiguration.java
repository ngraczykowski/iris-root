package com.silenteight.serp.governance.vector;

import com.silenteight.serp.governance.common.signature.SignatureModule;
import com.silenteight.serp.governance.policy.domain.InUsePolicyQuery;
import com.silenteight.serp.governance.policy.domain.PolicyByIdQuery;
import com.silenteight.serp.governance.policy.solve.StepsSupplierProvider;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
    FeatureVectorModule.class,
    SignatureModule.class
})
public class VectorTestConfiguration {

  @MockBean
  PolicyStepsRequestQuery policyStepsRequestQuery;

  @MockBean
  PolicyByIdQuery policyByIdQuery;

  @MockBean
  InUsePolicyQuery inUsePolicyQuery;

  @MockBean
  StepsSupplierProvider stepsSupplierProvider;
}
