insert into Users_sub  (id, user_real_id, email, telegram, change, max, min) values ('2021bd48-694e-11eb-9439-0242ac130002','1231', '', '12', 5, 40, 10 )
insert into Subscriptions(id, ticker, type_event) values ('07348360-694e-11eb-9439-0242ac130002','sber', 'eventChange')
insert into Sub_user(sub_id, user_id)  values ('07348360-694e-11eb-9439-0242ac130002', '2021bd48-694e-11eb-9439-0242ac130002')

insert into Subscriptions(id, ticker, type_event) values ('5fd20c80-694f-11eb-9439-0242ac130002','sber', 'eventMax')
insert into Sub_user(sub_id, user_id)  values ('5fd20c80-694f-11eb-9439-0242ac130002', '2021bd48-694e-11eb-9439-0242ac130002')

insert into Subscriptions(id, ticker, type_event) values ('6cbb625c-694f-11eb-9439-0242ac130002','sber', 'eventMin')
insert into Sub_user(sub_id, user_id)  values ('6cbb625c-694f-11eb-9439-0242ac130002', '2021bd48-694e-11eb-9439-0242ac130002')

