import logging
import os
import sys
from logging.handlers import TimedRotatingFileHandler

import omegaconf

CONFIG_APP_DIR = os.environ["CONFIG_APP_DIR"]
service_config = omegaconf.OmegaConf.load(os.path.join(CONFIG_APP_DIR, "service", "service.yaml"))


FORMATTER = logging.Formatter(
    "%(asctime)s — %(name)s — %(levelname)s — %(funcName)s:%(lineno)d — %(message)s"
)

LOGGING_PATH = None

try:
    LOGGING_PATH = service_config.LOGGING_PATH
except omegaconf.errors.ConfigAttributeError:
    pass


def get_console_handler():
    console_handler = logging.StreamHandler(sys.stdout)
    console_handler.setFormatter(FORMATTER)
    console_handler.setLevel(logging.DEBUG)
    return console_handler


def get_file_handler(filename="etl_pipeline.log"):
    os.makedirs(os.path.dirname(LOGGING_PATH), exist_ok=True)
    file_handler = TimedRotatingFileHandler(os.path.join(LOGGING_PATH, filename), when="midnight")
    file_handler.setLevel(logging.DEBUG)
    file_handler.setFormatter(FORMATTER)
    return file_handler


def get_logger(name, file=None):
    logger = logging.getLogger(name)
    if file:
        service_config = omegaconf.OmegaConf.load(
            os.path.join(CONFIG_APP_DIR, "service", "service.yaml")
        )
        try:
            LOGGING_PATH = service_config.LOGGING_PATH
        except omegaconf.errors.ConfigAttributeError:
            pass

        logger.setLevel(logging.DEBUG)
        if LOGGING_PATH:
            logger.addHandler(get_file_handler(file))
        logger.addHandler(get_console_handler())
        logger.propagate = False
        return logger
    else:
        return logger
