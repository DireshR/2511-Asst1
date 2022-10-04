package unsw.blackout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

public class BlackoutController {

    private static final double RADIUS_OF_JUPITER = 69911;
    private ArrayList<Satellite> satellites = new ArrayList<Satellite>();
    private ArrayList<Device> devices = new ArrayList<Device>();

    public void createDevice(String deviceId, String type, Angle position) {
        // TODO: Task 1a)
        switch (type) {
            case "HandheldDevice":
                HandheldDevice hndDev = new HandheldDevice(deviceId, type, position);
                devices.add(hndDev);
                break;
            case "LaptopDevice":
                LaptopDevice lapDev = new LaptopDevice(deviceId, type, position);
                devices.add(lapDev);
                break;
            case "DesktopDevice":
                DesktopDevice dskDev = new DesktopDevice(deviceId, type, position);
                devices.add(dskDev);
                break;
            default:
                break;
        }

    }

    public void removeDevice(String deviceId) {
        // TODO: Task 1b)
        Iterator<Device> dev = devices.iterator();
        while (dev.hasNext()) {
            Device curr = dev.next();
            if (curr.getDeviceId().equals(deviceId)) {
                dev.remove();
            }
        }
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        // TODO: Task 1c)

        // if (!(satelliteId instanceof String && type instanceof String && height
        // instanceof double
        // && position instanceof Angle)) {
        // throw new ExceptionInInitializerError("Parameter Types Incorrect");
        // }

        switch (type) {
            case "StandardSatellite":
                StandardSatellite stdSat = new StandardSatellite(satelliteId, type, height, position);
                satellites.add(stdSat);
                break;
            case "TeleportingSatellite":
                TeleportingSatellite tlpSat = new TeleportingSatellite(satelliteId, type, height, position);
                satellites.add(tlpSat);
                break;
            case "RelaySatellite":
                RelaySatellite relSat = new RelaySatellite(satelliteId, type, height, position);
                satellites.add(relSat);
                break;
            default:
                break;
        }
    }

    public void removeSatellite(String satelliteId) {
        // TODO: Task 1d)
        Iterator<Satellite> sat = satellites.iterator();
        while (sat.hasNext()) {
            Satellite curr = sat.next();
            if (curr.getSatelliteId().equals(satelliteId)) {
                sat.remove();
            }
        }
    }

    public List<String> listDeviceIds() {
        // TODO: Task 1e)
        ArrayList<String> devIds = new ArrayList<String>();
        for (Device dev : devices) {
            devIds.add(dev.getDeviceId());
        }
        return devIds;
    }

    public List<String> listSatelliteIds() {
        // TODO: Task 1f)
        ArrayList<String> satIds = new ArrayList<String>();
        for (Satellite sat : satellites) {
            satIds.add(sat.getSatelliteId());
        }
        return satIds;
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        // TODO: Task 1g)
        for (Device dev : devices) {
            if (dev.getDeviceId().equals(deviceId)) {
                dev.addFileToDevice(filename, content);
            }
        }
    }

    public EntityInfoResponse getInfo(String id) {
        // TODO: Task 1h)
        Angle position;
        double height;
        String type;
        Map<String, FileInfoResponse> files;
        for (Satellite sat : satellites) {
            if (sat.getSatelliteId().equals(id)) {
                position = sat.getPosition();
                height = sat.getHeight();
                type = sat.getType();
                return new EntityInfoResponse(id, position, height, type);
            }
        }

        for (Device dev : devices) {
            if (dev.getDeviceId().equals(id)) {
                position = dev.getPosition();
                height = RADIUS_OF_JUPITER;
                type = dev.getType();
                files = dev.getFiles();
                return new EntityInfoResponse(id, position, height, type, files);
            }
        }

        return null;
    }

    public void simulate() {
        // TODO: Task 2a)
        for (Satellite sat : satellites) {
            sat.updateLinearVelocity();
            sat.moveSatellite();
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

    public boolean isDevice(String id) {
        for (Device dev : devices) {
            if (dev.getDeviceId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSatellite(String id) {
        for (Satellite sat : satellites) {
            if (sat.getSatelliteId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public String getType(String id) throws Exception {
        for (Satellite sat : satellites) {
            if (sat.getSatelliteId().equals(id)) {
                return sat.getType();
            }
        }
        for (Device dev : devices) {
            if (dev.getDeviceId().equals(id)) {
                return dev.getType();
            }
        }
        throw new Exception("Incorrect Entity ID");
    }

    public Satellite getSatellite(String id) throws Exception {
        for (Satellite sat : satellites) {
            if (sat.getSatelliteId().equals(id)) {
                return sat;
            }
        }
        throw new Exception("Incorrect Entity ID");
    }

    public boolean isCompatible(String senderId, String targetId) throws Exception {
        if ((getType(senderId) == "DesktopDevice" && getType(targetId) == "StandardSatellite")
                || (getType(targetId) == "DesktopDevice" && getType(senderId) == "StandardSatellite")) {
            return false;
        }
        return true;
    }

    public boolean isCommunicable() {
        return false;
    }

    public List<String> communicableEntitiesInRange(String id) {
        // TODO: Task 2 b)
        if (isDevice(id)) {
            return new ArrayList<>();
        }
        return new ArrayList<>();
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
