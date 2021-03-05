package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@javax.annotation.Generated(
    value = "io.swagger.codegen.v3.generators.java.JavaResteasyServerCodegen",
    date = "2021-03-05T14:11:51.641Z[GMT]")
public class AlertSystemInformation {

  private List<CountryCtrpScreeningIndividuals> countryCtrpScreeningIndividuals =
      new ArrayList<CountryCtrpScreeningIndividuals>();
  private List<CustomerEntities> customerEntities = new ArrayList<CustomerEntities>();
  private List<WorldCheckEntities> worldCheckEntities = new ArrayList<WorldCheckEntities>();
  private List<CustomerIndividuals> customerIndividuals = new ArrayList<CustomerIndividuals>();
  private List<WorldCheckIndividuals> worldCheckIndividuals =
      new ArrayList<WorldCheckIndividuals>();
  private List<CaseComments> caseComments = new ArrayList<CaseComments>();
  private List<Relationships> relationships = new ArrayList<Relationships>();
  private List<PrivateListIndividuals> privateListIndividuals =
      new ArrayList<PrivateListIndividuals>();
  private List<PrivateListEntities> privateListEntities = new ArrayList<PrivateListEntities>();
  private List<CaseHistory> caseHistory = new ArrayList<CaseHistory>();
  private List<CountryCtrpScreeningEntities> countryCtrpScreeningEntities =
      new ArrayList<CountryCtrpScreeningEntities>();
  private List<CasesWithAlertURL> casesWithAlertURL = new ArrayList<CasesWithAlertURL>();

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("countryCtrpScreeningIndividuals")
  public List<CountryCtrpScreeningIndividuals> getCountryCtrpScreeningIndividuals() {
    return countryCtrpScreeningIndividuals;
  }

