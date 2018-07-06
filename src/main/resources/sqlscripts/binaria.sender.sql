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
  esmtp_id varchar(40) NULL DEFAULT NULL,
  response varchar(512) NULL DEFAULT NULL,
  error varchar(512) DEFAULT NULL,  
  fields_search varchar(1024) NULL DEFAULT NULL,  
  sender_assigned_id bigint(20),
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