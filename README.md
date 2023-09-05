# Бизнес-логика программных систем. Лабораторная работа №1 #

Бордун А.В.

## Вариант №11102/9909320 ##
Aviasales.ru Поиск Дешевых Авиабилетов по 728 Авиакомпаниям - http://www.aviasales.ru/

![image](https://user-images.githubusercontent.com/22819920/228567258-2bf3bd16-dee9-4fd8-889d-5caac2cc4527.png)
![image](https://github.com/Bordsiya/aviasales/assets/22819920/318514b8-57b7-45c8-b709-d6b5bced674b)
![изображение](https://github.com/Bordsiya/aviasales/assets/22819920/dd1cffe9-2518-435f-960a-f0aeb6e0946f)


# BPMN (1-2 labs) #

![bpmn-1](https://user-images.githubusercontent.com/22819920/228568057-e37499b7-ae11-4635-bf1f-9a284126a0d4.png)
![bpmn-2](https://github.com/Bordsiya/aviasales/blob/master/docs/diagram_2lab.png)

# Architecture (3lab) #

![architecture](https://github.com/Bordsiya/aviasales/blob/master/docs/architecture_3lab.jpg)

# Database Diagram #

![db_diagram](https://user-images.githubusercontent.com/22819920/234403414-b32aa065-c897-4c80-bff2-4253de8b20c9.png)
![uml_users](https://github.com/Bordsiya/aviasales/assets/22819920/c1582d87-2012-457a-a884-54106f9e2906)
![db_3_lab](https://github.com/Bordsiya/aviasales/blob/master/docs/add_db_elements_3lab.png)

# API #
http://localhost:9095/swagger-ui/index.html#/

# Система рекоммендаций #
В качестве алгоритма машинного обучения используется коллаборативная фильтрация. В качестве рейтинга используется количество посещений того или иного города.
Основа для матрицы - таблица city_experience(id, user_id, city, scrobbles).

Когда может выдать рекоммендацию?
- мы можем найти пользователей, у которых 2 и более совпадающих с нашими городов в списке посещенных
- у нашего пользователя более 1 посещенного города
- оставшиеся непосещенные города в списках у наиболее близких соседей должны встретиться более 2 раз

В противном случае, рекоммендация не сможет быть выдана из-за малого количества исходных данных.

Ссылка на статью о подобном методе:
https://mindy-dossett.com/2021/04/18/collaborative-filtering-in-Java/
