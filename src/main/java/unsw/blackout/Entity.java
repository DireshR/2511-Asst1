package unsw.blackout;

import unsw.utils.Angle;

public abstract class Entity {
    private String entityId;
    private String type;
    private double range;

    public Entity(String entityId, String type) {
        this.entityId = entityId;
        this.type = type;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDevice() {
        return (this instanceof Device);
    }

    public boolean isSatellite() {
        return (this instanceof Satellite);
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public abstract double getHeight();

    public abstract Angle getPosition();
}
