package unsw.blackout;

public class File {
    private String filename;
    private String content;
    private int size;
    private String transferringContent;
    private String origin;

    public File(String filename, String content) {
        this.filename = filename;
        this.content = content;
        this.transferringContent = "";
        this.size = content.length();
        this.origin = "";
    }

    public File(String filename, String content, int size, String origin) {
        this.filename = filename;
        this.transferringContent = content;
        this.content = "";
        this.size = size;
        this.origin = origin;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTransferringContent() {
        return transferringContent;
    }

    public void setTransferringContent(String transferringContent) {
        this.transferringContent = transferringContent;
    }

}
