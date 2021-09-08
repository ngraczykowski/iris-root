import time
from tstoolkit.utils import LogLevel, generate_logger, setup_logging

from id_mismatch_agent import SearchCodeMismatchAgentInput, identification_mismatch_agent


def main():
    setup_logging()
    logger = generate_logger(log_level=LogLevel.debug)
    logger.debug(
        identification_mismatch_agent(
            SearchCodeMismatchAgentInput(
                matching_field="WE REFER TO 23190617054158 FOR15,990.00",
                matching_text="190617",
                watchlist_search_codes=["190617"],
                watchlist_type="Individual",
                watchlist_bic_codes=[],
            )
        )
    )
    while True:  # just to be a service
        time.sleep(3600)


if __name__ == "__main__":
    main()
