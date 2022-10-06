package unsw.blackout;

import java.util.ArrayList;

// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.Map;

// import unsw.response.models.FileInfoResponse;
// import java.util.ArrayList;
import unsw.utils.Angle;

public class Device extends Entity {
    private static final double RADIUS_OF_JUPITER = 69911;
    private String deviceId;
    private String type;
    private Angle position;
    // private ArrayList<File> files = new ArrayList<File>();

    public Device(String deviceId, String type, Angle position) {
        super(deviceId, type);
        this.deviceId = deviceId;
        this.type = type;
        this.position = position;
        this.setAvailableReceiveBandwidth(Integer.MAX_VALUE);
        this.setAvailableSendBandwidth(Integer.MAX_VALUE);
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

    @Override
    public double getHeight() {
        return RADIUS_OF_JUPITER;
    }

    @Override
    public void transferFile(String filename, String content, String origin) throws FileTransferException {
        super.addFile(filename, content, content.length(), origin);
    }

    @Override
    public void updateFileTransfer(int bandwidth, String origin) {
        ArrayList<File> files = super.getFiles();
        ArrayList<File> transferringFiles = new ArrayList<File>();
        for (File file : files) {
            if (!(file.getTransferringContent().equals(""))) {
                transferringFiles.add(file);
            }
        }
        for (File file : transferringFiles) {
            if (!(origin.equals(file.getOrigin()))) {
                break;
            }
            int nextByte = file.getContent().length();
            String newContent = file.getContent();
            String nextChar = "" + file.getTransferringContent().charAt(nextByte);
            if (bandwidth == Integer.MAX_VALUE && nextChar == "t") {
                nextChar = "";
            }
            newContent = newContent + nextChar;
            file.setContent(newContent);
            if (nextByte == (file.getSize() - 1)) {
                file.setTransferringContent("");
                break;
            }
            break;
        }
    }

    @Override
    public void leftRange(String senderId) {
        ArrayList<File> files = super.getFiles();
        File fileToDelete = null;
        for (File file : files) {
            if (file.getOrigin().equals(senderId) && !(file.getTransferringContent().equals(""))) {
                fileToDelete = file;
                break;
            }
        }
        if (fileToDelete != null) {
            files.remove(fileToDelete);
        }
    }

}
