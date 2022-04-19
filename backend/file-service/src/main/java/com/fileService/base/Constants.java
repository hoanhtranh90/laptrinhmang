package com.fileService.base;

public class Constants {

    public interface DELETE {
        Long DELETED = 1L;
        Long NORMAL = 0L;
    }

    public interface ATTACH_DOCUMENT {
        Long ENCRYPTED = 1L;
        Long PDF = 1L;
        Long ORIGINAL = 1L;
    }

    public interface DOWNLOAD_OPTION {
        interface TYPE {
            String ORIGINAL = "original";
            String PDF = "pdf";
            String IMAGE = "image";
            String TRANG_KY_SO = "trangKySo";
        }
    }

    public interface FILE_EXTENSION {
        String IMAGE_PNG = ".png";
    }

    public interface IMAGE_EXTRACT_STATUS {
        String ORIGINAL = "original";
        String COMMENT = "comment";
    }

    public interface CONTENT_TYPE {
        String PDF = "application/pdf";
        String DOC = "application/msword";
        String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    }

    public interface STATUS {
        String ACTIVE = "active";
        String DEACTIVE = "deactive";
    }
}
