  CREATE DATABASE LINK "DB.DOMAIN.XYZ"
   CONNECT TO "SCHEMA" IDENTIFIED BY VALUES ':1'
   USING 'conn_str';

create database link DB.DOMAIN.XYZ
  connect to DB_SCHEMA identified by passStr
  using 'conn_str';