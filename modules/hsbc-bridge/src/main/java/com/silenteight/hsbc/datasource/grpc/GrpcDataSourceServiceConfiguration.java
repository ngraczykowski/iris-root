package com.silenteight.hsbc.datasource.grpc;

//@Configuration
//@Profile("dev") // Change to @Profile(!dev) when watchlistService will be implemented !!!
//@RequiredArgsConstructor
//@EnableConfigurationProperties({ NameInformationGrpcAdapterProperties.class, })
class GrpcDataSourceServiceConfiguration {

//  private final NameInformationGrpcAdapterProperties nameInformationGrpcAdapterProperties;
//
//  @Bean
//  NameInformationServiceClient nameInformationServiceGrpcApi() {
//    return new NameInformationGrpcAdapter(
//        namesInformationServiceBlockingStub(),
//        nameInformationGrpcAdapterProperties.getDeadlineInSeconds());
//  }
//
//  private NamesInformationServiceBlockingStub namesInformationServiceBlockingStub() {
//    return NamesInformationServiceGrpc.newBlockingStub(
//        getManagedChannel(nameInformationGrpcAdapterProperties.getGrpcAddress()))
//        .withWaitForReady();
//  }
//
//  private ManagedChannel getManagedChannel(String address) {
//    return ManagedChannelBuilder.forTarget(address)
//        .usePlaintext()
//        .build();
//  }
}
