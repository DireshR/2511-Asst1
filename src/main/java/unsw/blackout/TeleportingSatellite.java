package unsw.blackout;

import unsw.utils.Angle;

public class TeleportingSatellite extends Satellite {
    private int linearVelocity = 1000;
    private static final int MAX_RANGE = 200000;
    private static final int RECEIVE_BANDWIDTH = 15;
    private static final int SEND_BANDWIDTH = 10;

    public TeleportingSatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position);
    }

    public int getLinearVelocity() {
        return linearVelocity;
    }

    public int getMaxRange() {
        return MAX_RANGE;
    }

    public static int getReceiveBandwidth() {
        return RECEIVE_BANDWIDTH;
    }

    public static int getSendBandwidtth() {
        return SEND_BANDWIDTH;
    }

    public void setLinearVelocity() {
        if (this.getPosition().toDegrees() == 180) {
            this.linearVelocity = this.linearVelocity * -1;
        }
    }

    @Override
    public void updateLinearVelocity() {
        setLinearVelocity();
        super.setLinearVelocity(linearVelocity);
    }

    // @Override
    // public void setPosition(Angle position) {
    // if (this.getPosition().toDegrees() >= 180) {
    // this.setPosition(new Angle());
    // } else {

    // }
    // }

    @Override
    public void moveSatellite() {
        Angle currAngle = this.getPosition();
        double currPos = currAngle.toRadians();
        int linVel = getLinearVelocity();
        double radius = this.getHeight();
        double angVel = linVel / radius;
        double newPos = currPos + angVel;
        Angle newAngle = Angle.fromRadians(newPos);
        if (currAngle.toDegrees() < 180 && newAngle.toDegrees() >= 180
                || currAngle.toDegrees() > 180 && newAngle.toDegrees() <= 180) {
            this.setPosition(new Angle());
        } else {
            this.setPosition(newAngle);
        }
    }

}
