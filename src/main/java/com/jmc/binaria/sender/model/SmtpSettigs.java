package com.jmc.binaria.sender.model;

import java.util.Map;

public class SmtpSettigs {
  
  private String hostname;
  
  private int port;
  
  private String username;
  
  private String password;
  
  private String from;
  
  private String subject;
  
  private String body;
  
  private String attachmenName;
  
  private Map<String, Object> variables;

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public String getAttachmenName() {
    return attachmenName;
  }

  public void setAttachmenName(String attachmenName) {
    this.attachmenName = attachmenName;
  }

  public Map<String, Object> getVariables() {
    return variables;
  }

  public void setVariables(Map<String, Object> variables) {
    this.variables = variables;
  }
  
}
