import os

from flask_wtf import FlaskForm
from wtforms import TextAreaField, SelectField


def load_requests():
    files = next(os.walk("data/"))[2]
    files = [""] + sorted(list(filter(lambda x: x.endswith(".json"), files)))
    return files


class RequestForm(FlaskForm):
    requests = SelectField(u"Select predefined request", choices=load_requests())
    request = TextAreaField()
