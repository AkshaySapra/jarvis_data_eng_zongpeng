package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.DataTransferObject;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Order implements DataTransferObject {

  private long id;
  private String customerFirstName;
  private String customerLastName;
  private String customerEmail;
  private Date creationDate;
  private BigDecimal totalDue;
  private String status;
  private String salespersonFirstName;
  private String salespersonLastName;
  private String salespersonEmail;
  private List<OrderItem> orderItems;


  @Override
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getCustomerFirstName() {
    return customerFirstName;
  }

  public void setCustomerFirstName(String customerFirstName) {
    this.customerFirstName = customerFirstName;
  }

  public String getCustomerLastName() {
    return customerLastName;
  }

  public void setCustomerLastName(String customerLastName) {
    this.customerLastName = customerLastName;
  }

  public String getCustomerEmail() {
    return customerEmail;
  }

  public void setCustomerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date createionDate) {
    this.creationDate = createionDate;
  }

  public BigDecimal getTotalDue() {
    return totalDue;
  }

  public void setTotalDue(BigDecimal totalDue) {
    this.totalDue = totalDue;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getSalespersonFirstName() {
    return salespersonFirstName;
  }

  public void setSalespersonFirstName(String salespersonFirstName) {
    this.salespersonFirstName = salespersonFirstName;
  }

  public String getSalespersonLastName() {
    return salespersonLastName;
  }

  public void setSalespersonLastName(String salespersonLastName) {
    this.salespersonLastName = salespersonLastName;
  }

  public String getSalespersonEmail() {
    return salespersonEmail;
  }

  public void setSalespersonEmail(String salespersonEmail) {
    this.salespersonEmail = salespersonEmail;
  }

  public List<OrderItem> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(List<OrderItem> orderItems) {
    this.orderItems = orderItems;
  }

  @Override
  public String toString() {
    return "Order{" +
        "id=" + id +
        ", customerFirstName='" + customerFirstName + '\'' +
        ", \ncustomerLastLane='" + customerLastName + '\'' +
        ", \ncustomerEmail='" + customerEmail + '\'' +
        ", \ncreationDate=" + creationDate +
        ", \ntotalDue=" + totalDue +
        ", \nstatus='" + status + '\'' +
        ", \nsalespersonFirstName='" + salespersonFirstName + '\'' +
        ", \nsalespersonLastName='" + salespersonLastName + '\'' +
        ", \nsalespersonEmail='" + salespersonEmail + '\'' +
        ", \norderItemss=" + orderItems +
        '}';
  }


}