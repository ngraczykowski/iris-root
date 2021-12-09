import os
os.environ["SERP_HOME"] = ".."
import sys
sys.path.append('python-datatoolbox/')
os.environ["PATH"] = "/opt/jdk/bin"+ ":" + os.environ["PATH"]
import pandas as pd
import psutil

pd.options.display.max_rows = 50 
pd.options.display.max_columns = 500 
pd.options.display.width = 1000 
# pd.options.display.max_colwidth = None 
pd.options.display.float_format = lambda x: '%.3f' % x 


import os

POV_INTEGRATION_PATH = os.path.join(os.getcwd(), '..')

if 'S8_CONFIG_DIR_PATH' not in os.environ:
    MOAPOV_CONFIG_PATH = os.path.join(POV_INTEGRATION_PATH, 'conf/default')
    os.environ['S8_CONFIG_DIR_PATH'] = MOAPOV_CONFIG_PATH
    
if 'MOAPOV_WORKING_DIR_PATH' not in os.environ:
    MOAPOV_WORKING_DIR_PATH = os.path.join(POV_INTEGRATION_PATH, 'working_dir/default')
    os.environ['MOAPOV_WORKING_DIR_PATH'] = MOAPOV_WORKING_DIR_PATH
    
MOAPOV_PIPELINE_RESULTS_DIR_PATH = os.path.join(POV_INTEGRATION_PATH, 'notebooks-outputs')
MOAPOV_NOTEBOOKS_PATH = os.path.join(POV_INTEGRATION_PATH, 'moapov/notebooks')
CS_NOTEBOOKS_PATH = os.path.join(POV_INTEGRATION_PATH, 'pov_notebooks')

os.environ['MOAPOV_PIPELINE_RESULTS_DIR_PATH'] = MOAPOV_PIPELINE_RESULTS_DIR_PATH
os.environ['MOAPOV_NOTEBOOKS_PATH'] = MOAPOV_NOTEBOOKS_PATH
os.environ['CS_NOTEBOOKS_PATH'] = CS_NOTEBOOKS_PATH

DEBUG=True



## Victor config.ini - merge with config 

import sys, os, platform, pyspark, logging
from configparser import ConfigParser
from glob import glob
from collections.abc import Iterable
from typing import Union
from pprint import pprint

PLATFORM_SYSTEM = platform.system()
config = ConfigParser()

if PLATFORM_SYSTEM == 'Windows':
    config_file_path = 'config-windows.ini'
elif PLATFORM_SYSTEM == 'Darwin':
    config_file_path = 'config-mac.ini'
else:
    config_file_path = 'config.ini'
    
config.read(config_file_path)

PYTHON_DATA_TOOLBOX_PATH = config['APP']['PYTHON_DATA_TOOLBOX_PATH']
DATA_DIR = config['APP']['DATA_DIR']

RAW_DATA_DIR = config['APP']['RAW_DATA_DIR']
STANDARDIZED_DATA_DIR = config['APP']['STANDARDIZED_DATA_DIR']
CLEANSED_DATA_DIR = config['APP']['CLEANSED_DATA_DIR']
APPLICATION_DATA_DIR = config['APP']['APPLICATION_DATA_DIR']
REPORT_DATA_DIR = config['APP']['REPORT_DATA_DIR']
SANDBOX_DATA_DIR = config['APP']['SANDBOX_DATA_DIR']
APPLICATION_CONF_DIR = config['APP']['APPLICATION_CONF_DIR']


SPARK_APP_NAME = config['SPARK']['APP_NAME']
SPARK_MASTER = config['SPARK']['MASTER']
SPARK_DRIVER_MEMORY = config['SPARK']['DRIVER_MEMORY']
SPARK_USE_OPTIMAL_CONFIG = config['SPARK'].getboolean('USE_OPTIMAL_CONFIG')

SPARK_IVY2_DIR = config['SPARK']['IVY2_DIR']
SPARK_DB_JARS = config['SPARK']['DB_JARS']
SPARK_TMP_DIR = config['SPARK']['TMP_DIR']
# SPARK_COMPILE_EGG = config['SPARK'].getboolean('IS_COMPILE_EGG')

PYTHON_SKIP_PACKAGE_DEPENDENCY = config['PYTHON'].getboolean('SKIP_PACKAGE_DEPENDENCY')