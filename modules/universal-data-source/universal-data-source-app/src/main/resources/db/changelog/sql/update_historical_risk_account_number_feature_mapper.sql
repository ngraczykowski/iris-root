DELETE FROM uds_feature_mapper
WHERE feature_name =  'features/historicalRiskAccountNumber';

INSERT INTO uds_feature_mapper(feature_name, mapped_feature_name)
VALUES ('features/historicalRiskAccountNumberTP', 'features/historicalRiskAccountNumberTP'),
       ('features/historicalRiskAccountNumberFP', 'features/historicalRiskAccountNumberFP');
