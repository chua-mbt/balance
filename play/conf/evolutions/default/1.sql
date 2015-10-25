# --- !Ups

CREATE TABLE tags(
  tag_id BIGINT PRIMARY KEY,
  tag_name TEXT NOT NULL UNIQUE
);;

CREATE TABLE transactions(
  transaction_id BIGINT PRIMARY KEY,
  transaction_detail TEXT NOT NULL,
  transaction_value MONEY NOT NULL,
  transaction_date DATE NOT NULL
);;

# --- !Downs

DROP TABLE transactions;;
DROP TABLE tags;;