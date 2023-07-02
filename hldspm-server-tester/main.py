import os
import json
import modules.stages_processor.stages_processor
from flask import Flask, render_template, request

app = Flask(__name__)
ROOT_DIR = os.path.dirname(os.path.abspath(__file__))


@app.route('/')
def form():
    return render_template('form.html')


def write_init_json():
    data = {
        "server_ip": request.form['input_data'],
        "server_port": request.form['input_port'],
        "server_protocol": "http",
        "token": request.form['token'],
        "name": request.form['name']
    }
    json_data = json.dumps(data)
    with open('tests/data.json', 'w') as file:
        file.write(json_data)


@app.route('/run_test', methods=['POST'])
def run_test():
    write_init_json()
    return modules.stages_processor.stages_processor.process_tests()


if __name__ == '__main__':
    app.run(debug=True)
