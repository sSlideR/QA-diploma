# Дипломный проект профессии «Тестировщик»
Дипломный проект представляет собой автоматизацию тестирования комплексного сервиса, взаимодействующего с СУБД и API Банка.

## Документация по проекту
[Plan.md](documents/Plan.md) - планирование процедур по автоматизации и подготовке отчётной документации.\
[Report.md](documents/Report.md) - отчёт по итогам тестирования. \[ToDo\] \
[Summary.md](documents/Summary.md) - отчёт о результатах автоматизации. \[ToDo\]

## Тестирование

### Выполнение авто-тестов
Для выполения автотестов необходимо выполнить следующие шаги (команды указаны для запуска на Windows):
1. Установить и запустить приложение Docker Desktop ([Инструкция](https://github.com/netology-code/aqa-homeworks/blob/master/docker/installation.md)).<br><br>
1. Собрать нужные образы и запустить контейнеры командой:<br>
```docker-compose up -d --build```<br><br>
_Примечание: Запустится как контейнер с MySQL базой данных, так и с PostgreSQL._<br>
_Запуск контейнеров занимает 1-2 минуты в зависимости от оборудования_<br><br>
**Как узнать, что запуск успешен?**<br>
После ввода команды ```docker-compose logs``` необходимо посмотреть последние статусы для каждого из контейнеров:<br>
    * mysql - "X Plugin ready for connections"
    * postgresql - "database system is ready to accept connections"
    * gate-simulator - JSON-данные из файла ./artifacts/gate-simulator/data.json<br><br>
1. Запустить приложение (SUT) командой:
    * Для тестирования SUT в паре с MySQL базой данных:\
```java -Dspring.datasource.url=jdbc:mysql://localhost:3306/app -jar ./artifacts/aqa-shop.jar```
    * Для тестирования SUT в паре с PostgreSQL базой данных:\
```java -Dspring.datasource.url=jdbc:postgresql://localhost:5432/app -jar ./artifacts/aqa-shop.jar```<br>
    * Опциональные параметры запуска SUT:\
    ```-Dspring.datasource.url= - адрес БД (по умолчанию - jdbc:mysql://localhost:3306/app) ```\
    ```-Dspring.datasource.username= - имя пользователя БД```\
    ```-Dspring.datasource.password= - пароль доступа к БД```<br><br>
_Примечание: для запуска экземпляра SUT с другими параметрами, необходимо остановить запущенный экземпляр SUT._\
_Для остановки запущенного экземпляра SUT необходимо закрыть окно терминала, из которого оно было запущено._<br><br>
1. Запустить автоматизированное тестирование командой:
    * Для тестирования SUT в паре с MySQL базой данных:\
```gradlew clean test allureReport -Ddatasource.url=jdbc:mysql://localhost:3306/app```
    * Для тестирования SUT в паре с PostgreSQL базой данных:\
```gradlew clean test allureReport -Ddatasource.url=jdbc:postgresql://localhost:5432/app```
    * Опциональные параметры запуска тестов:\
    ```-Ddatasource.url - адрес БД (по умолчанию - jdbc:mysql://localhost:3306/app)```\
    ```-Ddatasource.username= - имя пользователя БД```\
    ```-Ddatasource.password= - пароль доступа к БД```\
    ```-Dsut.url= - адрес, на котором развернут SUT (по умолчанию: http://localhost)```\
    ```-Dsut.port= - порт, на котором развернут SUT (по умолчанию: 8080)```\
    ```-Dapi.url= - адрес, на котором развернут PaymentGate и CreditGate (по умолчанию: http://localhost)```\
    ```-Dapi.port= - порт, на котором развернут PaymentGate и CreditGate (по умолчанию: 9999)```\<br><br>
1. После проведения автоматизированного тестирования закрыть контейнеры командой:\
```docker-compose down```

### Отчётные документы по итогам тестирования
После выполнения авто-тестов Вам будет доступна отчётность, сгенерированная сервисом Allure. 
Команда для запуска сервиса, предоставляющего интерактивный интерфейс для просмотра отчёта Allure:\
```gradlew allureServe```\
Для закрытия сервиса необходимо в терминале, из которого осуществлялся запуск сервиса, ввести:\
```Ctrl+C -> Y -> Enter```<br><br>
_Примечание: После прогона каждого из тестов (запуска gradlew test), необходимо перезапускать сервис allureServe Для получения актуальной информации о результатах авто-тестов._