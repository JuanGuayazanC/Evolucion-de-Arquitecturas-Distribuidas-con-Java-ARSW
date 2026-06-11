package classroom;

public class classroom {
    private int id;

    public classroom(int id, String title, String director, int year) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public String toText() {
        return id + "," ;
    }
}
