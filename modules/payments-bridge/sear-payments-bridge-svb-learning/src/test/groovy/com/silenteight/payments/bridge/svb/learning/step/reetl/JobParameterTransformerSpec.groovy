package com.silenteight.payments.bridge.svb.learning.step.reetl

import org.springframework.batch.core.JobParametersBuilder
import spock.lang.Specification

import static com.silenteight.payments.bridge.svb.learning.job.csvstore.LearningJobConstants.FILE_NAME_PARAMETER

class JobParameterTransformerSpec extends Specification {

  def "When feature inputs are indicated"() {
    def expectedParameters = List.of("feature1", "feature2")

    final JobParametersBuilder jobParametersBuilder = new JobParametersBuilder()
        .addString(FILE_NAME_PARAMETER, "filename");
    var featureJobParameters = new JobParametersBuilder();


    for (final String feature : expectedParameters) {
      featureJobParameters.addString("feature-" + feature, feature);
    }

    jobParametersBuilder.addJobParameters(featureJobParameters.toJobParameters());
    var jobParameters = jobParametersBuilder.toJobParameters();



    given:

    def parameters = jobParameters
    when:
    def result = JobParameterTransformer.determineFeatureInputList(parameters)
    then:
    noExceptionThrown()
    result == expectedParameters
  }

}
