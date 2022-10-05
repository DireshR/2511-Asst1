package unsw.blackout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class BlackoutController {

    private static final double RADIUS_OF_JUPITER = 69911;
    private ArrayList<Entity> entities = new ArrayList<Entity>();

    public void createDevice(String deviceId, String type, Angle position) {
        // TODO: Task 1a)
        switch (type) {
            case "HandheldDevice":
                HandheldDevice hndDev = new HandheldDevice(deviceId, type, position);
                entities.add(hndDev);
                break;
            case "LaptopDevice":
                LaptopDevice lapDev = new LaptopDevice(deviceId, type, position);
                entities.add(lapDev);
                break;
            case "DesktopDevice":
                DesktopDevice dskDev = new DesktopDevice(deviceId, type, position);
                entities.add(dskDev);
                break;
            default:
                break;
        }

    }

    public void removeDevice(String deviceId) {
        // TODO: Task 1b)
        Iterator<Entity> ent = entities.iterator();
        while (ent.hasNext()) {
            Entity curr = ent.next();
            if (curr.getEntityId().equals(deviceId)) {
                ent.remove();
            }
        }
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        // TODO: Task 1c)
        switch (type) {
            case "StandardSatellite":
                StandardSatellite stdSat = new StandardSatellite(satelliteId, type, height, position);
                entities.add(stdSat);
                break;
            case "TeleportingSatellite":
                TeleportingSatellite tlpSat = new TeleportingSatellite(satelliteId, type, height, position);
                entities.add(tlpSat);
                break;
            case "RelaySatellite":
                RelaySatellite relSat = new RelaySatellite(satelliteId, type, height, position);
                entities.add(relSat);
                break;
            default:
                break;
        }
    }

    public void removeSatellite(String satelliteId) {
        // TODO: Task 1d)
        Iterator<Entity> ent = entities.iterator();
        while (ent.hasNext()) {
            Entity curr = ent.next();
            if (curr.getEntityId().equals(satelliteId)) {
                ent.remove();
            }
        }
    }

    public List<String> listDeviceIds() {
        // TODO: Task 1e)
        ArrayList<String> devIds = new ArrayList<String>();
        for (Entity ent : entities) {
            if (ent instanceof Device) {
                devIds.add(ent.getEntityId());
            }
        }
        return devIds;
    }

    public List<String> listSatelliteIds() {
        // TODO: Task 1f)
        ArrayList<String> satIds = new ArrayList<String>();
        for (Entity ent : entities) {
            if (ent instanceof Satellite) {
                satIds.add(ent.getEntityId());
            }
        }
        return satIds;
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        // TODO: Task 1g)
        for (Entity ent : entities) {
            if (ent.getEntityId().equals(deviceId)) {
                Device curr = (Device) ent;
                curr.addFileToDevice(filename, content);
            }
        }
    }

    public EntityInfoResponse getInfo(String id) {
        // TODO: Task 1h)
        Angle position;
        double height;
        String type;
        for (Entity ent : entities) {
            if (ent.getEntityId().equals(id) && ent instanceof Satellite) {
                Satellite sat = (Satellite) ent;
                position = sat.getPosition();
                height = sat.getHeight();
                type = sat.getType();
                return new EntityInfoResponse(id, position, height, type);
            }

            if (ent.getEntityId().equals(id) && ent instanceof Device) {
                Device dev = (Device) ent;
                position = dev.getPosition();
                height = RADIUS_OF_JUPITER;
                type = dev.getType();
                Map<String, FileInfoResponse> fileInfo = dev.getFilesInfo();
                return new EntityInfoResponse(id, position, height, type, fileInfo);
            }
        }

        return null;
    }

    public void simulate() {
        // TODO: Task 2a)
        for (Entity ent : entities) {
            if (ent instanceof Satellite) {
                Satellite sat = (Satellite) ent;
                sat.updateLinearVelocity();
                sat.moveSatellite();
            }
        }
    }

    /**
     * Simulate for the specified number of minutes.
     * You shouldn't need to modify this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    public Optional<Entity> findEntity(String id) {
        return entities.stream().filter(entity -> id.equals(entity.getEntityId())).findFirst();
    }

    public boolean isCompatible(String senderId, String targetId) {
        Entity sender = findEntity(senderId).get();
        Entity target = findEntity(targetId).get();
        if ((sender instanceof StandardSatellite && target instanceof DesktopDevice)
                || (sender instanceof DesktopDevice && target instanceof StandardSatellite)) {
            return false;
        } else if (sender instanceof Device && target instanceof Device) {
            return false;
        }
        return true;
    }

    public boolean inRange(String senderId, String targetId) {
        Entity sender = findEntity(senderId).get();
        Entity target = findEntity(targetId).get();
        double range = sender.getRange();
        double distance = MathsHelper.getDistance(sender.getHeight(), sender.getPosition(), target.getHeight(),
                target.getPosition());
        return (distance <= range);
    }

    public List<String> communicableEntitiesInRange(String id) {
        // TODO: Task 2 b)
        ArrayList<String> commList = new ArrayList<String>();
        doCommunicableEntitiesInRange(id, commList);
        for (String newId : commList) {
            if (findEntity(newId).get() instanceof RelaySatellite) {
                commList.addAll(communicableEntitiesInRange(newId));
            }
        }
        return commList;
    }

    public void doCommunicableEntitiesInRange(String id, ArrayList<String> commList) {
        for (Entity ent : entities) {
            String targetId = ent.getEntityId();
            if (!(targetId.equals(id)) && isCompatible(id, targetId) && inRange(id, targetId)
                    && !(commList.contains(targetId))) {
                commList.add(targetId);
            }
        }
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        // TODO: Task 2 c)
    }

    public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        createDevice(deviceId, type, position);
        // TODO: Task 3
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        // TODO: Task 3
        // If you are not completing Task 3 you can leave this method blank :)
    }

}
