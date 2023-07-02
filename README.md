# HLDS Package Manager: Server

## Описание проекта

Проект HLDS Package Manager предназначен для предоставления игрового контента  игровым серверам на движках GoldSource и Source, использующих системы [Half-Life Dedicated Server](https://developer.valvesoftware.com/wiki/Half-Life_Dedicated_Server) или [Source Dedicated Server](https://developer.valvesoftware.com/wiki/Source_Dedicated_Server)  при помощи API.

В данном репозитории представлена серверная часть вышеописанного проекта. 

## Применение. Клиент.

Взаимодействие с репозиторием предполагается через клиент, реализованный в виде модуля для системы [Metamod](http://metamod.org/), или [AMX MOD X](https://www.amxmodx.org/) - плагин при помощи API. Предполагаются следующие сценарии использования

1) Установка необходимого контента путем взаимодействия с консолью сервера. При вводе администратором команды ```hlds-get``` и последующих аргументов в консоли сервера происходит скачивание необходимого контента и последующая его обработка в зависимости от типа.

***Пример командля для загрузки карты rats с репозитория:*** 

```hlds-get map rats```

После ввода команды с репозитория будет скачан ```tar.gz``` архив, содержащий ресурсы вышеупомянутой карты, который в последствие будет распакован в директории мода (в данном случае ```valve```). Список распакованных данных будет сохранен в файле ```rats.fileslist``` в директории downloads в папке мода.

2) Быстрое развертывание серверной сборки. Пользователю достаточно иметь систему HLDS с предустановленными Metamod, Amx Mod X, и HLDS Package Manager, а также файл server.manifest, в котором хранится список необходимого контента. При первом запуске сборки произойдёт скачивание контента, прописанного в манифесте.   

3) Удаление установленного контента. Иногда полное удаление карт, плагинов или модулей с большим количеством ресурсов представляет собой проблему, так как не всегда разработчики располагают ресурсы в соответствующих подпапках директорий ресурсов. Кроме того, встает проблема определения ресурсов, относящихся конкретно к данной карте/плагину при большом количестве установленных оных. При удалении происходит обращение к файлу с расширением ```fileslist``` для данного ресурса, в последствие все файлы, перечисленные там будут удалены.   



## Функционал сервера

1) Запуск [FTP-сервера](https://mina.apache.org/ftpserver-project/) на локальной машине, на котором будут храниться игровые данные
2) Подключение к базе данных [PostgreSQL](https://www.postgresql.org/), в которой хранится информация о предоставляемом контенте
3) Валидация загружаемых в репозиторий ресурсов (соотвие архива необходимой структуре файлов)
4) Предоставление контента по запросу через API (в случае, если запрашиваемый контент есть в репозитории) 
5) Загрузка контента через API в репозиторий. 

## API 

### Получение ресурсов. Единичное получение.

**Тип запроса:*** GET <br>
***Роутинги:*** /plugin /map 

***Поля запроса:*** 

| Поле   | Тип    | Описание                                                                                                                                                                                                                         |
|--------|--------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| engine | String | Игровой движок. Поддерживаются ``gold`` и ``source``                                                                                                                                                                                     |
| game   | String | Название игры/мода, для которого предназначен ресурс. Примеры: ``hl``, ``cs``, ``dod``                                                                                                                                                       |
| name   | String | Название ресурса. Для плагинов доступна возможность выбора версии. Например, если name = gauss_overheat, то будет скачана последняя версия плагина. Но если значением будет gauss_overheat%1.31 - будет скачана указанная версия |


***Примеры запросов:***

***Пример URL:*** http://localhost:8080/plugin <br>
***JSON:*** 
```
{
	"engine": "gold",
	"game": "hl",
	"name": "gauss-overheat%1.31"
}
```

***Пример URL:*** http://localhost:8080/map <br>
***JSON:***
```
{
	"engine": "gold",
	"game": "hl",
	"name": "classroomrats1"
}
```

***Примеры ответов от сервера:***

Ресурс найден: 

```
{
	"status": 200,
	"link": "ftp://anonymous@localhost/gold/maps/hl/classroomrats1.tar.gz"
}
```

Ресурс не найден: 

```
{
  "status": 404,
  "link": "None"
}
```


### Получение ресурсов. Получение списка карт по параметрам

***Роутинг***: /maplist <br>
***Тип запроса***: GET <br>

**Пример запроса на получение карт определённых режимов**: 

