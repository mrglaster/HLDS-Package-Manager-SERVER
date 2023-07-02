import json
import requests

with open('data.json') as file:
    data = json.load(file)
    server_ip = data['server_ip']
    server_port = data['server_port']
    server_protocol = data['server_protocol']
    token = data['token']
    name = data['name']

delete_url = server_protocol + "://" + server_ip + ":" + server_port + "/delete"

maps_to_del = ["yardrats", "yardrats_new"]


def test_delete_plugins_family():
    request = {
        "token": token,
        "game": "hl",
        "type": "plugin",
        "engine": "gold",
        "name": "gauss-overheat"
    }
    response = requests.post(delete_url, json=request, verify=False)
    assert response.json().get("status") == 200


def test_delete_rest_of_maps():
    for i in maps_to_del:
        request = {
            "token": token,
            "game": "hl",
            "type": "map",
            "engine": "gold",
            "name": i
        }
        response = requests.post(delete_url, json=request, verify=False)
        assert response.json().get("status") == 200


def test_delete_bundles():
    bundles = ["test-bundle-plugins-yay", "bundle-to-delete"]
    for i in bundles:
        request = {
            "token": token,
            "game": "hl",
            "type": "bundle",
            "engine": "gold",
            "name": i
        }
        response = requests.post(delete_url, json=request, verify=False)

    assert True
