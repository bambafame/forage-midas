package com.jpmc.midascore.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class TransactionRecord {
  @Id
  @GeneratedValue()
  private long id;
  @Column(nullable = false)
  private float amount;

  @ManyToOne(cascade = CascadeType.ALL)
  private UserRecord sender;
  @ManyToOne(cascade = CascadeType.ALL)
  private UserRecord recipient;

  public TransactionRecord() {
  }

  public TransactionRecord(float amount, UserRecord sender, UserRecord recipient) {
    this.amount = amount;
    this.sender = sender;
    this.recipient = recipient;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public float getAmount() {
    return amount;
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }

  public UserRecord getSender() {
    return sender;
  }

  public void setSender(UserRecord sender) {
    this.sender = sender;
  }

  public UserRecord getRecipient() {
    return recipient;
  }

  public void setRecipient(UserRecord recipient) {
    this.recipient = recipient;
  }
}