```
{
	"engine": "gold",
	"game": "hl",
	"modes": ["ffa", "ctf"]
}
```

**Пример ответа от сервера**: 

```
{
	"status": 200,
	"maps": ["rats", "crossfire_agarena", "ctf_turbine_gold", "ctf_2fort_gold"]
}
```

**Пример запроса на получение всех карт для данного мода***

```
{
	"engine": "gold",
	"game": "hl",
	"modes": ["ALL"]
}
```


### Получение ресурсов. Бандлы. 

***Бандл (набор, сборник)*** - несколько ресурсов, объединённых в одну группу. Это может быть набор плагинов, состоящий из main-части и ряда дополнений к нему. Или серия карт. <br>

***Тип запроса:*** GET <br>
***Роутинг:*** /bundle

***Поля запроса:***

| Поле   | Тип    | Описание                                                                                                                                                                                                                         |
|--------|--------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| engine | String | Игровой движок. Поддерживаются gold и source                                                                                                                                                                                     |
| game   | String | Название игры/мода, для которого предназначен ресурс. Примеры: ``hl``, ``cs``, ``dod``                                                                                                                                                       |
| type   | String | Тип сборника. Поддерживаемые варианты: ```plugin```, ``map``                                                                                                                                                                               |
| name   | String | Название ресурса. Для плагинов доступна возможность выбора версии. Например, если name = gauss_overheat, то будет скачана последняя версия плагина. Но если значением будет gauss_overheat%1.31 - будет скачана указанная версия |


### Получение ресурсов. Манифест. 

```Манифест``` - файл, содержащий список всего необходимого контента, который может включать в себя как карты, так и плагины и др. 

***Тип запроса***: GET <br>
***Роутинг***: /manifest

***Пример запроса:***

```
{
	"engine": "gold",
	"game": "hl",
	"manifest":[
			{
				"type": "plugin",
				"name": "gauss-overheat"
			},
			{
				"type": "map",
				"name": "rats"
			},
			{
				"type": "bundle",
				"name": "deags-map-manager"
			}
	]
	
}
```

***Пример ответа от сервера***

```
{
   "status":200,
   "plugins":[
      "ftp://anonymous@localhost/gold/plugins/hl/gauss-overheat%1.31.tar.gz",
      "ftp://anonymous@localhost/gold/plugins/hl/deags-map-manager-main%2.4.tar.gz",
      "ftp://anonymous@localhost/gold/plugins/hl/deags-map-manager-votes%2.4.tar.gz"
   ],
   "maps":[
      "ftp://anonymous@localhost/gold/maps/hl/rats.tar.gz"
   ],
   "errors": []
}
```


### Загрузка ресурсов в репозиторий. Загрузка карт. 

***Тип запроса:*** POST <br>
***Роутинг:*** /upload/map


| Поле   | Тип    | Описание                                                                                         |
|--------|--------|--------------------------------------------------------------------------------------------------|
| engine | String | Игровой движок. Поддерживаются gold и source                                                     |
| game   | String | Название игры/мода, для которого предназначен ресурс. Примеры: hl, cs, dod                       |
| gamemode   | String | Игровой режим карты, например ```ffa```, ```de```                       |
| token  | String | Токен пользователя с правом на загрузку ресурсов. На данный момент такие токены выдаются вручную |
| name   | String | Название ресурса                                                                                 |
| data   | String |  tar.gz архив с ресурсами в формате Base64.                                                      |


***Пример запроса:***

***JSON:***

```
{
   "token":"YOUR TOKEN",
   "game":"hl",
   "gamemode":"ffa",
   "engine":"gold",
   "name":"classroomrats1",
   "data":"base64_string"
}
```


### Загрузка ресурсов в репозиторий. Загрузка плагинов. 

***Тип запроса***: POST <br>
***Роутинг***: /upload/plugin


| Поле    | Тип    | Описание                                                                                         |
|---------|--------|--------------------------------------------------------------------------------------------------|
| engine  | String | Игровой движок. Поддерживаются gold и source                                                     |
| game    | String | Название игры/мода, для которого предназначен ресурс. Примеры: hl, cs, dod                       |
| token   | String | Токен пользователя с правом на загрузку ресурсов. На данный момент такие токены выдаются вручную |
| name    | String | Название ресурса                                                                                 |
| version | String | Версия ресурса                                                                                   |
| data    | String |  tar.gz архив с ресурсами в формате Base64.                                                      |


