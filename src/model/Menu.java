package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Menu {
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleDoubleProperty price;
    private SimpleIntegerProperty timesOrdered;
    private SimpleStringProperty availableString;
    private SimpleIntegerProperty availableNum;

    public Menu(Integer id, String name, Double price) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.timesOrdered = new SimpleIntegerProperty(0);
    }

    public Menu(Integer id, String name, int available) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.availableString = (available == 1) ? new SimpleStringProperty("Yes") : new SimpleStringProperty("No");
        this.availableNum = new SimpleIntegerProperty(available);
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

    public String getAvailableString() {
        return availableString.get();
    }

    public int getAvailableNum() {
        return availableNum.get();
    }

    public void setAvailableNum(int availableNum) {
        this.availableNum.set(availableNum);
    }
}
