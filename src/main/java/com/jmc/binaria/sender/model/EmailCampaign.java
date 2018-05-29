package com.jmc.binaria.sender.model;

import java.util.Date;

public class EmailCampaign {
  
  private String id;
  
  private long number;
  
  private Date date;
  
  private long customerDocumentId;
  
  private Customer customer;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public long getNumber() {
    return number;
  }

  public void setNumber(long number) {
    this.number = number;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public long getCustomerDocumentId() {
    return customerDocumentId;
  }

  public void setCustomerDocumentId(long customerDocumentId) {
    this.customerDocumentId = customerDocumentId;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }
  
}
