package com.jmc.binaria.sender.model;

public class Customer {
  
  private int id;
  
  private String number;
  
  private String name;
  
  private FtpSettings ftpSettings;
  
  private SmtpSettings smtSettings;

  public int getId() {
    return id;
  }

  public void setId(int id) {
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

  public SmtpSettings getSmtSettings() {
    return smtSettings;
  }

  public void setSmtSettings(SmtpSettings smtSettings) {
    this.smtSettings = smtSettings;
  }
  
}
