//package com.example.socketpart_1.data;
package com.kgg.controlpc.data;

//0文件，1文件夹，2盘符
//文件夹默认大小为0

public class NetFileData {
    private Long fileDate;
    private String fileName="$error";
    private long fileSize = 0;
    private String filePath="./";
    private int fileType=0;

    public NetFileData(Long fileDate, String fileName, int fileType) {
        this.fileDate = fileDate;
        this.fileName = fileName;
        this.fileType = fileType;
    }

    public NetFileData(Long fileDate, String fileName, long fileSize, int fileType) {
        this.fileDate = fileDate;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getFileDate() {
        return fileDate;
    }

    public void setFileDate(Long fileDate) {
        this.fileDate = fileDate;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    @Override
    public String toString() {
        return this.fileName + ">" + this.fileDate + ">" + this.fileSize + ">" + this.fileType;
    }
}
