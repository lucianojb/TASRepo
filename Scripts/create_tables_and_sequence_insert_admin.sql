drop table if exists user_roles;
drop table if exists admin_users;
drop table if exists down_schedule;
drop table if exists app_conns;
drop table if exists connections;
drop table if exists health_payload;
drop table if exists tas_app;
drop sequence if exists tas_app_app_id_seq;
drop sequence if exists down_schedule_sched_id_seq;
drop sequence if exists app_conns_conn_id_seq;
drop sequence if exists connections_conn_id_seq;
drop sequence if exists health_payload_health_id_seq;

create sequence tas_app_app_id_seq;
create sequence down_schedule_sched_id_seq;
create sequence app_conns_conn_id_seq;
create sequence health_payload_health_id_seq;
create sequence connections_conn_id_seq;


CREATE TABLE tas_app (
	app_name varchar NULL,
	url varchar NULL,
	version_num varchar NULL,
	app_id int8 NOT NULL DEFAULT nextval('tas_app_app_id_seq'::regclass),
	uppdated_date timestamp NULL DEFAULT now(),
	active bool NULL,
	uptimestart timestamp NULL,
	CONSTRAINT tas_app_pkey PRIMARY KEY (app_id)
)
WITH (
	OIDS=FALSE
);

CREATE TABLE down_schedule (
	sched_id int4 NOT NULL DEFAULT nextval('down_schedule_sched_id_seq'::regclass),
	app_id int4 NOT NULL,
	start_date timestamp NOT NULL,
	end_date timestamp NOT NULL,
	uppdated_date timestamp NULL,
	CONSTRAINT down_schedule_pkey PRIMARY KEY (sched_id),
	CONSTRAINT app_id FOREIGN KEY (app_id) REFERENCES public.tas_app(app_id) ON UPDATE CASCADE
)
WITH (
	OIDS=FALSE
);

CREATE TABLE app_conns (
	conn_name varchar not NULL,
	conn_id int8 NOT NULL DEFAULT nextval('app_conns_conn_id_seq'::regclass),
	app_id int4 NOT NULL,
	uppdated_date timestamp NULL DEFAULT now(),
	priority bool not NULL,
	CONSTRAINT app_conns_pkey PRIMARY KEY (conn_id),
	CONSTRAINT app_id FOREIGN KEY (app_id) REFERENCES public.tas_app(app_id) ON UPDATE CASCADE
)
WITH (
	OIDS=FALSE
);

CREATE TABLE health_payload (
	health_id int4 NOT NULL DEFAULT nextval('health_payload_health_id_seq'::regclass),
	app_id int4 not null,
	error_message varchar,
	result_value int4,
	uppdated_date timestamp NULL,
	CONSTRAINT health_payload_pkey PRIMARY KEY (health_id),
	CONSTRAINT app_id FOREIGN KEY (app_id) REFERENCES public.tas_app(app_id) ON UPDATE CASCADE
)
WITH (
	OIDS=FALSE
);

CREATE TABLE connections (
	conn_id int4 NOT NULL DEFAULT nextval('connections_conn_id_seq'::regclass),
	conn_name varchar not null,
	details varchar NOT NULL,
	app_id int4 not null,
	functional boolean,
	expected boolean not null,
	priority boolean not null,
	uppdated_date timestamp NULL,
	CONSTRAINT connections_pkey PRIMARY KEY (conn_id),
	CONSTRAINT app_id FOREIGN KEY (app_id) REFERENCES public.tas_app(app_id) ON UPDATE CASCADE
)
WITH (
	OIDS=FALSE
);

CREATE table admin_users (
	username varchar(45) NOT NULL,
	password varchar(60) NOT NULL,
	enabled int4 NOT NULL DEFAULT 1,
	CONSTRAINT admin_users_pkey PRIMARY KEY (username)
)
WITH (
	OIDS=FALSE
);

CREATE TABLE user_roles (
	username varchar(45) NOT NULL,
	"role" varchar(45) NOT NULL,
	CONSTRAINT user_roles_pkey PRIMARY KEY (username),
	CONSTRAINT username FOREIGN KEY (username) REFERENCES public.admin_users(username)
)
WITH (
	OIDS=FALSE
);

insert into admin_users values ('tps.admin', '$2a$10$LjZHrywN2fV3Q8pEA8yrIeMa1CiuNC3TBadg2J2fjm9/BlvYkPp/6', 1);

insert into user_roles values ('tps.admin', 'ROLE_ADMIN');