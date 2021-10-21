import argparse
import pathlib
import sys

from surrounding_check import get_company_token_count


def main():
    parser = argparse.ArgumentParser(description="Company name surrounding agent")
    parser.add_argument(
        "-c",
        "--configuration-dir",
        type=pathlib.Path,
        default=pathlib.Path("config"),
        help="Path for configuration files",
    )
    parser.add_argument(
        "--grpc",
        action="store_true",
        help="Start grpc service",
    )
    parser.add_argument(
        "-v",
        "--verbose",
        action="store_true",
        help="Increase verbosity for debug purpose",
    )
    parser.add_argument(
        "-n",
        "--names",
        nargs="+",
        help="names to check surrounding",
    )
    args = parser.parse_args()

    if args.names:
        return str(get_company_token_count(args.names))
    else:
        return ""


if __name__ == "__main__":
    sys.stdout.write(main() + "\n")
