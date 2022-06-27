## ShelterBot 1.0.0 Documentation

Telegram bot for discovering nearest bomb shelters in your place.

See it in work:

https://t.me/shelter_bomb_bot

Technology stack:

- Micronaut(CORE, AWS Lambda)

API used:

- AWS SDK2(Lambda function, S3)
- Telegram SDK
- Google Geocoding API
- Google Static MAP API
- Yandex Geocoding API

## How to configure required services:

Shelter bot requires the following services:

- AWS Lambda. 

Create new function in AWS console(choose Java 11 as runtime and specify com.corbandalas.shelterbot.FunctionRequestHandler as handler). In configuration tab specify the following environment vars:


AWSACCESSKEYID	Access key for AWS account

AWSBUCKET	Name of S3 bucket for shelter bot storage

AWSREGION	Lambda region name

AWSSECRETKEY	AWS account secret key

GOOGLEGEOKEY	Access key for google geo coding API

TELEGRAMBOTNAME	name of your telegram bot

TELEGRAMBOTPATH	API gateway name of your AWS lambda function configured as telegram bot notification URL

TELEGRAMBOTTOKEN	Token of your telegram bot

YANDEXGEOKEY	Access key for yandex geo coding API



- AWS S3

Create new bucket for storing telegram bot state files and specify its name in AWSBUCKET env

- AWS API gateway

Create new API gateway URL(and assign it to telegram bot callback URL) and create Lambda trigger to invoke function handler via http

- Google Static Map API and Google Geocoding API
Create google dev map account and specify API key in GOOGLEGEOKEY env var

- Yandex Geocoding API
Create google dev map account and specify API key in YANDEXGEOKEY env var. We used this API due to its best covering of our local facilities in Donetsk. You are free to ommit Yandex and use Google implementation instead (we have this client too).

## How to set up shelters DB:

Shelters CSV files per city are stored in resource/shelter folder. Please look at data file mapping in the application.yml:

shelterbot:

  db:
  
    defaultCountry: ДНР
    
    defaultRegion: Донецкая
    
    shelters:
    
      ДНР-Донецкая-Андреевка: 1.csv
      
      ДНР-Донецкая-Донецк: 2.csv
      
      ....

where e.g  ДНР-Донецкая-Андреевка: 1.csv means that ДНР - is country name, Донецкая - is region, Андреевка is name of city.

Each CSV file contains data in the following format(data is separated by ';' ):

Калининский;" «УК Калининского района  г. Донецка""";б. Шевченко, 49;Поддубный А.С.;50;ключ в квартире №10;37.141151 48.223868;

1st field - District name 
2 - Responsible company cared about shelter
3 - Address
4 - Responsible person name
5 - Capacity
5 - Description
6 - Coords (separated by space)


## How to install and deploy:

Install and configure AWS CLI. Then run ./deploy.sh script in project root.



