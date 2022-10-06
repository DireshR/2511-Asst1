package unsw.blackout;

import java.util.ArrayList;

import unsw.utils.Angle;
import java.util.HashMap;
import java.util.Map;

import unsw.response.models.FileInfoResponse;

public abstract class Entity {
    private String entityId;
    private String type;
    private double range;
    private int availableSendBandwidth;
    private int availableReceiveBandwidth;

    private ArrayList<File> files = new ArrayList<File>();
    private ArrayList<File> filesSending = new ArrayList<File>();

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

    public int getAvailableSendBandwidth() {
        return availableSendBandwidth;
    }

    public void setAvailableSendBandwidth(int availableSendBandwidth) {
        this.availableSendBandwidth = availableSendBandwidth;
    }

    public int getAvailableReceiveBandwidth() {
        return availableReceiveBandwidth;
    }

    public void setAvailableReceiveBandwidth(int availableReceiveBandwidth) {
        this.availableReceiveBandwidth = availableReceiveBandwidth;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public ArrayList<String> getFilenames() {
        ArrayList<String> filenames = new ArrayList<String>();
        for (File file : files) {
            filenames.add(file.getFilename());
        }
        return filenames;
    }

    public File findFile(String filename) {
        for (File file : files) {
            if (file.getFilename().equals(filename)) {
                return file;
            }
        }
        return null;
    }

    public void addFile(String filename, String content) {
        File file = new File(filename, content);
        files.add(file);
    }

    public void addFile(String filename, String content, int size, String origin) {
        File file = new File(filename, content, size, origin);
        files.add(file);
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }

    public int numOfFiles() {
        return files.size();
    }

    public int numFilesSending() {
        return filesSending.size();
    }

    public int numFilesReceiving() {
        int filesReceiving = 0;
        for (File file : files) {
            if (!(file.getTransferringContent().equals(""))) {
                filesReceiving++;
            }
        }
        return filesReceiving;
    }

    public int totalBytesStored() {
        int total = 0;
        for (File file : files) {
            total += file.getSize();
        }
        return total;
    }

    public Map<String, FileInfoResponse> getFilesInfo() {
        Map<String, FileInfoResponse> fileInfo = new HashMap<>();
        for (File file : files) {
            fileInfo.put(file.getFilename(),
                    new FileInfoResponse(file.getFilename(), file.getContent(), file.getSize(), true));
        }
        return fileInfo;
    }

    public void sendFile(String filename) throws FileTransferException {
        File file = findFile(filename);
        if (!(file.getTransferringContent().equals(""))) {
            throw new FileTransferException.VirtualFileNotFoundException(filename);
        }
        File sendFile = new File(filename, file.getContent());
        filesSending.add(sendFile);
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

    public abstract void transferFile(String filename, String content, String origin) throws FileTransferException;

    public abstract double getHeight();

    public abstract Angle getPosition();

    public abstract void updateFileTransfer(int bandwidth);

    public abstract void leftRange(String senderId);
}
