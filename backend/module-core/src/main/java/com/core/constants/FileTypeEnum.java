package com.core.constants;

public enum FileTypeEnum {

    DG_FILE(12L, "DG_File"), BANNER(13L, "Banner"), STUTUTE(14L, "Stute"), AVATAR(15L, "Avatar");

    private final Long fileType;
    private final String fileTypeDescription;

    private FileTypeEnum(Long fileType, String fileTypeDescription) {
        this.fileType = fileType;
        this.fileTypeDescription = fileTypeDescription;
    }

    public Long getFileType() {
        return fileType;
    }

    public String getFileTypeDescription() {
        return fileTypeDescription;
    }

    public static String getFileTypeDescription(Long fileType) {
        for (FileTypeEnum fileTypeDescription : FileTypeEnum.values()) {
            if (fileTypeDescription.getFileType().equals(fileType)) {
                return fileTypeDescription.getFileTypeDescription();
            }
        }
        return null;
    }
}
