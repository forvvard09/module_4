--Преобразование типов
--select (10.0 / 3)::numeric(10, 2);
--select (1)::bigint;
--select cast(10.0 / 3 as numeric(10,2));

--Текстовые типы данных
--char - 'abc' + 97 * ' '
--varchar - 'abc'
--text - 'abc'

--Временные типы данных
--select date '2022-11-06';
--select time with time zone '13:35:40.879 +2:00';
--select timestamp '2022-11-06 13:35:40';

--Получение данных путем прибавления секунд к 1970-01-01 00:00:00
--select to_timestamp(20);

--Интервалы
--select (date '2022-11-06') - (date '2022-10-06');
--select (date '2022-11-06') - interval '1 day 1 hour 1 minute';

--Бесконечность временная
--select timestamp with time zone 'infinity';

--Логические значения
--select '1'::boolean;
--select '0'::boolean;
--select null::boolean;

--create table bol (value boolean);
-- insert into bol (value) values(true);
-- insert into bol (value) values(null);

--Получение записей со значением null
-- select *
-- from bol
-- where value is null;

--Конкатенация строк
--select 'hello ' || 'world';

--Создание связи один ко одному
-- drop table if exists country;
-- drop table if exists capital;
-- create table country (id int generated always as identity primary key, name varchar(100));
--Осуществляется за счет добавление ключевого слова Unique к внешнему ключу
-- create table capital (id serial primary key, name varchar(100), country_id int references country(id) unique);

-- insert into country (name) values('Мексика');
-- insert into capital (name, country_id) values('Мехико', 1);
-- insert into capital (name, country_id) values('Париж', 2);
-- insert into capital (name, country_id) values('Теночтилан', 1);

--Создание связи один ко многим
-- drop table if exists country;
-- drop table if exists capital;
-- create table country (id int generated always as identity primary key, name varchar(100));
-- create table capital (id serial primary key, name varchar(100), country_id int references country(id));

-- insert into country (name) values('Мексика');
-- insert into capital (name, country_id) values('Мехико', 1);
-- insert into capital (name, country_id) values('Париж', 2);
-- insert into capital (name, country_id) values('Теночтилан', 1);


--Создание связи многие ко многим
-- drop table if exists relations;
-- drop table if exists country;
-- drop table if exists company;

-- create table country (id int generated always as identity primary key, name varchar(100));
-- create table company (id int generated always as identity primary key, name varchar(100));
-- create table relations(company_id int references company(id), country_id int references country(id), primary key (company_id, country_id));

-- insert into country (name) values('Мексика');
-- insert into company (name) values('Coca cola');
-- insert into company (name) values('Amazon');
-- insert into relations (company_id, country_id) values(1, 1);
-- insert into relations (company_id, country_id) values(2, 1);

-- select co.name, cp.name
-- from relations r inner join country co on co.id = r.country_id inner join company cp on r.company_id = cp.id

--Получение текущего значения sequence (последовательности)
--select currval('country_id_seq');
--Получение следующего значения sequence (последовательности)
--select nextval('country_id_seq');