Пример запроса: 

```
{ 
   "engine":"gold",
   "game":"hl",
   "token":"YOUR TOKEN",
   "name":"gauss-overheat",
   "version": "1.33",
   "data":"base64_string"
}
```


#### Для любого запроса по загрузке данных на сервер реализована реализована единая форма ответов.

| Поле        | Тип    | Описание    |
|-------------|--------|-------------|
| status      | Int    | Код статуса |
| description | String | Описание    |


***Реализованы следующие коды статуса***

| Код | Описание                                  |
|-----|-------------------------------------------|
| 200 | Данные успешно загружены                  |
| 400 | Неизвестная игра/движок/невалидные данные |
| 403 | Доступ запрещён. Невалидный токен.        |
| 433 | Ресурс с таким именем уже загружен        |


### Загрузка данных. Создание бандлов

***Тип запроса***: POST <br>
***Роутинг***: /upload/bundle

***Пример запроса***:

```
{
    "token": "token",
    "engine": "gold",
    "game": "hl",
    "type": "plugin"
    "name": "bundle name",
    "elements": ["gauss-overheat", "deags-map-manager"]
}
```

***Примеры ответа***:

Успешный запрос: 

```
{
  "status": 200,
  "description": "The bundle was successfuly created"
}
```

Неуспешный запрос: 

```
{
  "status": 400,
  "description": "One or several plugins weren't found!"
}
```


### Добавление данных. Создание пользователя с правом загрузки контента в репозиторий

***Тип запроса***: POST <br>
***Роутинг***: /create/uploader

***Пример запроса***: 

```
{
	"token": "ADMIN TOKEN",
	"name": "NAME OF UPLOADER",
	"uploader_token": "TOKEN OF UPLOADER"
}
```

