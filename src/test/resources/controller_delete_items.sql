UPDATE items
SET user_id = null;

DELETE FROM items;

DELETE FROM users;

ALTER TABLE ITEMS
    ALTER COLUMN id RESTART WITH 1;

ALTER TABLE USERS
    ALTER COLUMN id RESTART WITH 1;