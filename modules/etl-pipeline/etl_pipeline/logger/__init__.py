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

logging_path = None


def get_logging_level():
    logging_level = logging.INFO
    try:
        logging_level = getattr(logging, service_config.logging_level)
    except omegaconf.errors.ConfigAttributeError:
        pass
    return logging_level


try:
    logging_path = service_config.logging_path
except omegaconf.errors.ConfigAttributeError:
    pass


def get_console_handler():
    console_handler = logging.StreamHandler(sys.stdout)
    console_handler.setFormatter(FORMATTER)
    console_handler.setLevel(get_logging_level())
    return console_handler


def get_file_handler(filename="etl_pipeline.log"):
    os.makedirs(os.path.dirname(logging_path), exist_ok=True)
    file_handler = TimedRotatingFileHandler(
        os.path.join(logging_path, filename), encoding="utf8", when="midnight"
    )
    file_handler.setFormatter(FORMATTER)
    file_handler.setLevel(get_logging_level())
    return file_handler


def get_logger(name, file=None):
    logger = logging.getLogger(name)
    if file:
        logging_path = None
        try:
            logging_path = service_config.logging_path
        except omegaconf.errors.ConfigAttributeError:
            pass

        logger.setLevel(get_logging_level())
        if logging_path:
            logger.addHandler(get_file_handler(file))
        logger.addHandler(get_console_handler())
        logger.propagate = False
        return logger
    else:
        return logger
