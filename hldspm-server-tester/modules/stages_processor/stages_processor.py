import subprocess
import uuid
from datetime import datetime
from pathlib import Path

styles = ["""
	<style>
		body {
			background-color: #000;
		}

		h2 {
			font-size: 4em;
			text-align: center;
			font-family: sans-serif;
			text-transform: uppercase;
			letter-spacing: 0.1em;
			padding: 0.5em;
			margin: 1em 0;
			box-shadow: 0 0 20px #fff;
		}

		h2.success {
			color: #0f0;
		}

		h2.failure {
			color: #f00;
		}
	</style>
	"""]




def process_stage(test_file: str, test_amount: int, tests_name: str) -> tuple:
    file_name = str(uuid.uuid4()) + ".log"
    init_tests_cmd = f"pytest tests/{test_file} > {file_name}"
    subprocess.call(init_tests_cmd, shell=True)
    file_data = []
    has_fails = False
    with open(file_name, "r") as f:
        lines = [line + "<br>" for line in f]
        for i in lines:
            if "FAILURES" in i:
                has_fails = True
                break
    Path(file_name).unlink()
    if has_fails:
        link = modules.connection_utils.connection_utils.IP_TESTS_FAILED
        file_data += [f'<h2 class="failure">{tests_name} STATUS: FAILED! </h2>',
                      f'<br><img src="{link}"><br>', ]
    else:
        file_data.append(f'<h2 class="success">{tests_name} STATUS: SUCCESS! ({test_amount}/{test_amount}) </h2>')
    return file_data


def process_tests():
    tests = ["test_users.py", "test_maps.py", "test_plugins.py", "test_bundles.py", "test_manifest.py", "test_resource_delete.py"]
    tests_amount = [6, 8, 11, 6, 3, 3]
    test_names = ["USERS TESTS", "MAPS TESTS", "PLUGINS TESTS", "BUNDLES TESTS", "MANIFEST TESTS", "DELETE TESTS"]
    result = []
    for i in range(len(tests)):
        result += process_stage(tests[i], tests_amount[i], test_names[i])
    return "".join(styles + result)
