# CMAPI Mock
Mock for CMAPI endpoints.

## Setup environment
Use following commands to create the virtual environment:

    python3 -m venv .venv
    venv/bin/pip install -U pip setuptools wheel
    venv/bin/pip install -r requirements.txt

To activate the virtual environment:

    source .venv/bin/activate

## Running
Make sure that `settings.cfg` is created (`cp settings.cfg.template settings.cfg`):

    ./scripts/start-web.sh

After executing the command above the web app will be running on http://127.0.0.1:5000/. If you want to change the port, please run it as follows:

    ./scripts/start-web.sh --port=24609

Here we are able to choose one of the prepared requests (from the drop-down component) or we can create our own JSON-like request.
All predefined requests are stored in the `cmapi-mock/data` dir.

Other defined endpoints:
- `/ack`
- `/callback-phase1`
- `/callback-phase2`
- `/callback-phase3`
  
All of the above are `POST` endpoints.
