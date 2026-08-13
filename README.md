# MyMoney

Простой веб-трекер доходов и расходов.  
Приложение: [mymoney-webapp.onrender.com](https://mymoney-webapp.onrender.com)

## Содержание

> ➠ [Описание](#-описание)  
> ➠ [Функционал](#-функционал)  
> ➠ [Технологический стек](#-технологический-стек)  
> ➠ [Запуск](#-запуск)   
> ➠ [Структура проекта](#-структура-проекта)  

---

## Описание

MyMoney — небольшое демонстрационное веб-приложение для учёта личных финансов.  
Позволяет добавлять доходы и расходы, смотреть текущий баланс, список транзакций за текущий месяц и статистику расходов по категориям.

Данные хранятся **в памяти** (in-memory). При перезапуске сервера список транзакций сбрасывается, но при старте автоматически подгружаются демо-данные (версия приложения несёт демонстрационный характер и предполагается как база для автоматизированного тестирования).

Фронтенд — React (собранный бандл в `static`)  
Бэкенд — Java (Spring Boot).

---

## Функционал

### UI
- [x] Главная страница: баланс, список транзакций за месяц, статистика по категориям
- [x] Добавление дохода (модальное окно)
- [x] Добавление расхода с выбором категории (модальное окно)
- [x] Удаление транзакции
- [x] Валидация суммы и комментария
- [x] Переключение статистики на узких экранах
- [x] Цветовая визуализация категорий

### Backend / API
- [x] `GET /` — главная страница
- [x] `GET /api/balance` — текущий баланс
- [x] `GET /api/monthly-transactions` — транзакции текущего месяца
- [x] `GET /api/transactions/month` — статистика расходов за месяц
- [x] `GET /api/categories` — список категорий расходов
- [x] `POST /api/income` — добавление дохода
- [x] `POST /api/expense` — добавление расхода
- [x] `DELETE /api/transactions/{id}` — удаление транзакции
- [x] Валидация входных данных и обработка ошибок

### Категории расходов
| Код           | Название            |
|---------------|---------------------|
| FOODSTUFF     | Продукты            |
| TRANSPORT     | Транспорт           |
| RESTAURANTS   | Кафе и рестораны    |
| ENTERTAINMENT | Развлечения         |
| HOUSE         | Товары для дома     |
| UTILITIES     | Коммуналка          |
| SPORT         | Спорт               |
| EDUCATION     | Образование         |
| MEDICINE      | Медицина            |
| OTHER         | Другое              |

---

## Технологический стек

- **Java 21**
- **Spring Boot 4** (Web MVC + Validation)
- **Maven**
- **React** (собранный фронтенд в `src/main/resources/static`)
- **Docker** (Dockerfile для деплоя)
- **In-memory хранилище** (без БД, демонстрационная версия)

---

## Запуск

### Локально
### Сборка и запуск  
(bash)  
mvn clean package  
java -jar target/MyMoney-1.0-SNAPSHOT.jar  
Или через Spring Boot:  
Bashmvn spring-boot:run  
Приложение будет доступно по адресу:  
http://localhost:8080  
(порт берётся из переменной окружения PORT, по умолчанию 8080)  
### Через Docker  
Bashdocker build -t mymoney.  
docker run -p 8080:8080 mymoney  
### Деплой  
Приложение развёрнуто на Render:  
https://mymoney-webapp.onrender.com  
> Особенности бесплатного хостинга  
> • После 15 минут бездействия приложение «засыпает». При первом открытии может потребоваться около минуты на «пробуждение».  
> • Для доступа может потребоваться VPN.

---

## Структура проекта
MyMoney/  
├── Dockerfile  
├── application.properties  
├── pom.xml  
└── src/main/  
    ├── java/mymoney/  
    │   ├── Main.java                 # Точка входа  
    │   ├── component/  
    │   │   └── DataInitializer.java  # Демо-данные при старте  
    │   ├── controller/  
    │   │   ├── StartController.java  # Отдача index.html  
    │   │   └── TransactionController.java  
    │   ├── dto/                      # Request/Response DTO  
    │   ├── exception/                # Обработка ошибок  
    │   ├── model/                    # Transaction, Category, Type  
    │   └── service/  
    │       └── TransactionService.java  
    └── resources/static/             # React-бандл (index.html, JS, CSS)  

---

# Полезные ссылки
Приложение: [mymoney-webapp.onrender.com ](https://mymoney-webapp.onrender.com/)  
Автотесты: [Schwerelosigkeit/MyMoney_AutoTesting](https://github.com/Schwerelosigkeit/MyMoney_AutoTesting)    
Allure-отчёт автотестов: [ссылка в репозитории автотестов ](https://schwerelosigkeit.github.io/MyMoney_AutoTesting/) 
