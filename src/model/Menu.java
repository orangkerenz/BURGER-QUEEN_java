package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Menu {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleDoubleProperty price;
    private SimpleIntegerProperty timesOrdered;

    public Menu(Integer id, String name, Double price) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.timesOrdered = new SimpleIntegerProperty(0);
    }

    public Menu(String name, Double price, Integer timesOrdered) {
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.timesOrdered = new SimpleIntegerProperty(timesOrdered);
    }

    public Menu(Integer id, String name, Integer timesOrdered) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.timesOrdered = new SimpleIntegerProperty(timesOrdered);
    }

    public Menu(String name, Integer timesOrdered) {
        this.name = new SimpleStringProperty(name);
        this.timesOrdered = new SimpleIntegerProperty(timesOrdered);
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

    public int getTimesOrdered() {
        return timesOrdered.get();
    }

    public void setTimesOrdered(int timesOrdered) {
        this.timesOrdered.set(timesOrdered);
    }
}
