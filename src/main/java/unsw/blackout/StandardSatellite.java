package unsw.blackout;

import unsw.utils.Angle;

public class StandardSatellite extends Satellite {
    private static final int LINEAR_VELOCITY = -2500;
    private static final double MAX_RANGE = 150000;
    private static final int RECEIVE_BANDWIDTH = 1;
    private static final int SEND_BANDWIDTH = 1;

    public StandardSatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position);
        super.setRange(MAX_RANGE);
    }

    public int getLinearVelocity() {
        return LINEAR_VELOCITY;
    }

    public int getReceiveBandwidth() {
        return RECEIVE_BANDWIDTH;
    }

    public int getSendBandwidtth() {
        return SEND_BANDWIDTH;
    }

    @Override
    public void updateLinearVelocity() {
        super.setLinearVelocity(LINEAR_VELOCITY);
    }

    @Override
    public void moveSatellite() {
        Angle currAngle = this.getPosition();
        double currPos = currAngle.toRadians();
        int linVel = getLinearVelocity();
        double radius = this.getHeight();
        double angVel = linVel / radius;
        double newPos = currPos + angVel;
        Angle newAngle = Angle.fromRadians(newPos);
        this.setPosition(newAngle);
    }

}
