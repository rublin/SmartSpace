alter table zones drop column security_changed;
alter table zones add security_changed timestamp;

