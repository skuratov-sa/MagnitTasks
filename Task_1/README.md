### Задание 1.
Написать тест, который будет отправлять сообщения в брокер и принимать в разных режимах как сессий
(AUTO_ACK, TRANSACTED, etc.), так и сообщений (персистентные и неперсистентные).

Оформить итоговый файл с результатами записи и чтения в каждом режиме. Объяснить полученные результаты. 

Для запуска:
- Установить дистрибутивы jdk 14 и выше, mvn
- запустить [activeMQ сервер](https://downloads.apache.org/activemq/5.17.3/)
- запустить [ProducerRunnerTest.java](src%2Ftest%2Fjava%2Fcom%2Fskuratov%2Ftask1%2FProducerRunnerTest.java)
- посмотреть логи [task_1.log](logs%2Ftask_1.log), или только сгенерированные(session.log)


### Задание 1.2
В задаче 1 использовать embedded брокер и VM-коннектор, после чего снова проверить все режимы.
Сравнить скорость работы по сравнению с использованием tcp коннектора. Объяснить полученные результаты.

Для запуска:
- Настроить среду
- Запустить тесты [EmbeddedBroker.java](src%2Ftest%2Fjava%2Fcom%2Fskuratov%2Ftask2%2FEmbeddedBroker.java)
- посмотреть логи [embedded_broker.log](logs%2Fembedded_broker.log), или только сгенерированные(session.log)