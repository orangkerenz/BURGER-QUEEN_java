package model;

public class Table {
    private int tables_num;
    private int tables_capacity;
    private int avaliable;

    public Table(int tables_num, int tables_capacity, int avaliable) {

        this.tables_num = tables_num;
        this.tables_capacity = tables_capacity;
        this.avaliable = avaliable;
    }

    public int getTablesNum() {
        return tables_num;
    }

    public void getTablesNum(int tables_num) {
        this.tables_num = tables_num;
    }

    public int getTableCapacity() {
        return tables_capacity;
    }

    public void setTableCapacity(int tables_capacity) {
        this.tables_capacity = tables_capacity;
    }

    public int getAvaliable() {
        return avaliable;
    }

    public void setAvaliable(int avaliable) {
        this.avaliable = avaliable;
    }

}
