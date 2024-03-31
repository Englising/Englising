import inspect
import os

from log.log_info import LogKind
from log.logging_config import logger


def log(name, type, msg):
    caller_frame_record = inspect.stack()[1]
    caller_filepath = caller_frame_record.filename
    caller_filename = os.path.basename(caller_filepath)
    message = '[{}] {}: {}'.format(name, caller_filename, msg)

    match type:
        case LogKind.INFO: logger.info(message)
        case LogKind.DEBUG: logger.debug(message)
        case LogKind.ERROR: logger.error(message)
        case LogKind.CRITICAL: logger.critical(message)
        case LogKind.TRACE: logger.trace(message)
        case LogKind.WARNING: logger.warning(message)

