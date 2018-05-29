package com.jmc.binaria.sender.model;

public class Customer {
  
  private String id;
  
  private String number;
  
  private String name;
  
  private FtpSettings ftpSettings;
  
  private SmtpSettigs smtSettings;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public FtpSettings getFtpSettings() {
    return ftpSettings;
  }

  public void setFtpSettings(FtpSettings ftpSettings) {
    this.ftpSettings = ftpSettings;
  }

  public SmtpSettigs getSmtSettings() {
    return smtSettings;
  }

  public void setSmtSettings(SmtpSettigs smtSettings) {
    this.smtSettings = smtSettings;
  }
  
}
