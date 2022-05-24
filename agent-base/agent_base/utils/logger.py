import logging
import sys
from logging.handlers import TimedRotatingFileHandler

LOG_FORMAT = "%(asctime)s — %(name)s — %(levelname)s — %(funcName)s:%(lineno)d — %(message)s"

FORMATTER = logging.Formatter(LOG_FORMAT)


def get_console_handler(log_level=logging.INFO):
    console_handler = logging.StreamHandler(sys.stdout)
    console_handler.setFormatter(FORMATTER)
    console_handler.setLevel(log_level)
    return console_handler


def get_file_handler(filename="agent.log", log_level=logging.INFO):
    file_handler = TimedRotatingFileHandler(filename, encoding="utf8", when="midnight")
    file_handler.setFormatter(FORMATTER)
    file_handler.setLevel(log_level)
    return file_handler


def get_logger(name, log_filename=None, log_level=logging.INFO):
    console_handler = get_console_handler(log_level)
    handlers = [console_handler]
    if log_filename:
        handlers.append(get_file_handler(log_filename, log_level))
    logging.basicConfig(
        level=log_level,
        format=LOG_FORMAT,
        handlers=handlers,
    )
    logger = logging.getLogger(name)
    logger.addHandler(console_handler)
    if log_filename:
        logger.addHandler(get_file_handler(log_filename, log_level))
    logger.propagate = False
    return logger
