import requests
import json

demo_name = "hachu"
demo_token = "avtomat"

creation_routing = "/create/uploader"
delete_routing = "/delete/uploader"

with open('data.json') as file:
    data = json.load(file)
    server_ip = data['server_ip']
    server_port = data['server_port']
    server_protocol = data['server_protocol']
    token = data['token']
    name = data['name']

creation_url = server_protocol + "://" + server_ip + ":" + server_port + creation_routing
delete_url = server_protocol + "://" + server_ip + ":" + server_port + delete_routing


def test_add_admin():
    request = {
        "token": token,
        "name": demo_name,
        "uploader_token": demo_token
    }
    response = requests.post(creation_url, json=request, verify=False)
    assert response.json().get("status") == 200


def test_add_admin_same_name():
    request = {
        "token": token,
        "name": demo_name,
        "uploader_token": demo_token
    }
    response = requests.post(creation_url, json=request, verify=False)
    assert response.json().get("status") == 400


def test_delete_uploader():
    request = {
        "token": token,
        "name": demo_name,
    }
    response = requests.post(delete_url, json=request, verify=False)
    assert response.json().get("status") == 200


def test_delete_deleted_admin():
    request = {
        "token": token,
        "name": demo_name,
    }
    response = requests.post(delete_url, json=request, verify=False)

    assert response.json().get("status") == 404


def test_delete_yourself():
    request = {
        "token": token,
        "name": name
    }
    response = requests.post(delete_url, json=request, verify=False)
    assert response.json().get("status") == 400


def test_delete_not_sudo():
    request = {
        "token": demo_token,
        "name": demo_name
    }
    response = requests.post(delete_url, json=request, verify=False)
    assert response.json().get("status") == 403

