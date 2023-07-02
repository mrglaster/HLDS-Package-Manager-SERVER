import requests
import json

import main
import modules.data_processor.data_processor

get_routing = "/bundle"
upload_routing = "/upload/bundle"
upload_map_routing = "/upload/map"


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
upload_map_url = server_protocol + "://" + server_ip + ":" + server_port + upload_map_routing
delete_url = server_protocol + "://" + server_ip + ":" + server_port + delete_routing


def test_get_unknown_bundle():
    request = {
        "engine": "gold",
        "game": "hl",
        "name": "ijiojiojioioiodiofgfiogifopgfdpogi",
        "type": "plugin"
    }
    response = requests.get(get_url, json=request, verify=False)
    assert response.json().get("status") == 404


def test_create_bunle_plugins():
    request = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "type": "plugin",
        "name": "test-bundle-plugins-yay",
        "elements": ["gauss-overheat%1.0", "gauss-overheat%1.1"]
    }
    response = requests.post(upload_url, json=request, verify=False)
    assert response.json().get("status") == 200


def test_get_created_bundle():
    request = {
        "engine": "gold",
        "game": "hl",
        "name": "test-bundle-plugins-yay",
        "type": "plugin"
    }
    response = requests.get(get_url, json=request, verify=False)
    assert response.json().get("status") == 200
    assert len(response.json().get("elements")) == 2


def test_delete_bundle():
    request = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "type": "plugin",
        "name": "bundle-to-delete",
        "elements": ["gauss-overheat%1.0", "gauss-overheat%1.1"]
    }
    response = requests.post(upload_url, json=request, verify=False)
    assert response.json().get("status") == 200
    request = {
        "token": token,
        "game": "hl",
        "engine": "gold",
        "type": "bundle",
        "name": "bundle-to-delete"
    }
    response = requests.post(delete_url, json=request, verify=False)
    assert response.json().get("status") == 200


def test_check_bundles_update():
    request = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "name": "yardrats-crap1",
        "gamemode": "dm",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/maps/yardrats.tar.gz")
    }
    response = requests.post(upload_map_url, json=request, verify=False)

    request = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "name": "yardrats-crap2",
        "gamemode": "dm",
        "data": modules.data_processor.data_processor.file_to_base64(main.ROOT_DIR + "/demodata/maps/yardrats.tar.gz")
    }
    response = requests.post(upload_map_url, json=request, verify=False)

    request = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "type": "map",
        "name": "rats-crappy-bundle",
        "elements": ["yardrats-crap1", "yardrats-crap2"]
    }
    response = requests.post(upload_url, json=request, verify=False)
    assert response.json().get("status") == 200

    request = {
        "engine": "gold",
        "game": "hl",
        "name": "rats-crappy-bundle",
        "type": "map"
    }
    response = requests.get(get_url, json=request, verify=False)
    assert response.json().get("status") == 200
    assert len(response.json().get("elements")) == 2


    request = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "type": "map",
        "name": "yardrats-crap2"
    }
    response = requests.post(delete_url, json=request, verify=False)
    assert response.json().get("status") == 200

    request = {
        "engine": "gold",
        "game": "hl",
        "name": "rats-crappy-bundle",
        "type": "map"
    }
    response = requests.get(get_url, json=request, verify=False)
    assert response.json().get("status") == 200
    assert len(response.json().get("elements")) == 1

    request = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "type": "map",
        "name": "yardrats-crap1"
    }
    response = requests.post(delete_url, json=request, verify=False)
    assert response.json().get("status") == 200

    request = {
        "engine": "gold",
        "game": "hl",
        "name": "rats-crappy-bundle",
        "type": "map"
    }
    response = requests.get(get_url, json=request, verify=False)
    assert response.json().get("status") == 404




def test_create_bundle_for_furute():
    request = {
        "token": token,
        "engine": "gold",
        "game": "hl",
        "type": "plugin",
        "name": "bundle-to-delete",
        "elements": ["gauss-overheat%1.0", "gauss-overheat%1.1"]
    }
    response = requests.post(upload_url, json=request, verify=False)
    assert response.json().get("status") == 200
