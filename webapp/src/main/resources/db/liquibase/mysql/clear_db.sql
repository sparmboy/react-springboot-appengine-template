drop table if exists USER_ROLES;
drop table if exists ROLE_PERMISSIONS;
drop table if exists PERMISSIONS;
drop table if exists ROLE;
drop table if exists USER;
truncate DATABASECHANGELOG;
truncate DATABASECHANGELOGLOCK;
commit;
