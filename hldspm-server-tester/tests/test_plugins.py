import requests
import json

import main
import modules.data_processor.data_processor

get_routing = "/plugin"
upload_routing = "/upload/plugin"
delete_routing = "/delete"

with open('data.json') as file:
    data = json.load(file)
    server_ip = data['server_ip']
    server_port = data['server_port']
    server_protocol = data['server_protocol']
    token = data['token']
    name = data['name']

get_url = server_protocol + "://" + server_ip + ":" + server_port + get_routing
upload_url = server_protocol + "://" + server_ip + ":" + server_port + upload_routing
delete_url = server_protocol + "://" + server_ip + ":" + server_port + delete_routing

def test_get_not_existing_plugin():
    request = {
        "engine": "gold",
        "game": "hl",
        "name": "fdfpdlpfdpsdfspkfspfskofspoksfpokfskopfspkosfkpofsopkofs"
    }
    response = requests.get(get_url, json=request, verify=False)
    assert response.json().get("status") == 404


def test_upload_simple_plugin():
    request = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "name": "gauss-overheat",
        "version": "1.0",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/plugins/gauss-overheat.tar.gz")
    }
    response = requests.post(upload_url, json=request, verify=False)
    assert response.json().get("status") == 200


def test_upload_newer_version():
    request = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "name": "gauss-overheat",
        "version": "1.1",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/plugins/gauss-overheat.tar.gz")
    }
    response = requests.post(upload_url, json=request, verify=False)
    assert response.json().get("status") == 200


def test_upload_same():
    request = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "name": "gauss-overheat",
        "version": "1.1",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/plugins/gauss-overheat.tar.gz")
    }
    response = requests.post(upload_url, json=request, verify=False)
    assert response.json().get("status") == 433



def test_upload_bad_token():
    request = {
        "token": token + "228",
        "engine": "gold",
        "game": "hl",
        "name": "gauss-overheat",
        "version": "1.0",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/plugins/gauss-overheat.tar.gz")
    }
    response = requests.post(upload_url, json=request, verify=False)
    assert response.json().get("status") == 403


def test_get_plugin_simple():
    request = {
        "engine": "gold",
        "game": "hl",
        "name": "gauss-overheat"
    }
    response = requests.get(get_url, json=request, verify=False)
    assert response.json().get("status") == 200
    assert "gauss-overheat%1.1" in response.json().get("link")


def test_get_plugin_specified_version():
    request = {
        "engine": "gold",
        "game": "hl",
        "name": "gauss-overheat%1.0"
    }
    response = requests.get(get_url, json=request, verify=False)
    assert response.json().get("status") == 200
    assert "gauss-overheat%1.0" in response.json().get("link")



def test_delete_plugin():
    request_upload = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "name": "gauss-overheat",
        "version": "1.8",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/plugins/gauss-overheat.tar.gz")
    }
    response = requests.post(upload_url, json=request_upload, verify=False)
    assert response.json().get("status") == 200
    request_delete = {
        "token": token,
        "game": "hl",
        "type": "plugin",
        "engine": "gold",
        "name": "gauss-overheat%1.8"
    }
    response = requests.post(delete_url, json=request_delete, verify=False)
    assert response.json().get("status") == 200


def test_get_deleted():
    request = {
        "engine": "gold",
        "game": "hl",
        "name": "gauss-overheat%1.8"
    }
    response = requests.get(get_url, json=request, verify=False)
    assert response.json().get("status") == 404


def test_upload_bad_plugin():
    request_upload = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "name": "bad-plugin",
        "version": "1.8",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/maps/mansion_v2.tar.gz")
    }
    response = requests.post(upload_url, json=request_upload, verify=False)
    assert response.json().get("status") == 400



def test_incomplete_request():
    request_upload = {
        "token": token,
        "engine": "",
        "game": "hl",
        "name": "bad-plugin",
        "version": "1.8",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/maps/mansion_v2.tar.gz")
    }
    response = requests.post(upload_url, json=request_upload, verify=False)
    assert response.json().get("status") == 400

    request_upload = {
        "token": token,
        "engine": "gold",
        "game": "",
        "name": "bad-plugin",
        "version": "1.8",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/maps/mansion_v2.tar.gz")
    }
    response = requests.post(upload_url, json=request_upload, verify=False)
    assert response.json().get("status") == 400


    request_upload = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "name": "",
        "version": "1.8",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/maps/mansion_v2.tar.gz")
    }
    response = requests.post(upload_url, json=request_upload, verify=False)
    assert response.json().get("status") == 400

    request_upload = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "name": "crap",
        "version": "",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/maps/mansion_v2.tar.gz")
    }
    response = requests.post(upload_url, json=request_upload, verify=False)
    assert response.json().get("status") == 400