  public void setCountryCtrpScreeningIndividuals(
      List<CountryCtrpScreeningIndividuals> countryCtrpScreeningIndividuals) {
    this.countryCtrpScreeningIndividuals = countryCtrpScreeningIndividuals;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("customerEntities")
  public List<CustomerEntities> getCustomerEntities() {
    return customerEntities;
  }

  public void setCustomerEntities(List<CustomerEntities> customerEntities) {
    this.customerEntities = customerEntities;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("worldCheckEntities")
  public List<WorldCheckEntities> getWorldCheckEntities() {
    return worldCheckEntities;
  }

  public void setWorldCheckEntities(List<WorldCheckEntities> worldCheckEntities) {
    this.worldCheckEntities = worldCheckEntities;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("customerIndividuals")
  public List<CustomerIndividuals> getCustomerIndividuals() {
    return customerIndividuals;
  }

  public void setCustomerIndividuals(List<CustomerIndividuals> customerIndividuals) {
    this.customerIndividuals = customerIndividuals;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("worldCheckIndividuals")
  public List<WorldCheckIndividuals> getWorldCheckIndividuals() {
    return worldCheckIndividuals;
  }

  public void setWorldCheckIndividuals(List<WorldCheckIndividuals> worldCheckIndividuals) {
    this.worldCheckIndividuals = worldCheckIndividuals;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("caseComments")
  public List<CaseComments> getCaseComments() {
    return caseComments;
  }

  public void setCaseComments(List<CaseComments> caseComments) {
    this.caseComments = caseComments;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("relationships")
  public List<Relationships> getRelationships() {
    return relationships;
  }

  public void setRelationships(List<Relationships> relationships) {
    this.relationships = relationships;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("privateListIndividuals")
  public List<PrivateListIndividuals> getPrivateListIndividuals() {
    return privateListIndividuals;
  }

  public void setPrivateListIndividuals(List<PrivateListIndividuals> privateListIndividuals) {
    this.privateListIndividuals = privateListIndividuals;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("privateListEntities")
  public List<PrivateListEntities> getPrivateListEntities() {
    return privateListEntities;
  }

  public void setPrivateListEntities(List<PrivateListEntities> privateListEntities) {
    this.privateListEntities = privateListEntities;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("caseHistory")
  public List<CaseHistory> getCaseHistory() {
    return caseHistory;
  }

  public void setCaseHistory(List<CaseHistory> caseHistory) {
    this.caseHistory = caseHistory;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("countryCtrpScreeningEntities")
  public List<CountryCtrpScreeningEntities> getCountryCtrpScreeningEntities() {
    return countryCtrpScreeningEntities;
  }

  public void setCountryCtrpScreeningEntities(
      List<CountryCtrpScreeningEntities> countryCtrpScreeningEntities) {
    this.countryCtrpScreeningEntities = countryCtrpScreeningEntities;
  }

  /**
   *
   **/

  @Schema(description = "")
  @JsonProperty("casesWithAlertURL")
  public List<CasesWithAlertURL> getCasesWithAlertURL() {
    return casesWithAlertURL;
  }

  public void setCasesWithAlertURL(List<CasesWithAlertURL> casesWithAlertURL) {
    this.casesWithAlertURL = casesWithAlertURL;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AlertSystemInformation alertSystemInformation = (AlertSystemInformation) o;
    return Objects.equals(
        countryCtrpScreeningIndividuals,
        alertSystemInformation.countryCtrpScreeningIndividuals) &&
        Objects.equals(customerEntities, alertSystemInformation.customerEntities) &&
        Objects.equals(worldCheckEntities, alertSystemInformation.worldCheckEntities) &&
        Objects.equals(customerIndividuals, alertSystemInformation.customerIndividuals) &&
        Objects.equals(worldCheckIndividuals, alertSystemInformation.worldCheckIndividuals) &&
        Objects.equals(caseComments, alertSystemInformation.caseComments) &&
        Objects.equals(relationships, alertSystemInformation.relationships) &&
        Objects.equals(privateListIndividuals, alertSystemInformation.privateListIndividuals) &&
        Objects.equals(privateListEntities, alertSystemInformation.privateListEntities) &&
        Objects.equals(caseHistory, alertSystemInformation.caseHistory) &&
        Objects.equals(
            countryCtrpScreeningEntities, alertSystemInformation.countryCtrpScreeningEntities) &&
        Objects.equals(casesWithAlertURL, alertSystemInformation.casesWithAlertURL);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        countryCtrpScreeningIndividuals, customerEntities, worldCheckEntities, customerIndividuals,
        worldCheckIndividuals, caseComments, relationships, privateListIndividuals,
        privateListEntities, caseHistory, countryCtrpScreeningEntities, casesWithAlertURL);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AlertSystemInformation {\n");

    sb
        .append("    countryCtrpScreeningIndividuals: ")
        .append(toIndentedString(countryCtrpScreeningIndividuals))
        .append("\n");
    sb.append("    customerEntities: ").append(toIndentedString(customerEntities)).append("\n");
    sb.append("    worldCheckEntities: ").append(toIndentedString(worldCheckEntities)).append("\n");
    sb
        .append("    customerIndividuals: ")
        .append(toIndentedString(customerIndividuals))
        .append("\n");
    sb
        .append("    worldCheckIndividuals: ")
        .append(toIndentedString(worldCheckIndividuals))
        .append("\n");
    sb.append("    caseComments: ").append(toIndentedString(caseComments)).append("\n");
    sb.append("    relationships: ").append(toIndentedString(relationships)).append("\n");
    sb
        .append("    privateListIndividuals: ")
        .append(toIndentedString(privateListIndividuals))
        .append("\n");
    sb
        .append("    privateListEntities: ")
        .append(toIndentedString(privateListEntities))
        .append("\n");
    sb.append("    caseHistory: ").append(toIndentedString(caseHistory)).append("\n");
    sb
        .append("    countryCtrpScreeningEntities: ")
        .append(toIndentedString(countryCtrpScreeningEntities))
        .append("\n");
    sb.append("    casesWithAlertURL: ").append(toIndentedString(casesWithAlertURL)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first
   * line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
