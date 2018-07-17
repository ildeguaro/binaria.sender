DROP TABLE IF EXISTS sender_campaigns;
CREATE TABLE sender_campaigns (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  uuid varchar(45) DEFAULT NULL,
  name varchar(45) DEFAULT NULL,
  customer_id bigint(20) DEFAULT NULL,
  orden_impresion_id bigint(20) DEFAULT NULL,
  creation_date timestamp NULL DEFAULT NULL,
  html_template LONGTEXT NOT NULL,
  sending_begin timestamp NULL DEFAULT NULL,
  sending_end timestamp NULL DEFAULT NULL,
  sending_duration int(11) DEFAULT NULL,
  status varchar(45) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS sender_email_campaign;
CREATE TABLE sender_email_campaign(
  id bigint(20) NOT NULL AUTO_INCREMENT,
  sender_campaigns_id bigint(20),
  addresses varchar(256) NOT NULL,
  names varchar(200) NOT NULL,
  package_id varchar(256) NULL, 
  document_id bigint(20) NULL,
  attachment_path varchar(1024) NOT NULL,
  content_email LONGTEXT NOT NULL,
  sending_date timestamp NULL DEFAULT NULL,
  sender_id bigint(20) NULL DEFAULT NULL,
  sent tinyint(1) NOT NULL DEFAULT 0,
  category varchar(56) NULL DEFAULT NULL,
  esmtp_id varchar(40) NULL DEFAULT NULL,
  response varchar(512) NULL DEFAULT NULL,
  error varchar(512) DEFAULT NULL,  
  fields_search varchar(1024) NULL DEFAULT NULL,  
  sender_assigned_id bigint(20),  
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS sender_email_events;
CREATE TABLE sender_email_events(
  id bigint(40) NOT NULL AUTO_INCREMENT,
  sender_email_campaign_id bigint(20),
  esmtp_id varchar(40) NULL DEFAULT NULL,
  event_type varchar(200) NOT NULL,
  event_date timestamp NULL DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS sender_email_category;
CREATE TABLE sender_email_category(
  id bigint(40) NOT NULL AUTO_INCREMENT,
  sender_email_campaign_id bigint(20),
  category varchar(56) NULL DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS sender_senders;
CREATE TABLE sender_senders(
  id bigint(20) NOT NULL AUTO_INCREMENT,
  name varchar(125), 
  uri  varchar(125), 
  assignation_type varchar(125) NULL DEFAULT 'ANY', -- TO_ME OR ANY
  customer_id bigint(20) DEFAULT NULL,
  enabled tinyint(1) NOT NULL DEFAULT 0,
  debug tinyint(1) NOT NULL DEFAULT 0,
  pid  varchar(10) NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS sender_events;
CREATE TABLE sender_events(
  id bigint(20) NOT NULL AUTO_INCREMENT,
  sender_id bigint(20),  
  event_date timestamp NULL DEFAULT NULL,  
  event varchar(512) NULL DEFAULT NULL,  
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS sender_campaigns_files;
CREATE TABLE sender_campaigns_files(
  id bigint(20) NOT NULL AUTO_INCREMENT,
  sender_campaigns_id bigint(20),  
  file_id varchar(200) NULL DEFAULT NULL,  
  file_path text NULL DEFAULT NULL,
  metadata text NULL DEFAULT NULL,  
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS sender_stat_type;
CREATE TABLE sender_stat_type(
  id int(4) NOT NULL,
  name varchar(56) NULL DEFAULT NULL,
  description varchar(256) NULL DEFAULT NULL,
  icon_path varchar(128) NULL DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT unique_id UNIQUE (id),
  CONSTRAINT unique_name UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS sender_campaign_stat;
CREATE TABLE sender_campaign_stat(
  id bigint(40) NOT NULL AUTO_INCREMENT,
  sender_email_campaign_id bigint(40) NOT NULL,
  sender_stat_type_id bigint(40) NOT NULL,
  value_count int(40) NOT NULL DEFAULT 0,
  value_percent double NOT NULL DEFAULT 0.00,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO sender_stat_type(id,name,description,icon_path) 
  VALUES (1,'request', 'The number of emails that were requested to be delivered.', '/images/icons/stats/requests.png');
INSERT INTO sender_stat_type(id,name,description,icon_path) 
  VALUES (2,'processed', 'Requests from your website, application, or mail client via SMTP Relay or the API that SendGrid processed.', '/images/icons/stats/processed.png');
INSERT INTO sender_stat_type(id,name,description,icon_path) 
  VALUES (3,'delivered', 'The number of emails SendGrid was able to confirm were actually delivered to a recipient.', '/images/icons/stats/delivered.png');
INSERT INTO sender_stat_type(id,name,description,icon_path) 
  VALUES (4,'open', 'The total number of times your emails were opened by recipients.', '/images/icons/stats/opens.png');

DROP VIEW IF EXISTS sender_view_stats_global_category;
CREATE VIEW sender_view_stats_global_category AS
SELECT email.sender_campaigns_id AS campaigns_id, cat.category, COUNT(email.id) as count
FROM sender_email_campaign email
JOIN sender_email_category cat ON cat.sender_email_campaign_id = email.id
WHERE email.sent
AND email.sender_campaigns_id = 18
GROUP BY 1,2;  
  
DROP VIEW IF EXISTS sender_view_stats_global;
CREATE VIEW sender_view_stats_global AS 
SELECT email.sender_campaigns_id AS campaigns_id,even.event_type AS 'event', stat.description, stat.icon_path, COUNT(email.id) AS 'count' 
FROM sender_email_campaign email
JOIN sender_email_events even ON even.esmtp_id = email.esmtp_id
JOIN sender_stat_type stat ON stat.name = even.event_type
WHERE email.sent
GROUP BY 1,2,3,4;

DROP VIEW IF EXISTS sender_view_stats_by_category;
CREATE VIEW sender_view_stats_global AS 
SELECT email.sender_campaigns_id AS campaigns_id, cat.category, even.event_type AS 'event', stat.description, stat.icon_path, COUNT(email.id) AS 'count' FROM sender_email_campaign email
JOIN sender_email_category cat ON cat.sender_email_campaign_id = email.id
JOIN sender_email_events even ON even.esmtp_id = email.esmtp_id
JOIN sender_stat_type stat on stat.name = even.event_type
WHERE email.sent
GROUP BY 1,2,3,4,5;
