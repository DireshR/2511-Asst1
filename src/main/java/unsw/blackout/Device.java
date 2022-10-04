package unsw.blackout;

//import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

public class Device {
    private String deviceId;
    private String type;
    private Angle position;
    private double range;
    private Map<String, FileInfoResponse> files = new HashMap<String, FileInfoResponse>();

    public Device(String deviceId, String type, Angle position) {
        this.deviceId = deviceId;
        this.type = type;
        this.position = position;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Angle getPosition() {
        return position;
    }

    public void setPosition(Angle position) {
        this.position = position;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public void addFileToDevice(String filename, String content) {
        FileInfoResponse file = new FileInfoResponse(filename, content, content.length(), true);
        files.put(filename, file);

    }

    public Map<String, FileInfoResponse> getFiles() {
        return files;
    }

    public void setFiles(Map<String, FileInfoResponse> files) {
        this.files = files;
    }

}
