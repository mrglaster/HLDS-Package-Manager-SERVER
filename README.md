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

3) Удаление установленного контента. Иногда полное удаление карт, плагинов или модулей с большим количеством ресурсов представляет собой проблему, так как не всегда разработчики не всегда располагают ресурсы в отдельных папках для данного модуля (так все необходимые модлели могут просто находиться в папке models). Кроме того, встает проблема определения ресурсов, относящихся конкретно к данной карте при большом количестве установленных карт/плагинот на сервере. При удалении происходит обращение к файлу с расширением ```fileslist``` для данного ресурса, в последствие все файлы, перечисленные там будут удалены.   



## Функционал сервера

1) Запуск [FTP-сервера](https://mina.apache.org/ftpserver-project/) на локальной машине, на котором будут храниться игровые данные
2) Подключение к базе данных [PostgreSQL](https://www.postgresql.org/), в которой хранится информация о предоставляемом контенте
3) Валидация загружаемых в репозиторий ресурсов (соотвие архива необходимой структуре файлов)
4) Предоставление контента по запросу через API (в случае, если запрашиваемый контент есть в репозитории) 
5) Загрузка контента через API в репозиторий. 

## API 

### Получение ресурсов. Единичное получение.

**Тип запроса:*** GET <br>
***Роутинги:*** /plugin /map /module

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
   ]
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
    "name": "bundle name",
    "plugins": ["gauss-overheat", "deags-map-manager"]
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



