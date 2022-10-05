package unsw.blackout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import unsw.response.models.FileInfoResponse;
// import java.util.ArrayList;
import unsw.utils.Angle;

public class Device extends Entity {
    private static final double RADIUS_OF_JUPITER = 69911;
    private String deviceId;
    private String type;
    private Angle position;
    private ArrayList<File> files = new ArrayList<File>();

    public Device(String deviceId, String type, Angle position) {
        super(deviceId, type);
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

    public void addFileToDevice(String filename, String content) {
        File file = new File(filename, content);
        files.add(file);
    }

    public Map<String, FileInfoResponse> getFilesInfo() {
        Map<String, FileInfoResponse> fileInfo = new HashMap<>();
        for (File file : files) {
            fileInfo.put(file.getFilename(),
                    new FileInfoResponse(file.getFilename(), file.getContent(), file.getSize(), true));
        }
        return fileInfo;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }

    @Override
    public double getHeight() {
        return RADIUS_OF_JUPITER;
    }

}
