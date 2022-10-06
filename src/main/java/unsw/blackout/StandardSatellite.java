package unsw.blackout;

import java.util.ArrayList;

import unsw.utils.Angle;

public class StandardSatellite extends Satellite {
    private static final int LINEAR_VELOCITY = -2500;
    private static final double MAX_RANGE = 150000;
    private static final int RECEIVE_BANDWIDTH = 1;
    private static final int SEND_BANDWIDTH = 1;

    public StandardSatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position);
        super.setRange(MAX_RANGE);
        super.setAvailableReceiveBandwidth(RECEIVE_BANDWIDTH);
        super.setAvailableSendBandwidth(SEND_BANDWIDTH);
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

    @Override
    public void transferFile(String filename, String content, String origin) throws FileTransferException {
        int numFiles = super.numOfFiles();
        int totalBytes = super.totalBytesStored();
        int size = content.length();
        ArrayList<String> filenames = super.getFilenames();
        for (String name : filenames) {
            if (name.equals(filename)) {
                throw new FileTransferException.VirtualFileAlreadyExistsException(filename);
            }
        }
        if (numFiles < 3 && (totalBytes + content.length()) <= 80) {
            super.addFile(filename, content, size, origin);
        } else if (numFiles >= 3 && (totalBytes + content.length()) <= 80) {
            throw new FileTransferException.VirtualFileNoStorageSpaceException("Max Files Reached");
        } else if (numFiles < 3 && (totalBytes + content.length()) > 80) {
            throw new FileTransferException.VirtualFileNoStorageSpaceException("Max Storage Reached");
        }
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
            for (int i = 0; i < (bandwidth / transferringFiles.size()); i++) {
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
            }
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
