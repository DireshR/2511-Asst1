package unsw.blackout;

import java.util.ArrayList;

import unsw.utils.Angle;

public class TeleportingSatellite extends Satellite {
    private int linearVelocity = 1000;
    private static final double MAX_RANGE = 200000;
    private static final int RECEIVE_BANDWIDTH = 15;
    private static final int SEND_BANDWIDTH = 10;

    public TeleportingSatellite(String satelliteId, String type, double height, Angle position) {
        super(satelliteId, type, height, position);
        super.setRange(MAX_RANGE);
        super.setAvailableReceiveBandwidth(RECEIVE_BANDWIDTH);
        super.setAvailableSendBandwidth(SEND_BANDWIDTH);
    }

    public int getLinearVelocity() {
        return linearVelocity;
    }

    public static int getReceiveBandwidth() {
        return RECEIVE_BANDWIDTH;
    }

    public static int getSendBandwidtth() {
        return SEND_BANDWIDTH;
    }

    public void setLinearVelocity() {
        if (this.getPosition().toDegrees() == 180) {
            this.linearVelocity = this.linearVelocity * -1;
        }
    }

    public boolean isTeleporting() {
        Angle currAngle = this.getPosition();
        double currPos = currAngle.toRadians();
        int linVel = getLinearVelocity();
        double radius = this.getHeight();
        double angVel = linVel / radius;
        double newPos = currPos + angVel;
        Angle newAngle = Angle.fromRadians(newPos);
        return ((currAngle.toDegrees() < 180 && newAngle.toDegrees() >= 180)
                || (currAngle.toDegrees() > 180 && newAngle.toDegrees() <= 180));
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
        if (currAngle.toDegrees() < 180 && newAngle.toDegrees() >= 180
                || currAngle.toDegrees() > 180 && newAngle.toDegrees() <= 180) {
            this.setPosition(new Angle());
        } else {
            this.setPosition(newAngle);
        }
    }

    @Override
    public void transferFile(String filename, String content, String origin) throws FileTransferException {
        int totalBytes = super.totalBytesStored();
        int size = content.length();
        ArrayList<String> filenames = super.getFilenames();
        for (String name : filenames) {
            if (name.equals(filename)) {
                throw new FileTransferException.VirtualFileAlreadyExistsException(filename);
            }
        }
        if ((totalBytes + content.length()) <= 200) {
            super.addFile(filename, content, size, origin);
        } else {
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
                Character nextChar = file.getTransferringContent().charAt(nextByte);
                if (bandwidth == Integer.MAX_VALUE && nextChar.equals('t')) {
                    continue;
                }
                newContent = newContent + nextChar;
                file.setContent(newContent);
                if (nextByte == (file.getSize() - 1)) {
                    file.setTransferringContent("");
                    file.setSize(file.getContent().length());
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
