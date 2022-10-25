# Курсовой проект "Сетевой чат"

## Описание проекта

Вам нужно разработать два приложения для обмена текстовыми сообщениями по сети с помощью консоли (терминала) между двумя и более пользователями.

**Первое приложение - сервер чата**, должно ожидать подключения пользователей.

**Второе приложение - клиент чата**, подключается к серверу чата и осуществляет доставку и получение новых сообщений.

Все сообщения должны записываться в file.log как на сервере, так и на клиентах. File.log должен дополняться при каждом запуске, а также при отправленном или полученном сообщении. Выход из чата должен быть осуществлен по команде exit.

## Требования к серверу

- Установка порта для подключения клиентов через файл настроек (например, settings.txt);
- Возможность подключиться к серверу в любой момент и присоединиться к чату;
- Отправка новых сообщений клиентам;
- Запись всех отправленных через сервер сообщений с указанием имени пользователя и времени отправки.

## Требования к клиенту

- Выбор имени для участия в чате;
- Прочитать настройки приложения из файла настроек - например, номер порта сервера;
- Подключение к указанному в настройках серверу;
- Для выхода из чата нужно набрать команду выхода - “/exit”;
- Каждое сообщение участников должно записываться в текстовый файл - файл логирования. При каждом запуске приложения файл должен дополняться.

## Требования в реализации

- Сервер должен уметь одновременно ожидать новых пользователей и обрабатывать поступающие сообщения от пользователей;
- Использован сборщик пакетов gradle/maven;
- Код размещен на github;
- Код покрыт unit-тестами.

## Шаги реализации:

1. Нарисовать схему приложений;
2. Описать архитектуру приложений (сколько потоков за что отвечают, придумать протокол обмена сообщениями между приложениями);
3. Создать репозиторий проекта на github;
4. Написать сервер;
5. Провести интеграционный тест сервера, например с помощью telnet;
6. Написать клиент;
7. Провести интеграционный тест сервера и клиента;
8. Протестировать сервер при подключении нескольких клиентов;
9. Написать README.md к проекту;
10. Отправить на проверку.

# Описание предоставленного решения к проекту
 
## [Сервер](https://github.com/neo7976/Java-6-Homeworks-Multithreading-6-Course/blob/abb732216266eeb5221c565e48a74bc1e4baa2bc/src/main/java/Server.java)
### Сервер используется для принятия подключений пользователей и получения от них сообщений
- [ServLogger.java](https://github.com/neo7976/Java-6-Homeworks-Multithreading-6-Course/blob/abb732216266eeb5221c565e48a74bc1e4baa2bc/src/main/java/log/ServLogger.java)
 Производит запись событий сервера в отдельный лог файла.
- Запись в файл [setting](https://github.com/neo7976/Java-6-Homeworks-Multithreading-6-Course/blob/5bff8cd39e02f240c97360c8128aedfb8c4ca5b7/src/main/java/Server.java#L33-L39) настроек сервера, откуда пользователь считывает настройки и подключается
- [MyLogger.java](https://github.com/neo7976/Java-6-Homeworks-Multithreading-6-Course/blob/abb732216266eeb5221c565e48a74bc1e4baa2bc/src/main/java/log/MyLogger.java)
Служит для записи сообщений от пользователей
- Устанавливаем поток для принятия новых пользователей в [MonoThreadClientHandler](https://github.com/neo7976/Java-6-Homeworks-Multithreading-6-Course/blob/abb732216266eeb5221c565e48a74bc1e4baa2bc/src/main/java/thread/MonoThreadClientHandler.java), откуда происходит дальнейшее чтение сообщений каждого польователя отдельно
- [Поток чтения команд сервера](https://github.com/neo7976/Java-6-Homeworks-Multithreading-6-Course/blob/abb732216266eeb5221c565e48a74bc1e4baa2bc/src/main/java/Server.java#L32-L52)

##  MonoThreadClientHandler.java - поток для обработки [сообщений](https://github.com/neo7976/Java-6-Homeworks-Multithreading-6-Course/blob/abb732216266eeb5221c565e48a74bc1e4baa2bc/src/main/java/thread/MonoThreadClientHandler.java)

- Получаем [имя пользователя](https://github.com/neo7976/Java-6-Homeworks-Multithreading-6-Course/blob/abb732216266eeb5221c565e48a74bc1e4baa2bc/src/main/java/thread/MonoThreadClientHandler.java#L27-L29), чтобы знать, от кого приходят сообщений и под его именем записывать в файл сообщение

- Получение, отправка и запись [сообщений](https://github.com/neo7976/Java-6-Homeworks-Multithreading-6-Course/blob/abb732216266eeb5221c565e48a74bc1e4baa2bc/src/main/java/thread/MonoThreadClientHandler.java#L31-L48) от пользователя, пока не поступила команда "/end"

## [Client.java](https://github.com/neo7976/Java-6-Homeworks-Multithreading-6-Course/blob/abb732216266eeb5221c565e48a74bc1e4baa2bc/src/main/java/Client.java)

- Считываем [settings](https://github.com/neo7976/Java-6-Homeworks-Multithreading-6-Course/blob/abb732216266eeb5221c565e48a74bc1e4baa2bc/src/main/java/Client.java#L23-L38) настроек сервера для подключения

- Производим авторизацию и передаем серверу [имя пользователя](https://github.com/neo7976/Java-6-Homeworks-Multithreading-6-Course/blob/abb732216266eeb5221c565e48a74bc1e4baa2bc/src/main/java/Client.java#L40-L51)
- Создаем поток для постоянно считывания файла и проверка на новые [сообщения](https://github.com/neo7976/Java-6-Homeworks-Multithreading-6-Course/blob/abb732216266eeb5221c565e48a74bc1e4baa2bc/src/main/java/Client.java#L54-L55) и сам [код потока](https://github.com/neo7976/Java-6-Homeworks-Multithreading-6-Course/blob/abb732216266eeb5221c565e48a74bc1e4baa2bc/src/main/java/thread/ThreadReadMessage.java)

- Посылаем сообщения серверу для записи в [файл](https://github.com/neo7976/Java-6-Homeworks-Multithreading-6-Course/blob/abb732216266eeb5221c565e48a74bc1e4baa2bc/src/main/java/Client.java#L57-L85) до момента ввода "/end", тогда происходит разрыв канала.

## [ClientTest.java](https://github.com/neo7976/Java-6-Homeworks-Multithreading-6-Course/blob/abb732216266eeb5221c565e48a74bc1e4baa2bc/src/main/java/thread/ClientTest.java)

- Поток для тестирования подключения нескольких пользователей, которые запускаются через [Main.java](https://github.com/neo7976/Java-6-Homeworks-Multithreading-6-Course/blob/abb732216266eeb5221c565e48a74bc1e4baa2bc/src/main/java/Main.java)

## Пример записи в файл сообщений от тестированных нескольких пользоваталей
[Ссылка на log](https://github.com/neo7976/Java-6-Homeworks-Multithreading-6-Course/blob/188fa9c71697029c2a08db616b0554a06a2deb99/src/main/resources/log.start.log)

