package concurrent;

public class Individual {
    double x;
    double y;
    double fitness;

    Individual(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void print() {
        System.out.println(x);
        System.out.println(y);
    }
}
