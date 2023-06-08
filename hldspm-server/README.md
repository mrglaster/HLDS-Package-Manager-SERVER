# HLDS Package Manager Server API

## Maps processing

### Get map

***Type:*** GET <br>
***Routing:*** /maps/download <br>

***Example Request:*** 

```
{
  "game": "hl1",
  "map": "crossfire"
}
```

***Successful Response:***

```
{
  "status": 200,
  "link": "ftp-link"
}
```

***Unsuccessful response:***

```
"status": 404,
"link": ""
```


### Upload map

***Type:*** POST <br>
***Routing:*** /maps/upload <br>

***Example request:***

```
{
  "uploader_token": "token",
  "game": "hl1",
  "mapname": "rats",
  "data": "data_in_base64"
}
```

***Good response:***

```
{
  "status": 200
}
```

***Mapname is in use***

```
{
  "status": 409
}
```

***Such map is already uploaded***

```
{
"status": 410
}
```











