package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Menu {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleDoubleProperty price;
    private int timesOrdered = 1;

    public Menu(Integer id, String name, Double price) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public int getTimesOrdered() {
        return timesOrdered;
    }

    public void setTimesOrdered(int timesOrdered) {
        this.timesOrdered = timesOrdered;
    }

}
