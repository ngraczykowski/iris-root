package com.silenteight.serp.governance.vector.domain;

import com.silenteight.serp.governance.common.signature.SignatureConverter;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class FeatureVectorCustomRepositoryImpl implements FeatureVectorCustomRepository {

  @PersistenceContext
  private EntityManager entityManager;

  private final SignatureConverter signatureConverter;
  private final CommaSeparatedListConverter commaSeparatedListConverter;

  @Autowired
  public FeatureVectorCustomRepositoryImpl(
      SignatureConverter signatureConverter,
      CommaSeparatedListConverter commaSeparatedListConverter) {
    this.signatureConverter = signatureConverter;
    this.commaSeparatedListConverter = commaSeparatedListConverter;
  }

  @Override
  public void saveIfNotExist(FeatureVector featureVector) {
    String names = commaSeparatedListConverter.convertToDatabaseColumn(featureVector.getNames());
    String values = commaSeparatedListConverter.convertToDatabaseColumn(featureVector.getValues());
    String signature =
        signatureConverter.convertToDatabaseColumn(featureVector.getVectorSignature());

    createQuery(names, values, signature, featureVector.getCreatedAt())
        .executeUpdate();
  }

  private Query createQuery(
      String names,
      String values,
      String signature,
      OffsetDateTime createdAt) {
    return entityManager.createNativeQuery("INSERT"
            + " INTO governance_analytics_feature_vector"
            + " (names, values, vector_signature, created_at)"
            + " VALUES (:names, :values, :signature, :createdAt)"
            + " ON CONFLICT(vector_signature) DO NOTHING")
        .setParameter("names", names)
        .setParameter("values", values)
        .setParameter("signature", signature)
        .setParameter("createdAt", createdAt);
  }
}
