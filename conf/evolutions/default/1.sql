# --- !Ups

CREATE TABLE PRODUCT (
    id UUID PRIMARY KEY,
    title varchar NOT NULL,
    price varchar NOT NULL,
    currency varchar(3) NOT NULL ,
    image_url varchar NULL,
    new boolean NOT NULL DEFAULT TRUE,
    quantity int NOT NULL DEFAULT 0,
    created_date timestamp NOT NULL DEFAULT NOW(),
    last_update timestamp NOT NULL DEFAULT NOW(),
    created_by varchar NOT NULL
);


CREATE TABLE USER_SESSION (
    id  UUID PRIMARY KEY,
    user_agent varchar NOT NULL,
    host varchar NOT NULL
);


# --- !Downs
DROP TABLE PRODUCT;
DROP TABLE USER_SESSION;