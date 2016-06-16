package model;

/**
 * Created by Sheremet on 15.06.2016.
 */
public class AnalogTrigger extends AbstractTrigger{

    private double state;
    public AnalogTrigger (String name) {
        super(name);
    }
    public double getState() {
        return state;
    }

    public void setState(double state) {
        this.state = state;
    }
    @Override
    public String toString() {
        return "AnalogTrigger{" +
                "id=" + super.getId() +
                ", name=" + super.getName() +
                ", state=" + state +
                '}';
    }
}
