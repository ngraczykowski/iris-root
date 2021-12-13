import sys


def hide_errors_from_jupyter_notebook():
    sys.stderr = open("logs/stderr.log", "w")