***Примеры ответов***: см [тветы по загрузке данных](https://github.com/mrglaster/HLDS-Package-Manager-SERVER#:~:text=%D0%94%D0%BB%D1%8F%20%D0%BB%D1%8E%D0%B1%D0%BE%D0%B3%D0%BE%20%D0%B7%D0%B0%D0%BF%D1%80%D0%BE%D1%81%D0%B0%20%D0%BF%D0%BE%20%D0%B7%D0%B0%D0%B3%D1%80%D1%83%D0%B7%D0%BA%D0%B5%20%D0%B4%D0%B0%D0%BD%D0%BD%D1%8B%D1%85%20%D0%BD%D0%B0%20%D1%81%D0%B5%D1%80%D0%B2%D0%B5%D1%80%20%D1%80%D0%B5%D0%B0%D0%BB%D0%B8%D0%B7%D0%BE%D0%B2%D0%B0%D0%BD%D0%B0%20%D1%80%D0%B5%D0%B0%D0%BB%D0%B8%D0%B7%D0%BE%D0%B2%D0%B0%D0%BD%D0%B0%20%D0%B5%D0%B4%D0%B8%D0%BD%D0%B0%D1%8F%20%D1%84%D0%BE%D1%80%D0%BC%D0%B0%20%D0%BE%D1%82%D0%B2%D0%B5%D1%82%D0%BE%D0%B2)


### Удаление пользователей с правом загрузки данных в репозиторий

**Удаление пользователей с правом загрузки данных в репозиторий доступно только главному администратору**

***Тип запроса***: POST <br>
***Роутинг***: /delete/uploader

***Пример запроса***:

```
{
	"token": "sudo admin token",
	"name": "uploader's name"
}
```

***Примеры ответов***: см [тветы по загрузке данных](https://github.com/mrglaster/HLDS-Package-Manager-SERVER#:~:text=%D0%94%D0%BB%D1%8F%20%D0%BB%D1%8E%D0%B1%D0%BE%D0%B3%D0%BE%20%D0%B7%D0%B0%D0%BF%D1%80%D0%BE%D1%81%D0%B0%20%D0%BF%D0%BE%20%D0%B7%D0%B0%D0%B3%D1%80%D1%83%D0%B7%D0%BA%D0%B5%20%D0%B4%D0%B0%D0%BD%D0%BD%D1%8B%D1%85%20%D0%BD%D0%B0%20%D1%81%D0%B5%D1%80%D0%B2%D0%B5%D1%80%20%D1%80%D0%B5%D0%B0%D0%BB%D0%B8%D0%B7%D0%BE%D0%B2%D0%B0%D0%BD%D0%B0%20%D1%80%D0%B5%D0%B0%D0%BB%D0%B8%D0%B7%D0%BE%D0%B2%D0%B0%D0%BD%D0%B0%20%D0%B5%D0%B4%D0%B8%D0%BD%D0%B0%D1%8F%20%D1%84%D0%BE%D1%80%D0%BC%D0%B0%20%D0%BE%D1%82%D0%B2%D0%B5%D1%82%D0%BE%D0%B2)


### Удаление ресурсов. 

**Удаление данных из репозитория доступно только главному администратору (данные которого находятся в ini файле конфигурации).**

***Тип запроса***: POST <br>
***Роутинг***: /delete <br>

***Пример запроса**:

```
{
	"token": "Superuser Token",
	"game": "hl",
	"engine": "gold",
	"type": "plugin",
	"name": "gauss-overheat"
}
```

***Поддерживаемые types***: ```map```, ```plugin```, ```module```, ```bundle```;

***Примеры ответов***: см [тветы по загрузке данных](https://github.com/mrglaster/HLDS-Package-Manager-SERVER#:~:text=%D0%94%D0%BB%D1%8F%20%D0%BB%D1%8E%D0%B1%D0%BE%D0%B3%D0%BE%20%D0%B7%D0%B0%D0%BF%D1%80%D0%BE%D1%81%D0%B0%20%D0%BF%D0%BE%20%D0%B7%D0%B0%D0%B3%D1%80%D1%83%D0%B7%D0%BA%D0%B5%20%D0%B4%D0%B0%D0%BD%D0%BD%D1%8B%D1%85%20%D0%BD%D0%B0%20%D1%81%D0%B5%D1%80%D0%B2%D0%B5%D1%80%20%D1%80%D0%B5%D0%B0%D0%BB%D0%B8%D0%B7%D0%BE%D0%B2%D0%B0%D0%BD%D0%B0%20%D1%80%D0%B5%D0%B0%D0%BB%D0%B8%D0%B7%D0%BE%D0%B2%D0%B0%D0%BD%D0%B0%20%D0%B5%D0%B4%D0%B8%D0%BD%D0%B0%D1%8F%20%D1%84%D0%BE%D1%80%D0%BC%D0%B0%20%D0%BE%D1%82%D0%B2%D0%B5%D1%82%D0%BE%D0%B2)


***Нюансы***:

1) При удалении ресурса он будет удален из бандла.
2) Если версия плагина не указана (т.е. не заканчивается на %номер_версии), то будут удалены все версии данного плагина и соответственно они будут удалены из всех бандлов, в которых они есть.
3) При удалении объекта все пустые бандлы будут удалены. 
4) Право на удаление есть только у главного администратора.


## SSL. Использование протокла HTTPS

Сервер поддерживает использование HTTPS протокола. Для его использования измените свойство ```server.ssl.enabled``` на ```true``` и пересоберите проект. **Внимание: сервер использует самоподписанный сертификат. Его необходимо внести в исключения/отключить проверку сертификатов** 


## Как запустить 

### ОС Linux

1) Установите Java версии 17+
Сделать это можно при помощи команды
```
sudo apt install openjdk-19-jre-headless
```

2) Установите PostgreSQL

```
 sudo apt install postgresql postgresql-contrib
 sudo systemctl start postgresql.service	
```

3) Создайте нового пользователя PostgreSQL с паролем

```
sudo -u postgres createuser -d -r -e username --pwprompt
```

4) Создайте новую базу данных, владальцем которой является созданный вами пользователь

```
 sudo -u postgres psql
 CREATE DATABASE dbname OWNER username;
```

5) Скачайте последний релиз сервера, распакуйте архив
6) Отредактируйте файл cfg/configuration.ini

```

[server]
port=8081 ; Порт для HLDS:PM Server

[database]
server_address="localhost"
server_port="5432"
database_name="HLDS_PM_db"
username="hldspm"
password="hldspm"
repo_admin="admin"
repo_admin_token="TOKEN"
create_table_structure="1"

[ftp]
address="localhost"
port="21"

```

7) Запустите сервер при помощи команды

```
sudo java -jar hldspm-server.jar 
```

8) Profit!

![изображение](https://github.com/mrglaster/HLDS-Package-Manager-SERVER/assets/50916604/b8ba1ece-5aac-456a-8f71-0576939b2b41)








