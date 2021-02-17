package com.silenteight.hsbc.bridge.rest.model.input;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;

/**
 * AlertSystemInformation
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-02-17T10:41:43.102Z[GMT]")


public class AlertSystemInformation   {
  @JsonProperty("countryCTRPScreeningIndividuals")
  private CountryCTRPScreeningIndividuals countryCTRPScreeningIndividuals = null;

  @JsonProperty("customerEntities")
  private CustomerEntities customerEntities = null;

  @JsonProperty("worldCheckEntities")
  private WorldCheckEntities worldCheckEntities = null;

  @JsonProperty("customerIndividuals")
  private CustomerIndividuals customerIndividuals = null;

  @JsonProperty("worldCheckIndividuals")
  private WorldCheckIndividuals worldCheckIndividuals = null;

  @JsonProperty("caseComments")
  private CaseComments caseComments = null;

  @JsonProperty("relationships")
  private Relationships relationships = null;

  @JsonProperty("privateListIndividuals")
  private PrivateListIndividuals privateListIndividuals = null;

  @JsonProperty("caseAttachments")
  private CaseAttachments caseAttachments = null;

  @JsonProperty("privateListEntities")
  private PrivateListEntities privateListEntities = null;

  @JsonProperty("caseHistory")
  private CaseHistory caseHistory = null;

  @JsonProperty("countryCTRPScreeningEntities")
  private CountryCTRPScreeningEntities countryCTRPScreeningEntities = null;

  @JsonProperty("casesWithAlertURL")
  private CasesWithAlertURL casesWithAlertURL = null;

  public AlertSystemInformation countryCTRPScreeningIndividuals(CountryCTRPScreeningIndividuals countryCTRPScreeningIndividuals) {
    this.countryCTRPScreeningIndividuals = countryCTRPScreeningIndividuals;
    return this;
  }

  /**
   * Get countryCTRPScreeningIndividuals
   * @return countryCTRPScreeningIndividuals
   **/
  @Schema(description = "")
  
    @Valid
    public CountryCTRPScreeningIndividuals getCountryCTRPScreeningIndividuals() {
    return countryCTRPScreeningIndividuals;
  }

  public void setCountryCTRPScreeningIndividuals(CountryCTRPScreeningIndividuals countryCTRPScreeningIndividuals) {
    this.countryCTRPScreeningIndividuals = countryCTRPScreeningIndividuals;
  }

  public AlertSystemInformation customerEntities(CustomerEntities customerEntities) {
    this.customerEntities = customerEntities;
    return this;
  }

  /**
   * Get customerEntities
   * @return customerEntities
   **/
  @Schema(description = "")
  
    @Valid
    public CustomerEntities getCustomerEntities() {
    return customerEntities;
  }

  public void setCustomerEntities(CustomerEntities customerEntities) {
    this.customerEntities = customerEntities;
  }

  public AlertSystemInformation worldCheckEntities(WorldCheckEntities worldCheckEntities) {
    this.worldCheckEntities = worldCheckEntities;
    return this;
  }

  /**
   * Get worldCheckEntities
   * @return worldCheckEntities
   **/
  @Schema(description = "")
  
    @Valid
    public WorldCheckEntities getWorldCheckEntities() {
    return worldCheckEntities;
  }

  public void setWorldCheckEntities(WorldCheckEntities worldCheckEntities) {
    this.worldCheckEntities = worldCheckEntities;
  }

  public AlertSystemInformation customerIndividuals(CustomerIndividuals customerIndividuals) {
    this.customerIndividuals = customerIndividuals;
    return this;
  }

  /**
   * Get customerIndividuals
   * @return customerIndividuals
   **/
  @Schema(description = "")
  
    @Valid
    public CustomerIndividuals getCustomerIndividuals() {
    return customerIndividuals;
  }

  public void setCustomerIndividuals(CustomerIndividuals customerIndividuals) {
    this.customerIndividuals = customerIndividuals;
  }

  public AlertSystemInformation worldCheckIndividuals(WorldCheckIndividuals worldCheckIndividuals) {
    this.worldCheckIndividuals = worldCheckIndividuals;
    return this;
  }

  /**
   * Get worldCheckIndividuals
   * @return worldCheckIndividuals
   **/
  @Schema(description = "")
  
    @Valid
    public WorldCheckIndividuals getWorldCheckIndividuals() {
    return worldCheckIndividuals;
  }

  public void setWorldCheckIndividuals(WorldCheckIndividuals worldCheckIndividuals) {
    this.worldCheckIndividuals = worldCheckIndividuals;
  }

  public AlertSystemInformation caseComments(CaseComments caseComments) {
    this.caseComments = caseComments;
    return this;
  }

  /**
   * Get caseComments
   * @return caseComments
   **/
  @Schema(description = "")
  
    @Valid
    public CaseComments getCaseComments() {
    return caseComments;
  }

  public void setCaseComments(CaseComments caseComments) {
    this.caseComments = caseComments;
  }

  public AlertSystemInformation relationships(Relationships relationships) {
    this.relationships = relationships;
    return this;
  }

  /**
   * Get relationships
   * @return relationships
   **/
  @Schema(description = "")
  
    @Valid
    public Relationships getRelationships() {
    return relationships;
  }

  public void setRelationships(Relationships relationships) {
    this.relationships = relationships;
  }

  public AlertSystemInformation privateListIndividuals(PrivateListIndividuals privateListIndividuals) {
    this.privateListIndividuals = privateListIndividuals;
    return this;
  }

  /**
   * Get privateListIndividuals
   * @return privateListIndividuals
   **/
  @Schema(description = "")
  
    @Valid
    public PrivateListIndividuals getPrivateListIndividuals() {
    return privateListIndividuals;
  }

  public void setPrivateListIndividuals(PrivateListIndividuals privateListIndividuals) {
    this.privateListIndividuals = privateListIndividuals;
  }

  public AlertSystemInformation caseAttachments(CaseAttachments caseAttachments) {
    this.caseAttachments = caseAttachments;
    return this;
  }

  /**
   * Get caseAttachments
   * @return caseAttachments
   **/
  @Schema(description = "")
  
    @Valid
    public CaseAttachments getCaseAttachments() {
    return caseAttachments;
  }

  public void setCaseAttachments(CaseAttachments caseAttachments) {
    this.caseAttachments = caseAttachments;
  }

  public AlertSystemInformation privateListEntities(PrivateListEntities privateListEntities) {
    this.privateListEntities = privateListEntities;
    return this;
  }

  /**
   * Get privateListEntities
   * @return privateListEntities
   **/
  @Schema(description = "")
  
    @Valid
    public PrivateListEntities getPrivateListEntities() {
    return privateListEntities;
  }

  public void setPrivateListEntities(PrivateListEntities privateListEntities) {
    this.privateListEntities = privateListEntities;
  }

  public AlertSystemInformation caseHistory(CaseHistory caseHistory) {
    this.caseHistory = caseHistory;
    return this;
  }

  /**
   * Get caseHistory
   * @return caseHistory
   **/
  @Schema(description = "")
  
    @Valid
    public CaseHistory getCaseHistory() {
    return caseHistory;
  }

  public void setCaseHistory(CaseHistory caseHistory) {
    this.caseHistory = caseHistory;
  }

  public AlertSystemInformation countryCTRPScreeningEntities(CountryCTRPScreeningEntities countryCTRPScreeningEntities) {
    this.countryCTRPScreeningEntities = countryCTRPScreeningEntities;
    return this;
  }

  /**
   * Get countryCTRPScreeningEntities
   * @return countryCTRPScreeningEntities
   **/
  @Schema(description = "")
  
    @Valid
    public CountryCTRPScreeningEntities getCountryCTRPScreeningEntities() {
    return countryCTRPScreeningEntities;
  }

  public void setCountryCTRPScreeningEntities(CountryCTRPScreeningEntities countryCTRPScreeningEntities) {
    this.countryCTRPScreeningEntities = countryCTRPScreeningEntities;
  }

  public AlertSystemInformation casesWithAlertURL(CasesWithAlertURL casesWithAlertURL) {
    this.casesWithAlertURL = casesWithAlertURL;
    return this;
  }

  /**
   * Get casesWithAlertURL
   * @return casesWithAlertURL
   **/
  @Schema(description = "")
  
    @Valid
    public CasesWithAlertURL getCasesWithAlertURL() {
    return casesWithAlertURL;
  }

  public void setCasesWithAlertURL(CasesWithAlertURL casesWithAlertURL) {
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
    return Objects.equals(this.countryCTRPScreeningIndividuals, alertSystemInformation.countryCTRPScreeningIndividuals) &&
        Objects.equals(this.customerEntities, alertSystemInformation.customerEntities) &&
        Objects.equals(this.worldCheckEntities, alertSystemInformation.worldCheckEntities) &&
        Objects.equals(this.customerIndividuals, alertSystemInformation.customerIndividuals) &&
        Objects.equals(this.worldCheckIndividuals, alertSystemInformation.worldCheckIndividuals) &&
        Objects.equals(this.caseComments, alertSystemInformation.caseComments) &&
        Objects.equals(this.relationships, alertSystemInformation.relationships) &&
        Objects.equals(this.privateListIndividuals, alertSystemInformation.privateListIndividuals) &&
        Objects.equals(this.caseAttachments, alertSystemInformation.caseAttachments) &&
        Objects.equals(this.privateListEntities, alertSystemInformation.privateListEntities) &&
        Objects.equals(this.caseHistory, alertSystemInformation.caseHistory) &&
        Objects.equals(this.countryCTRPScreeningEntities, alertSystemInformation.countryCTRPScreeningEntities) &&
        Objects.equals(this.casesWithAlertURL, alertSystemInformation.casesWithAlertURL);
  }

  @Override
  public int hashCode() {
    return Objects.hash(countryCTRPScreeningIndividuals, customerEntities, worldCheckEntities, customerIndividuals, worldCheckIndividuals, caseComments, relationships, privateListIndividuals, caseAttachments, privateListEntities, caseHistory, countryCTRPScreeningEntities, casesWithAlertURL);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AlertSystemInformation {\n");
    
    sb.append("    countryCTRPScreeningIndividuals: ").append(toIndentedString(countryCTRPScreeningIndividuals)).append("\n");
    sb.append("    customerEntities: ").append(toIndentedString(customerEntities)).append("\n");
    sb.append("    worldCheckEntities: ").append(toIndentedString(worldCheckEntities)).append("\n");
    sb.append("    customerIndividuals: ").append(toIndentedString(customerIndividuals)).append("\n");
    sb.append("    worldCheckIndividuals: ").append(toIndentedString(worldCheckIndividuals)).append("\n");
    sb.append("    caseComments: ").append(toIndentedString(caseComments)).append("\n");
    sb.append("    relationships: ").append(toIndentedString(relationships)).append("\n");
    sb.append("    privateListIndividuals: ").append(toIndentedString(privateListIndividuals)).append("\n");
    sb.append("    caseAttachments: ").append(toIndentedString(caseAttachments)).append("\n");
    sb.append("    privateListEntities: ").append(toIndentedString(privateListEntities)).append("\n");
    sb.append("    caseHistory: ").append(toIndentedString(caseHistory)).append("\n");
    sb.append("    countryCTRPScreeningEntities: ").append(toIndentedString(countryCTRPScreeningEntities)).append("\n");
    sb.append("    casesWithAlertURL: ").append(toIndentedString(casesWithAlertURL)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
