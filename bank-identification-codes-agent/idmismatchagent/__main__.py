from tstoolkit.utils import LogLevel, generate_logger, setup_logging

from idmismatchagent import SearchCodeMismatchAgentInput, identification_mismatch_agent


def main():
    setup_logging()
    logger = generate_logger(log_level=LogLevel.debug)
    logger.debug(
        identification_mismatch_agent(
            SearchCodeMismatchAgentInput(
                message_type="103",
                message_tag="70",
                matching_field="WE REFER TO 23190617054158 FOR15,990.00",
                matching_text="190617",
                wl_search_codes=["190617"],
                wl_type="Individual",
                wl_bic_codes=[],
            )
        )
    )


if __name__ == "__main__":
    main()
