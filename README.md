# techradar-api
[![Deploy](https://github.com/hhru-school/techradar-api/actions/workflows/publish_action.yml/badge.svg)](https://github.com/hhru-school/techradar-api/actions/workflows/publish_action.yml)
[![Merge tests](https://github.com/hhru-school/techradar-api/actions/workflows/merge_action.yml/badge.svg)](https://github.com/hhru-school/techradar-api/actions/workflows/merge_action.yml)
[![Pull request tests](https://github.com/hhru-school/techradar-api/actions/workflows/pull_request_action.yml/badge.svg)](https://github.com/hhru-school/techradar-api/actions/workflows/pull_request_action.yml)
## Запуск миграций БД локально
#### mvn liquibase:update -Denv=local 
## Запуск миграций БД продакшн
#### mvn liquibase:update -Denv=prod 
