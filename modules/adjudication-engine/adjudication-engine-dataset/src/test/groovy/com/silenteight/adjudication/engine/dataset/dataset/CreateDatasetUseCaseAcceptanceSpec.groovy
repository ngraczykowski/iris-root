package com.silenteight.adjudication.engine.dataset.dataset

import com.silenteight.adjudication.api.v1.FilteredAlerts
import com.silenteight.adjudication.api.v1.NamedAlerts
import com.silenteight.adjudication.api.v1.NamedAlerts.Builder
import com.silenteight.adjudication.engine.common.model.EntityName

import org.springframework.data.domain.PageRequest
import spock.lang.Specification

import static com.silenteight.adjudication.engine.dataset.dataset.DatasetFixture.inMemoryDatasetFacade

class CreateDatasetUseCaseAcceptanceSpec extends Specification {
  private DatasetFacade facade = inMemoryDatasetFacade()

  def "should create dataset with named alerts"() {
    given:
    def namedAlerts = dataset().build()

    when:
    def createdDataset = facade.createDataset(namedAlerts)

    then:
    validateDataset(createdDataset)

  }

  def "should create dataset with filtered alerts"() {
    given:
    def filteredAlerts = filtered().build()

    when:
    def createdDataset = facade.createDataset(filteredAlerts)

    then:
    validateDatasetIsEmpty(createdDataset)
  }

  def "should get dataset by id"() {
    given:
    def namedAlerts = dataset().build()

    when:
    def createdDataset = facade.createDataset(namedAlerts)
    def foundDataset = facade.getDataset(createdDataset.getName())

    then:
    createdDataset.name == foundDataset.name
    createdDataset.createTime == foundDataset.createTime
    createdDataset.alertCount == foundDataset.alertCount
  }

  def "should get list of datasets"() {
    given:
    def alerts = dataset().build()
    for (int i = 0; i < 100; i++) {
      facade.createDataset(alerts)
    }

    when:
    def firstPage = facade.listDataset(PageRequest.of(0, 10))
    def lastPage = facade.listDataset(PageRequest.of(9, 10))

    then:
    firstPage.getDatasetsCount() == 10
    firstPage.getNextPageToken() == "1"
    lastPage.getNextPageToken() == ""
  }

  def "should get list of dataset alerts"() {
    given:
    def alerts = dataset().build()
    def dataset = facade.createDataset(alerts)

    when:
    def firstPage = facade.listDatasetAlerts(PageRequest.of(0, 1), dataset.getName())
    def lastPage = facade.listDatasetAlerts(PageRequest.of(1, 1), dataset.getName())

    then:
    firstPage.getDatasetAlertNamesCount() == 1
    firstPage.getDatasetAlertNamesList().get(0) == dataset.getName() + "/alerts/1"
    firstPage.getNextPageToken() == "1"
    lastPage.getNextPageToken() == ""
    lastPage.getDatasetAlertNamesList().get(0) == dataset.getName() + "/alerts/2"
  }

  void validateDataset(dataset) {
    assert dataset.alertCount == 2
    assert dataset.name.contains('datasets/')
    assert dataset.createTime != null
  }

  void validateDatasetIsEmpty(dataset) {
    assert dataset.alertCount == 0
    assert dataset.name.contains('datasets/')
    assert dataset.createTime != null
  }

  private static Builder dataset() {
    def list = List.of(
        EntityName.getName("alerts", 1L),
        EntityName.getName("alerts", 2L))
    NamedAlerts
        .newBuilder()
        .addAllAlerts(list)
  }

  private static FilteredAlerts.Builder filtered() {
    FilteredAlerts.newBuilder()
  }
}
