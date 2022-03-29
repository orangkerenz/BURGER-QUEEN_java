package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import tools.DatabaseTools;

public class Order {
    private SimpleIntegerProperty id;
    private SimpleStringProperty orderDate;
    private SimpleIntegerProperty paid;
    private SimpleIntegerProperty userId;
    private SimpleIntegerProperty tableNum;
    private SimpleIntegerProperty cancelled;
    private SimpleDoubleProperty totalPrice;
    private SimpleIntegerProperty served;
    private SimpleStringProperty name;

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

    public Order(Integer id, String orderDate, Integer tableNum, String name) {
        this.id = new SimpleIntegerProperty(id);
        this.orderDate = new SimpleStringProperty(orderDate);
        this.tableNum = new SimpleIntegerProperty(tableNum);
        this.name = new SimpleStringProperty(name);
    }

    public Order(Integer id, String orderDate, Integer tableNum, Double price) {
        this.id = new SimpleIntegerProperty(id);
        this.orderDate = new SimpleStringProperty(orderDate);
        this.tableNum = new SimpleIntegerProperty(tableNum);
        this.totalPrice = new SimpleDoubleProperty(price);
    }

    public Order(Integer id, Integer tableNum) {
        this.id = new SimpleIntegerProperty(id);
        this.tableNum = new SimpleIntegerProperty(tableNum);
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

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public static void cancelOrderBiggerThanOneDay() {
        // TODO Auto-generated method stub

        Connection conn = DatabaseTools.getConnection();
        try {
            Statement queryOrders = conn.createStatement();
            ResultSet rsOrders = queryOrders
                    .executeQuery(
                            "SELECT * FROM orders WHERE DATE(order_date) != CURRENT_DATE AND status != 'canceled'");
            while (rsOrders.next()) {
                Statement queryMenu = conn.createStatement();
                ResultSet rsMenu = queryMenu
                        .executeQuery(
                                "SELECT * FROM menus_has_orders WHERE order_id = '" + rsOrders.getInt("id") + "'");

                while (rsMenu.next()) {
                    Statement queryRecipeMenu = conn.createStatement();
                    ResultSet rsRecipeMenu = queryRecipeMenu
                            .executeQuery("SELECT * FROM recipes WHERE menu_id = '" + rsMenu.getInt("menu_id") + "'");
                    while (rsRecipeMenu.next()) {
                        double quantityWantToReturn = rsRecipeMenu.getDouble("quantity_in_grams")
                                * rsMenu.getInt("quantity");

                        Statement updateRecipe = conn.createStatement();
                        updateRecipe.executeUpdate(
                                "UPDATE ingredients SET quantity_in_grams = quantity_in_grams + " + quantityWantToReturn
                                        + " WHERE id = '" + rsRecipeMenu.getInt("ingredient_id") + "'");
                    }
                }

                Statement deleteMenusHasOrder = conn.createStatement();
                deleteMenusHasOrder.executeUpdate(
                        "DELETE FROM menus_has_orders WHERE order_id = '" + rsOrders.getInt("id") + "'");

                Statement cancelOrder = conn.createStatement();
                cancelOrder
                        .executeUpdate(
                                "UPDATE orders SET status = 'canceled' WHERE id = '" + rsOrders.getInt("id") + "'");

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
