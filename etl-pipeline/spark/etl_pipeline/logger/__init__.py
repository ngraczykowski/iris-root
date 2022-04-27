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


def get_logging_level():
    LOGGING_LEVEL = logging.INFO
    try:
        LOGGING_LEVEL = getattr(logging, service_config.LOGGING_LEVEL)
    except omegaconf.errors.ConfigAttributeError:
        pass
    return LOGGING_LEVEL


try:
    LOGGING_PATH = service_config.LOGGING_PATH
except omegaconf.errors.ConfigAttributeError:
    pass


def get_console_handler():
    console_handler = logging.StreamHandler(sys.stdout)
    console_handler.setFormatter(FORMATTER)
    console_handler.setLevel(get_logging_level())
    return console_handler


def get_file_handler(filename="etl_pipeline.log"):
    os.makedirs(os.path.dirname(LOGGING_PATH), exist_ok=True)
    file_handler = TimedRotatingFileHandler(
        os.path.join(LOGGING_PATH, filename), encoding="utf8", when="midnight"
    )
    file_handler.setLevel(get_logging_level())
    file_handler.setFormatter(FORMATTER)
    return file_handler


def get_logger(name, file=None):
    logger = logging.getLogger(name)
    if file:
        LOGGING_PATH = None
        try:
            LOGGING_PATH = service_config.LOGGING_PATH
        except omegaconf.errors.ConfigAttributeError:
            pass

        logger.setLevel(get_logging_level())
        if LOGGING_PATH:
            logger.addHandler(get_file_handler(file))
        logger.addHandler(get_console_handler())
        logger.propagate = False
        return logger
    else:
        return logger
