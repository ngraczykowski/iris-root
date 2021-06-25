package com.silenteight.serp.governance.model.grpc;

import com.silenteight.serp.governance.model.provide.DefaultModelQuery;
import com.silenteight.serp.governance.model.provide.SolvingModelDetailsQuery;
import com.silenteight.serp.governance.model.provide.SolvingModelQuery;
import com.silenteight.serp.governance.model.transfer.export.ExportModelUseCase;
import com.silenteight.serp.governance.model.transfer.importing.ImportModelUseCase;
import com.silenteight.serp.governance.model.use.UseModelUseCase;

import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SolvingModelConfiguration {

  @Bean
  @GrpcService
  SolvingModelGrpcService modelGrpcService(
      DefaultModelQuery defaultModelQuery,
      SolvingModelDetailsQuery modelDetailsQuery,
      SolvingModelQuery solvingModelQuery,
      ExportModelUseCase exportModelUseCase,
      ImportModelUseCase importModelUseCase,
      UseModelUseCase useModelUseCase) {

    return new SolvingModelGrpcService(
        defaultModelQuery,
        modelDetailsQuery,
        solvingModelQuery,
        exportModelUseCase,
        importModelUseCase,
        useModelUseCase);
  }

}
