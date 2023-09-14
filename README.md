# Бизнес-логика программных систем. Лабораторная работа №1 #

Бордун А.В.

## ТЗ ##
Aviasales.ru Поиск Дешевых Авиабилетов по 728 Авиакомпаниям - http://www.aviasales.ru/

![image](https://user-images.githubusercontent.com/22819920/228567258-2bf3bd16-dee9-4fd8-889d-5caac2cc4527.png)
![image](https://github.com/Bordsiya/aviasales/assets/22819920/318514b8-57b7-45c8-b709-d6b5bced674b)
![изображение](https://github.com/Bordsiya/aviasales/assets/22819920/dd1cffe9-2518-435f-960a-f0aeb6e0946f)
![изображение](https://github.com/Bordsiya/aviasales/assets/22819920/98833320-27e5-4ed8-89b1-f0e3e08c61fc)



# BPMN #
Авторизация:

![изображение](https://github.com/Bordsiya/aviasales/assets/22819920/09d49b4c-a834-4380-9951-039ca2b018a9)


Добавление пассажиров:

![изображение](https://github.com/Bordsiya/aviasales/assets/22819920/758fd1b3-3457-4753-8f88-685056add5b2)

Остальные бизнес-процессы имеют схему, похожую на приложенную ниже.
Поиск полетов:

![изображение](https://github.com/Bordsiya/aviasales/assets/22819920/d911311f-f9b9-4718-a73a-06d2a4ddddf8)


# Architecture #

![Lab4Architecture](https://github.com/Bordsiya/aviasales/assets/22819920/ba89c0d5-fd67-4cfc-a6b4-de29608f23f7)


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
