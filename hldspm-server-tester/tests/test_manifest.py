import requests
import json

get_routing = "/manifest"
with open('data.json') as file:
    data = json.load(file)
    server_ip = data['server_ip']
    server_port = data['server_port']
    server_protocol = data['server_protocol']
    token = data['token']
    name = data['name']

get_url = server_protocol + "://" + server_ip + ":" + server_port + get_routing


def test_get_nonsence():
    request = {
        "engine": "gold",
        "game": "hl",
        "manifest": [
            {
                "type": "plugin",
                "name": "nonesensei909iwef09iw90ifwe"
            }]
    }

    response = requests.get(get_url, json=request, verify=False)
    assert response.json().get("status") == 211
    assert len(response.json().get("errors")) == 1


def test_get_simple_manifest():
    request = {
        "engine": "gold",
        "game": "hl",
        "manifest": [
            {
                "type": "plugin",
                "name": "gauss-overheat"
            }]
    }

    response = requests.get(get_url, json=request, verify=False)
    assert response.json().get("status") == 200
    assert not len(response.json().get("errors"))


def test_difficult_manifest():
    request = {
        "engine": "gold",
        "game": "hl",
        "manifest": [
            {
                "type": "plugin",
                "name": "gauss-overheat"
            },
            {
                "type": "map",
                "name": "yardrats_new"
            }
        ]
    }
    response = requests.get(get_url, json=request, verify=False)
    assert response.json().get("status") == 200
