CREATE SEQUENCE ae_agent_exchange_match_feature_seq;

CREATE TABLE ae_agent_exchange_match_feature
AS (SELECT nextval('ae_agent_exchange_match_feature_seq') as agent_exchange_match_feature_id,
           aae.agent_exchange_id,
           aacf.agent_config_feature_id,
           aaem.match_id,
           aaem.created_at
    FROM ae_agent_exchange aae
             JOIN ae_agent_exchange_match aaem
                  on aae.agent_exchange_id = aaem.agent_exchange_id
             JOIN ae_agent_exchange_feature aaef
                  on aae.agent_exchange_id = aaef.agent_exchange_id
             JOIN ae_agent_config_feature aacf
                  on aae.agent_config = aacf.agent_config and
                     aaef.feature = aacf.feature
);

ALTER SEQUENCE ae_agent_exchange_match_feature_seq
    INCREMENT BY 10;

ALTER TABLE ae_agent_exchange_match_feature
    ADD CONSTRAINT pk_ae_agent_exchange_match_feature
        PRIMARY KEY (agent_exchange_match_feature_id);

ALTER TABLE ae_agent_exchange_match_feature
    ALTER COLUMN agent_exchange_id SET NOT NULL;
ALTER TABLE ae_agent_exchange_match_feature
    ADD CONSTRAINT fk_ae_agent_exchange_match_feature_agent_exchange_id
        FOREIGN KEY (agent_exchange_id) REFERENCES ae_agent_exchange (agent_exchange_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE ae_agent_exchange_match_feature
    ALTER COLUMN agent_config_feature_id SET NOT NULL;
ALTER TABLE ae_agent_exchange_match_feature
    ADD CONSTRAINT fk_ae_agent_exchange_match_feature_agent_config_feature_id
        FOREIGN KEY (agent_config_feature_id) REFERENCES ae_agent_config_feature (agent_config_feature_id);

ALTER TABLE ae_agent_exchange_match_feature
    ALTER COLUMN match_id SET NOT NULL;
ALTER TABLE ae_agent_exchange_match_feature
    ADD CONSTRAINT fk_ae_agent_exchange_match_feature_match_id
        FOREIGN KEY (match_id) REFERENCES ae_match (match_id);

ALTER TABLE ae_agent_exchange_match_feature
    ALTER COLUMN created_at SET NOT NULL;

CREATE INDEX ix_ae_agent_exchange_match_feature_agent_exchange_id
    ON ae_agent_exchange_match_feature (agent_exchange_id);
CREATE INDEX ix_ae_agent_exchange_match_feature_agent_config_feature
    ON ae_agent_exchange_match_feature (agent_config_feature_id);
CREATE INDEX ix_ae_agent_exchange_match_feature_match_id
    ON ae_agent_exchange_match_feature (match_id);
