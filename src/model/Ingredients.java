package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Ingredients {
    private SimpleIntegerProperty menuid;
    private SimpleStringProperty ingredientsName;
    private SimpleIntegerProperty ingredientsId;
    private SimpleDoubleProperty grams;

    public Ingredients(int menuid, String ingredientsName, Double grams) {
        this.menuid = new SimpleIntegerProperty(menuid);
        this.ingredientsName = new SimpleStringProperty(ingredientsName);
        this.grams = new SimpleDoubleProperty(grams);
    }

    public Ingredients(String ingredientsName, int ingredientsId, Double grams) {
        this.ingredientsName = new SimpleStringProperty(ingredientsName);
        this.grams = new SimpleDoubleProperty(grams);
        this.ingredientsId = new SimpleIntegerProperty(ingredientsId);
    }

    public int getMenuid() {
        return menuid.get();
    }

    public void setMenuid(int menuid) {
        this.menuid.set(menuid);
    }

    public String getIngredientsName() {
        return ingredientsName.get();
    }

    public void setIngredientsName(String ingredientsName) {
        this.ingredientsName.set(ingredientsName);
    }

    public double getGrams() {
        return grams.get();
    }

    public void setGrams(double grams) {
        this.grams.set(grams);
    }

    public int getIngredientsId() {
        return ingredientsId.get();
    }

    public void setIngredientsId(int ingredientsId) {
        this.ingredientsId.set(ingredientsId);
    }
}
