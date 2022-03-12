package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Order {
    private SimpleIntegerProperty id;
    private SimpleStringProperty orderDate;
    private SimpleIntegerProperty paid;
    private SimpleIntegerProperty userId;
    private SimpleIntegerProperty tableNum;
    private SimpleIntegerProperty cancelled;
    private SimpleDoubleProperty totalPrice;
    private SimpleIntegerProperty served;

    public Order(Integer id, String orderDate, Integer paid, Integer userId, Integer tableNum, Integer cancelled,
            Double totalPrice, Integer served) {
        this.id = new SimpleIntegerProperty(id);
        this.orderDate = new SimpleStringProperty(orderDate);
        this.paid = new SimpleIntegerProperty(paid);
        this.userId = new SimpleIntegerProperty(userId);
        this.tableNum = new SimpleIntegerProperty(tableNum);
        this.cancelled = new SimpleIntegerProperty(cancelled);
        this.totalPrice = new SimpleDoubleProperty(totalPrice);
        this.served = new SimpleIntegerProperty(served);
    }

    public Integer getId() {
        return id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public String getOrderDate() {
        return orderDate.get();
    }

    public void setOrderDate(String orderDate) {
        this.orderDate.set(orderDate);
    }

    public Integer getPaid() {
        return paid.get();
    }

    public void setPaid(Integer paid) {
        this.paid.set(paid);
    }

    public Integer getUserId() {
        return userId.get();
    }

    public void setUserId(Integer userId) {
        this.userId.set(userId);
    }

    public Integer getTableNum() {
        return tableNum.get();
    }

    public void setTableNum(Integer tableNum) {
        this.tableNum.set(tableNum);
    }

    public Integer getCancelled() {
        return cancelled.get();
    }

    public void setCancelled(Integer cancelled) {
        this.cancelled.set(cancelled);
    }

    public Double getTotalPrice() {
        return totalPrice.get();
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    public Integer getServed() {
        return served.get();
    }

    public void setServed(Integer served) {
        this.served.set(served);
    }
}
