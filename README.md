# wsockauth

JavaEE Winsock Authorization & Authentification demo

Проект собран в NetBeans. Использованы GlassFish 4.1 и DerbyDB, которые входят в стандартную инсталляцию NetBeans для JavaEE.

# Основные классы:
- WsockEndpoint - обслуживание HTTP-запросов
- MessageProcessor - обработка сообщений от пользователя
- Messaage - класс для объектов сообщений
- DbConnection - класс, обслуживающий работу с базой данных (схема WAUSERBOT, connectionURL = "jdbc:derby://localhost:1527/wsockauth")

# Порядок работы пользователя:
- существуют два типа пользователей: ROLE_ADMIN и ROLE_USER
- пользователь через страницу http://localhost:8080/WsockAuth/ может ввести E-mail и пароль для авторизации
- если таблица USERS пустая - будет зарегистрирован пользователь для ROLE_ADMIN
- при наличии прав ROLE_ADMIN - можно вводить новые пары E-mail/пароль и регистрировать пользователей (с правами ROLE_USER)
- можно вводить в дополнительное поле произвольные сообщения (кнопка Message) - в ответ будет получено эхо от сервера
- сессия заканчивается по кнопке Stop
 
Справа от панели кнопок и полей формы, находится поле для вывода сообщений.

Каждый пользователь на время работы сессии получает Token: fbb93741-9719-4335-98a1-5802df202cbf, который имеет время действия Expiration: 02016-25-23T03:25:46Z

Во время сессии, токен для каждого пользователя храниться в таблице TOKENS

Если производится повторная авторизация - пользователь получает новый токен, а старый записывается в таблицу TOKENHISTORY

Обмен сообщениями ведется через WebSocket в формате JSON, с заданными для приложения спецификациями.

# TODO:

1. Использовать закрытый протокол OAuth для авторизации
2. Использовать JPA для работы с базой данных
