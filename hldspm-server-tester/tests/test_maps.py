import requests
import json

import main
import modules.data_processor.data_processor

get_routing = "/map"
upload_routing = "/upload/map"
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


def test_get_not_existing_map():
    request = {
        "engine": "gold",
        "game": "hl",
        "name": "fdfpdlpfdpsdfspkfspfskofspoksfpokfskopfspkosfkpofsopkofs"
    }
    response = requests.get(get_url, json=request, verify=False)
    assert response.json().get("status") == 404


def test_upload_bad_map():
    request = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "name": "dm_townroad",
        "gamemode": "dm",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/maps/dm_townroad.tar.gz")
    }
    response = requests.post(upload_url, json=request, verify=False)
    assert response.json().get("status") == 422


def test_upload_simple_map():
    request = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "name": "yardrats",
        "gamemode": "dm",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/maps/yardrats.tar.gz")
    }
    response = requests.post(upload_url, json=request, verify=False)
    assert response.json().get("status") == 200


def test_upload_same_name():
    request = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "name": "yardrats",
        "gamemode": "dm",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/maps/yardrats.tar.gz")
    }
    response = requests.post(upload_url, json=request, verify=False)
    assert response.json().get("status") == 433


def test_upload_too_big_map():
    request = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "name": "mansion_v2",
        "gamemode": "dm",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/maps/mansion_v2.tar.gz")
    }
    response = requests.post(upload_url, json=request)
    assert response.json().get("status") == 400


def test_upload_bad_token():
    request = {
        "token": token + "228",
        "engine": "gold",
        "game": "hl",
        "name": "mansion_v2",
        "gamemode": "dm",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/maps/yardrats.tar.gz")
    }
    response = requests.post(upload_url, json=request)
    assert response.json().get("status") == 403


def test_upload_extra_map():
    request = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "name": "yardrats_new",
        "gamemode": "dm",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/maps/yardrats.tar.gz")
    }
    response = requests.post(upload_url, json=request, verify=False)
    assert response.json().get("status") == 200


def test_upload_incomplete_request():
    request = {
        "token": "",
        "engine": "gold",
        "game": "hl",
        "name": "yardrats_bad",
        "gamemode": "dm",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/maps/yardrats.tar.gz")
    }
    response = requests.post(upload_url, json=request, verify=False)
    assert response.json().get("status") == 403

    request = {
        "token": token,
        "engine": "",
        "game": "hl",
        "name": "yardrats_bad",
        "gamemode": "dm",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/maps/yardrats.tar.gz")
    }
    response = requests.post(upload_url, json=request, verify=False)
    assert response.json().get("status") == 422

    request = {
        "token": token,
        "engine": "gold",
        "game": "",
        "name": "yardrats_bad",
        "gamemode": "dm",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/maps/yardrats.tar.gz")
    }
    response = requests.post(upload_url, json=request, verify=False)
    assert response.json().get("status") == 400

    request = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "name": "",
        "gamemode": "dm",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/maps/yardrats.tar.gz")
    }
    response = requests.post(upload_url, json=request, verify=False)
    assert response.json().get("status") == 400


    request = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "name": "yardrats_bad",
        "gamemode": "dm",
        "data": ""
    }
    response = requests.post(upload_url, json=request, verify=False)
    assert response.json().get("status") == 400
