import os
import logging
import time


from pipeline.config import CLEANSED_DATA_DIR, in_cleansed_data_dir, RAW_DATA_DIR, STANDARDIZED_DATA_DIR, in_raw_data_dir, in_standardized_data_dir
import re
from pipeline.spark import spark_instance
from delta.tables import *
from silenteight.aia.alerts import AlertHitDictFactory, AlertHitExtractor
from pyspark.sql.types import StructType, ArrayType, StructField, StringType, MapType
import pyspark.sql.functions as F
from silenteight.data.spark import preprocess

import helper.dbhelper as dbhelper

# IMPLEMENTATION: DeltaConverter
def convert_to_standardized(raw_data_path=RAW_DATA_DIR, target_path=STANDARDIZED_DATA_DIR):
    for file_name in os.listdir(raw_data_path):
        start = time.time()
        raw_file_path = in_raw_data_dir(file_name)
        logging.info(f'Start to process {raw_file_path}')
        
        standardized_file_name = re.sub('.csv$', '.delta', file_name)
        standardized_file_path = in_standardized_data_dir(standardized_file_name)
        df = spark_instance.spark_read_csv(raw_file_path)
        df = preprocess.write_read_delta(spark_instance.spark_instance, df, standardized_file_path)
        
        logging.info(f'Data saved to {standardized_file_path}')
        logging.info(f'Time lapsed {time.time() - start:.2f} s')
        
        print()
        time.sleep(1)






