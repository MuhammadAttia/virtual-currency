# Virtual-Currency
### Application that allows users to send / receive virtual currency.


## API Specs

### /users

#### POST
##### Summary:

add user with default wallet

##### Responses

| Code | Description |
| ---- | ----------- |
| 201 | CREATED |
| 400 | Bad Request |
| 500 | Internal Server Error |

#### GET
##### Summary:

get user ( for testing purpose )

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| userId | header | User Id | Yes | string (UUID) |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | SUCCESS |
| 400 | Bad Request |
| 500 | Internal Server Error |

### /transactions

#### GET
##### Summary:

Find all transaction for specfic user

##### Description:

For each user retrieve a list of VC transactions where they can see who they’ve sent and received money from.


##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| userId | header | User Id | Yes | string (UUID) |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | successful operation |
| 400 | Invalid user Id |
| 401 | Un Authorized |
| 404 | transactions not found |

#### POST
##### Summary:

create transaction to send any amount of VC to users.


##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| userId | header | User Id | Yes | string (UUID) |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | OK |
| 400 | Bad Request |
| 500 | Internal Server Error |


## Database Schema

![Alt text](src/main/resources/VC_DB_ER.png?raw=true "Database Schema")

### requirement to set up the service

    - java 12 
    - maven
    - postman to import vcApi.yaml as postman collection 
        to test the application
    - MYSQL as DBMS
    - Spring boot 2.2.1

### Step 1

    update 'src/resources/application.yaml' with your connection prameters
    such as database url , username and password

### Step 2

    build the application using maven 

    bash
    mvn package


### Step 3

    bash
    java -jar ./target/virtual-currency-0.0.1-SNAPSHOT.jar


### Step 4

- import openApi/vcApi.yaml to postman as postman collection
- change the baseUrl var on postman with your server url and running port number example
  http://localhost:8080
- test all endpoints

### Step 5

- you can review api specs [openApi/vcApi.yaml](OpenAPI/vcApi.yaml)
  on swagger editor