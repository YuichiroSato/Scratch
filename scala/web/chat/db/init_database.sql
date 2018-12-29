CREATE ROLE chatuser WITH SUPERUSER LOGIN;
ALTER ROLE chatuser WITH PASSWORD 'chat_user_password';
CREATE DATABASE chat OWNER chatuser;
