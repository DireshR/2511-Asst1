package unsw.blackout;

import unsw.utils.Angle;

public abstract class Satellite extends Entity {
    private double height;
    private Angle position;
    private int linearVelocity;

    public Satellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type);
        this.height = height;
        this.position = position;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setPosition(Angle position) {
        this.position = position;
    }

    public int getLinearVelocity() {
        return linearVelocity;
    }

    public void setLinearVelocity(int linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public Angle getPosition() {
        return position;
    }

    public abstract void updateLinearVelocity();

    public abstract void moveSatellite();

}
