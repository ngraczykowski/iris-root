package com.silenteight.qco.infrastructure.parser

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.ResourceLoader
import spock.lang.Specification
import spock.lang.Subject

class SolutionConfigurationCsvFileParserTest extends Specification {

  def resourceLoader = Mock(ResourceLoader)
  def properties = new SolutionConfigurationFileProperties("config/qco_config_file.csv", (char) '|')
  def fileParserConfiguration = new FileParserConfiguration(resourceLoader, properties)
  def resource = new ClassPathResource(properties.location());
  def csvStreamParser =
      new CsvStreamParser(fileParserConfiguration.csvMapper(), properties.separator())

  @Subject
  def underTest = new CsvSolutionConfigurationProvider(csvStreamParser, resource)

  def "GetStatusOverridingConfigurations"() {
    when:
    def result = underTest.getSolutionConfigurations()
    then:
    assert result.size() == 3
    def configResult = result.get(0)
    configResult.getPolicyId() == 'policies/3f1432f4-8828-478e-a6b4-8803ba80be2b'
    configResult.getStepId() == 'steps/5ca9d972-695b-4550-a383-b09311ff42e4'
    configResult.getMatchThreshold() == 500
    configResult.getSolution() == 'FALSE:POSITIVE'
    configResult.getSolutionOverride() == 'Manual:Investigation'
  }
}
