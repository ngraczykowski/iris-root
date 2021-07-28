import argparse
import logging
import pathlib

from company_name.agent.company_name_agent import CompanyNameAgent


def main():
    parser = argparse.ArgumentParser(description="Company name agent")
    parser.add_argument(
        "-c",
        "--configuration-dir",
        type=pathlib.Path,
        default=pathlib.Path("config"),
        help="Path for configuration files",
    )
    parser.add_argument(
        "-v",
        "--verbose",
        action="store_true",
        help="Increase verbosity for debug purpose",
    )
    args = parser.parse_args()

    logging.basicConfig(
        level=logging.DEBUG if args.verbose else logging.INFO,
        format="%(asctime)s %(name)-20s %(levelname)-8s %(message)s",
    )
    CompanyNameAgent(configuration_dir=args.configuration_dir).run()


if __name__ == "__main__":
    main()
