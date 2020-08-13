from idmismatchagent import *
from tstoolkit.utils import setup_logging, generate_logger, LogLevel

if __name__ == "__main__":
    setup_logging()
    logger = generate_logger(log_level=LogLevel.debug)
    logger.debug(
        identification_mismatch_agent(
            SearchCodeMismatchAgentInput(
                message_type="103",
                message_tag="70",
                matching_field="WE REFER TO 23190617054158 FOR15,990.00",
                matching_text="190617",
                wl_search_codes="190617",
                wl_type="Individual",
            )
        )
    )
