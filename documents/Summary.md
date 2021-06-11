#Итоги автоматизации
## Что было запланировано и что было сделано
* Позитивные сценарии:
    * Ввод корректных персональных данных в форму: протестировано отсутствие уведомлений об ошибках при вводе корректных данных в поля формы.
    * Покупка валидной картой с достаточным количеством средств: протестирована обработка формы при использовании валидной карты и оплатой средствами карты.
    * Покупка с использованием валидной карты в кредит: протестирована отправка обработка при использовании валидной карты и оплатой покупки в кредит.
* Негативные сценарии:
    * Ввод некорретных персональных данных в форму: протестировано, что пустая или некорректно заполненая форма не отправляется и уведомляет пользователя об ошибках.
    * Покупка валидной картой с недостаточным количеством средств: (дублирующий два последующих пункта сценарий, не тестировался)
    * Покупка с использованием невалидной карты с карты: протестирована отправка формы с невалидной картой и получение соответствующего уведомления пользователем о неуспешной операции.
    * Покупка с использованием невалидной карты в кредит: протестирована отправка формы с невалидной картой и получение соответствующего уведомления пользователем о неуспешной операции.
## Причины, по которым что-то не было сделано
* Сценарий "Покупка валидной картой с недостаточным количеством средств" не был протестирован, т.к. по сути в рамках тестовых данных невалидная карта и подразумевала в себе недостаточность средств или др. ограничения, информацию о которых предоставляли банковские сервисы.
## Сработавшие риски
* Изучение особенностей подключения и обмена с банковскими сервисами: ранее не пробовал настраивать взаимодействие по REST API.
    * Доп. время было затрачено на изучение подходов к опросу API и получения статуса операции.
* Сложности при подключении к БД с различными СУБД: ошибки авторизации, ошибки настройки коннектора. 
    * Доп. время было затрачено на правильное формулирование сервисного класса по работе с БД, т.к. не было полного понимания как правильно прятать классы и как корректно описывать методы.
## Общий итог по времени (сколько запланировали/сколько потратили с обоснованием расхождения)
Запланированное время: 24 часа\
Фактическое время: 26 часов\
Учёт велся поверхностный, ориентировочно потратил столько времени сколько и планировал, но между этапами работ возможна реклассификация (на что-то потратил больше времени, на что-то меньше, чем ожидалось).\
Перерасход по часам возник из-за сложностей при описании методов по работе с БД и API. А также из-за незапланированных правок проекта. 