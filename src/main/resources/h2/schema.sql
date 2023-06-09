CREATE  TABLE stockPriceInfo (
    id identity not null primary key,
    code VARCHAR2 not null,
    name VARCHAR2 not null,
    marketIndex varchar2 not null,
    price  NUMERIC not null,
    updown numeric,
    rate float,
    volume NUMERIC
);

CREATE  TABLE marketIndex (
    id identity not null primary key,
    code VARCHAR2 not null,
    name VARCHAR2 not null,
    value  float not null,
    updown float,
    rate float
);

commit;