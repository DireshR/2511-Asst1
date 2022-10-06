package unsw.blackout;

import unsw.utils.Angle;

public class RelaySatellite extends Satellite {
    private int linearVelocity = -1500;
    private static final double MAX_RANGE = 300000;

    public RelaySatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position);
        super.setRange(MAX_RANGE);
        super.setAvailableReceiveBandwidth(0);
        super.setAvailableSendBandwidth(0);
    }

    public int getLinearVelocity() {
        return linearVelocity;
    }

    public void setLinearVelocity() {
        if (this.getPosition().toDegrees() <= 345 && this.getPosition().toDegrees() > 190) {
            this.linearVelocity = -1500;
        } else if (this.getPosition().toDegrees() > 345 && this.getPosition().toDegrees() < 140) {
            this.linearVelocity = 1500;
        }

        if (this.getPosition().toDegrees() <= 140) {
            this.linearVelocity = 1500;
        } else if (this.getPosition().toDegrees() >= 190) {
            this.linearVelocity = -1500;
        }
    }

    @Override
    public void updateLinearVelocity() {
        setLinearVelocity();
        super.setLinearVelocity(linearVelocity);
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

    @Override
    public void transferFile(String filename, String content, String origin) throws FileTransferException {
        throw new FileTransferException.VirtualFileNoBandwidthException(this.getEntityId());
    }

    @Override
    public void updateFileTransfer(int bandwidth) {
        return;
    }

    @Override
    public void leftRange(String senderId) {
        return;
    }
}
