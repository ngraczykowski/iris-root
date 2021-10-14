import requests
import logging
import json
import time

from flask import Flask, render_template, flash, request
from flask_bootstrap import Bootstrap

from app.RequestForm import RequestForm

app = Flask(__name__)
app.config.from_envvar("MOCKAPI_SETTINGS")
Bootstrap(app)

logging.basicConfig(level=logging.DEBUG)


@app.before_request
def log_request_info():
    app.logger.debug('Headers: %s', request.headers)

    try:
        app.logger.debug('Body: %s',
            json.dumps(json.loads(request.get_data().decode('utf-8')), indent=2))
    except:
        app.logger.debug('Body: %s', request.get_data())


@app.route("/", methods=["GET", "POST"])
def make_request():
    form = RequestForm(request.form)
    if request.method == "POST":
        try:
            headers = {'content-type': 'application/json'}
            requests.post(app.config["PAYMENTS_BRIDGE_ENDPOINT"], data=form.request.data, headers=headers)
            flash("The request has been sent.")
        except:
            flash("The request has not been sent!", category="error")

    return render_template("make-request.html", form=form)


@app.route("/get-request-content", methods=["POST"])
def get_content():
    data = request.get_json()
    file_name = "data/" + data["request_file"]
    with open(file_name, 'r') as file:
        file_content = file.read()

    return file_content


@app.route("/ack", methods=["POST"])
def ack():
    return "", 204


@app.route("/callback-recommendation", methods=["POST"])
def recommendation():
    time.sleep(35)
    return "", 204


if __name__ == "__main__":
    app.run()