def convert_standardized_to_cleansed():

    # Implementation: XML Parser
    alert_hit_dict_factory = AlertHitDictFactory()
    alert_hit_extractor = AlertHitExtractor()

    # Implementation: XML Parser
    alert_schema = alert_hit_dict_factory.get_alert_spark_schema()
    hit_schema = alert_hit_dict_factory.get_hit_spark_schema()

    # Implementation: Spark manager
    schema = StructType([
        StructField('alert_header', alert_schema),
        StructField('hits', ArrayType(hit_schema))
    ])

    # file_name = 'RCMDB.ALERTS_SAMPLE.delta'
    # Implementation: Spark manager
    file_name = 'ALERTS.delta'
    std_alert_df = spark_instance.read_delta("data/2.standardized/" + (file_name))

    # Implementation: Spark manager
    spark_instance.show_dim(std_alert_df)

    # Implementation: Spark manager, XML Parser

    ## Unwrap xmls
    alert_df = std_alert_df.withColumn('alert_hits',
                                    F.udf(alert_hit_extractor.extract_alert_hits_from_xml, schema)('html_file_key'))

    # Implementation: Delta Converter

    alert_df = spark_instance.write_and_get_delta_data(alert_df, in_cleansed_data_dir(file_name))


    # Create columns from hits
    # Implementation: Spark manager, XML Parser, Match/Hit Handler
    alert_hits_df = alert_df.selectExpr('*', 'alert_hits.*') \
        .selectExpr('*', 'explode(hits) as hit') \
        .selectExpr('*', 'alert_header.*') \
        .selectExpr('*', 'hit.*') \
        .drop('alert_hits', 'alert_header', 'hits', 'hit')

    # Implementation: Delta Exporter
    alert_hits_df = spark_instance.write_and_get_delta_data(alert_hits_df,
                                    in_cleansed_data_dir(file_name),
                                    user_metadata='At hit level'
                                    )


    # Implementation: XML Parser, Match/Hit Handler
    def get_wl_hit_aliases_matched_name(hit_aliases_displayName, hit_aliases_matchedName, hit_inputExplanations):
        if hit_inputExplanations is None or len(hit_inputExplanations) == 0:
            return []
        else:
            result = []
            hit_inputExplanations = list(set(hit_inputExplanations))
            for hit_inputExplanation in hit_inputExplanations:
                if hit_inputExplanation in hit_aliases_matchedName:
                    index_in_matchedName = hit_aliases_matchedName.index(hit_inputExplanation)
                    result.append(hit_aliases_displayName[index_in_matchedName])
                elif hit_inputExplanation in hit_aliases_displayName:
                    result.append(hit_inputExplanation)
                else:
                    result.append(hit_inputExplanation)
            
            return result


    # Implementation: Match/Hit Handler, Spark manager

    # Merge columns 
    ap_hit_names_sql = spark_instance.sql_merge_to_target_col_from_source_cols(alert_hits_df, 'ap_hit_names', 'hit_inputExplanations_matchedName_inputExplanation', 'hit_inputExplanations_aliases_matchedName_inputExplanation')
    alert_ap_hit_names_df = alert_hits_df.select('*', ap_hit_names_sql)

    # Clear columns from empty hits 
    alert_ap_wl_hit_names_df = alert_ap_hit_names_df.withColumn('wl_hit_matched_name',
                                                                F.when(F.expr('size(hit_explanations_matchedName_Explanation) > 0'), F.col('hit_displayName')) \
                                                                .otherwise(F.lit(None))
                                                                ) \
                                                    .withColumn('wl_hit_aliases_matched_name',
                                                                F.udf(get_wl_hit_aliases_matched_name, ArrayType(StringType()))('hit_aliases_displayName', 'hit_aliases_matchedName', 'hit_explanations_aliases_matchedName_Explanation')
                                                                )
    # Merge wl hits columns
    wl_hit_names_sql = spark_instance.sql_merge_to_target_col_from_source_cols(alert_ap_wl_hit_names_df, 'wl_hit_names', 'wl_hit_matched_name', 'wl_hit_aliases_matched_name')
    alert_ap_wl_hit_names_df = alert_ap_wl_hit_names_df.select('*', wl_hit_names_sql)


    # Implementation: Match/Hit Handler, Spark manager

    # Merge wl hits columns
    merge_hit_and_aliases_displayName_sql = spark_instance.sql_merge_to_target_col_from_source_cols(alert_ap_wl_hit_names_df, 'wl_hit_names', 'hit_displayName', 'hit_aliases_displayName')
    merge_ap_names_sql = spark_instance.sql_merge_to_target_col_from_source_cols(alert_ap_wl_hit_names_df, 'ap_hit_names', 'alert_ahData_partyName', return_array=True)

    # Clear wl and ap hits columns
    alert_ap_wl_hit_names_df = alert_ap_wl_hit_names_df.withColumn('wl_hit_names',
                                                                    F.when(F.expr('size(wl_hit_names) > 0'), F.col('wl_hit_names')) \
                                                                    .otherwise(merge_hit_and_aliases_displayName_sql)
                                                                    ) \
                                                    .withColumn('ap_hit_names',
                                                                    F.when(F.expr('size(ap_hit_names) > 0'), F.col('ap_hit_names')) \
                                                                    .otherwise(merge_ap_names_sql)
                                                                    )

    alert_ap_wl_hit_names_df.toPandas()['ap_hit_names']


    # Implementation: Match/Hit Handler, Spark manager
    alert_statuses_df = spark_instance.read_delta(in_standardized_data_dir('ACM_MD_ALERT_STATUSES.delta')).select('STATUS_INTERNAL_ID', 'STATUS_NAME')
    alert_ap_wl_hit_names_df = alert_ap_wl_hit_names_df.join(alert_statuses_df, 'STATUS_INTERNAL_ID')
    alert_ap_wl_hit_names_df = spark_instance.reorder_cols(alert_ap_wl_hit_names_df, 'STATUS_INTERNAL_ID', 'STATUS_NAME')
    alert_ap_wl_hit_names_df = spark_instance.write_and_get_delta_data(alert_ap_wl_hit_names_df, in_cleansed_data_dir(file_name),
                                            user_metadata = 'More processing on AP and WL names, pinpoint to the exact names from AP and WL which caused the hits'
                                           )
    # Implementation: NRIC handler (Customer specifics)
    def extract_wl_nric_dob(custom_field):
        def _extract_yob_from_st_nrics(nrics):
            # Assuemd there is no data quality issue, e.g, 2 S or T NRICs
            for nric in nrics:
                nric_type = nric[0]
                if nric_type.lower() in ['s', 't']:
                    two_digit_year = nric[1:3]

                    if nric_type.lower() == 's':
                        if int(two_digit_year) >= 68:
                            yob = '19' + two_digit_year
                        else:
                            yob = None
                    else:
                        yob = '20' + two_digit_year

                    return yob
        
        nric_match = re.match('^NRIC:.*?([STGF]\d{7}[A-Z])', custom_field)
        dob_match = re.match('.*DOB: (.+?\d{4})[,.]', custom_field)
        possible_nric_match = re.findall('([STGF]\d{7}[A-Z])', custom_field)
        
        if nric_match:
            nrics = nric_match.groups()
        else:
            nrics = None
            
        if dob_match:
            dobs = dob_match.groups()
        else:
            # Try to use NRIC to extract YOB only when there is no DOB from the text data
            if nrics:
                dobs = _extract_yob_from_st_nrics(nrics)
            else:
                dobs = None
                
        if possible_nric_match:
            possible_nrics = possible_nric_match
        else:
            possible_nrics = None 
        
        return {'nric': nrics, 'dob': dobs, 'possible_nric': possible_nrics}



    def extract_ap_nric(ap_id_numbers):
        ap_nrics = []
        for id_number in set(ap_id_numbers):
            if id_number and re.match('^[STGF]\d{7}[A-Z]$', id_number.upper()):
                ap_nrics.append(id_number)
        
        return ap_nrics

    extract_wl_nric_dob('NRIC: S6959726J, DOB: 1955, Freque')

    alert_nric_df = alert_ap_wl_hit_names_df.withColumn('hit_cs_1_data_points',
                                                        F.udf(extract_wl_nric_dob, MapType(StringType(), ArrayType(StringType())))('hit_cs_1')
                                                    )
    extract_ap_nric(['S7364776B', 'S7335736B'])

    alert_nric_df = alert_nric_df.withColumn('ap_nric',
                                            F.udf(extract_ap_nric, ArrayType(StringType()))('alert_partyIds_idNumber')
                                            )

    spark_instance.group_count(alert_nric_df.selectExpr('size(ap_nric) as s', 'ap_nric'), 's')

    alert_nric_df.selectExpr('size(ap_nric) as s', 'ap_nric').where('s = 2').limit(2).toPandas()
    alert_nric_df = spark_instance.write_and_get_delta_data(
                                 alert_nric_df,
                                 in_cleansed_data_dir(file_name),
                                 user_metadata = 'Extracted AP and WL NRIC'
                                 )

   
        # Implementation: Spark manager
    alert_notes_file_name = 'ACM_ALERT_NOTES.delta'
    item_status_file_name = 'ACM_ITEM_STATUS_HISTORY.delta'

    alert_notes_df = spark_instance.read_delta(in_standardized_data_dir(alert_notes_file_name))
    item_status_history_df = spark_instance.read_delta(in_standardized_data_dir(item_status_file_name))
    alert_statuses_df = spark_instance.read_delta(in_standardized_data_dir('ACM_MD_ALERT_STATUSES.delta'))


    # Implementation: Spark manager, Delta Converter

    item_status_history_df = item_status_history_df.join(alert_statuses_df.selectExpr('STATUS_IDENTIFIER', 'STATUS_NAME as FROM_STATUS_NAME'),
                                    F.col('FROM_STATUS_IDENTIFIER') == F.col('STATUS_IDENTIFIER'),
                                    how='left') \
                               .drop('STATUS_IDENTIFIER') \
                               .join(alert_statuses_df.selectExpr('STATUS_IDENTIFIER', 'STATUS_NAME as TO_STATUS_NAME'),
                                    F.col('TO_STATUS_IDENTIFIER') == F.col('STATUS_IDENTIFIER'),
                                    how='left') \
                               .drop('STATUS_IDENTIFIER')

    item_status_history_df = spark_instance.reorder_cols(item_status_history_df, 'FROM_STATUS_IDENTIFIER', 'FROM_STATUS_NAME')
    item_status_history_df = spark_instance.reorder_cols(item_status_history_df, 'TO_STATUS_IDENTIFIER', 'TO_STATUS_NAME')
    item_status_history_df.createOrReplaceTempView('status_df')

    system_id = "22601"
    item_status_history_stage_df = spark_instance.spark_instance.sql(f'''
    with status_row_num as (
        select *,
            row_number() over (partition by item_id order by create_date asc) as row_num
        from status_df),
    first_last_analyst_row_num as (
        select ITEM_ID,
            min(row_num) as first_analyst_row_num,
            max(row_num) as last_analyst_row_num
        from status_row_num
        where user_join_id != "{system_id}"
        group by ITEM_ID
        )
    select a.*,
        b.first_analyst_row_num,
        b.last_analyst_row_num,
        case 
            when row_num = first_analyst_row_num and row_num = last_analyst_row_num then "first_last_analyst_status"
            when row_num = first_analyst_row_num then "first_analyst_status"
            when row_num = last_analyst_row_num then "last_analyst_status"
            when row_num > first_analyst_row_num then "middle_analyst_status"
            else "system_activity"
        end as analyst_status_stage
    from status_row_num a
    join first_last_analyst_row_num b
    on a.ITEM_ID = b.ITEM_ID
    ''')
    item_status_history_stage_df = spark_instance.write_and_get_delta_data(
                                                item_status_history_stage_df,
                                                delta_path=in_cleansed_data_dir(item_status_file_name),
                                                user_metadata='Tagged the status stage'
                                               )
    alert_notes_df.createOrReplaceTempView('notes_df')

    alert_notes_stage_df = spark_instance.spark_instance.sql('''
        with notes_row_num as (
            select *,
                row_number() over (partition by alert_id order by create_date asc) as row_num
            from notes_df),
        first_last_analyst_row_num as (
            select *,
                min(row_num) over (partition by alert_id) as first_analyst_row_num,
                max(row_num) over (partition by alert_id) as last_analyst_row_num
            from notes_row_num)
        select *,
            case 
                when row_num = first_analyst_row_num and row_num = last_analyst_row_num then "first_last_analyst_note"
                when row_num = first_analyst_row_num then "first_analyst_note"
                when row_num = last_analyst_row_num then "last_analyst_note"
                else "middle_analyst_note"
            end as analyst_note_stage
        from first_last_analyst_row_num    
    ''')

    alert_notes_stage_df = spark_instance.write_and_get_delta_data(
                                        alert_notes_stage_df,
                                        delta_path=in_cleansed_data_dir(alert_notes_file_name),
                                        user_metadata='Tagged the note stage'
                                       )