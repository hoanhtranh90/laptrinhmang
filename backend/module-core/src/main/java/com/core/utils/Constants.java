/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sadfsafbhsaid
 */
public class Constants {


    public interface ROLE {
        String USER = "USER";
        String ADMIN = "ADMIN";
    }

    public interface SEND_STATUS {
        Long SENT = 1L;
        Long NOT_SENT = 0L;
    }

    public interface ACTION_HISTORY_FOR_CARE_REGISTER {
        String CALL = "Gọi điện";
        String MAIL = "Gửi email";

        public interface REGISTER_AUCTION_HISTORY {
            String REGISTER = "Đã nộp hồ sơ";
            String DENIED = "Từ chối tham gia";
            String REGISTER_FAIL = "Đăng ký không hợp lệ";
            String REGISTER_SUCCESS = "Đăng ký thành công";
        }
    }

    public interface TYPE_HISTORY {
        Long CARE = 0L;
        Long REGISTRATION = 1L;
    }

    public interface CALL_STATUS {
        Long COMPLETED = 1L;
        Long IN_PROGRESS = 0L;
    };

    public interface STOP {
        Long STOP = 2L;
        Long PAUSE = 1L;
        Long NOT_STOP = 0L;
    }
    public interface EMAIL {
        String EMAIL_USER = "sendmailtestz2@gmail.com";
        String EMAIL_PASSWORD = "123456a@A";
    }

    public interface REGISTER_STATUS {

        interface AUCTION_STATUS {
            Long SUCCESS = 1L;
            Long WAIT = 0L;
            Long FAIL = 2L;
            Long DENIED = 3L;
        }
        interface PRECOST_STATUS {
            Long CHUA_NOP_TIEN = 0L;
            Long THANH_CONG = 2L;
            Long THAT_BAI = 3L;
            Long CHO_DOI_SOAT = 1L;
        }



    }


    public interface UPLOAD_FILE_CONFIG {
        interface TYPE {
            String IMAGE = "image";
            String VIDEO = "video";
            String ANOTHER = "another";
        }
    }
    public interface DATE_FORMAT {
        String DATE_FORMAT_DD_MM_YYYY = "dd/MM/yyyy";
        String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
        String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    };

    public interface DOWNLOAD_OPTION {

        interface TYPE {

            String ORIGINAL = "original";
            String PDF = "pdf";
            String IMAGE = "image";
            String TRANG_KY_SO = "trangKySo";
        }
    }


    public interface CONTRACT_STATUS {

        Long TIEP_NHAN = 0L;
        Long DANG_XU_LY = 1L;
        Long DA_KY = 2L;
    }

    public interface DELETE {

        Long DELETED = 1L;
        Long NORMAL = 0L;
    }
    public interface TYPE_OF_FILE {

        Long BANNER = 13L;
        Long PRODUCT_AUCTION = 12L;
        Long STATUTE = 14L;
        Long AVATAR = 15L;

    }

    public interface TYPE_OF_PRODUCT {

        Long CHUA_TAO_QUYCCHE = 123L;
        Long DA_TAO_QUYCHE = 124L;
        Long DA_KET_THUC = 125L;

    }
    public interface BID_ORDER_STATUS {

        Long CHUA_KY = 1L;
        Long DA_KY = 2L;
    }

    public interface CONTENT_TYPE {

        String DOC = "application/msword";
        String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        String PDF = "application/pdf";
    }

    public interface ATTACHMENT_OBJECT_TYPE {

        Long VB_DI = -1L;
        Long VB_KHAC = 0L;
        Long VB_INTERF = 1L;
        Long VB_INTERF_LIEN_QUAN = 2L;

        Long VB_PLAN = 3L;
        Long VB_OUT_GOING_DOC = 4L;

        Long TAI_SAN_DAU_GIA = 12L;
        Long BANNER = 13L;

    }

    public static List<Long> ATTACHMENT_RELATION_TYPES = new ArrayList<>();

    static {
        ATTACHMENT_RELATION_TYPES.add(ATTACHMENT_RELATION_TYPE.PHU_LUC_DINH_KEM);
        ATTACHMENT_RELATION_TYPES.add(ATTACHMENT_RELATION_TYPE.BAN_DO);
        ATTACHMENT_RELATION_TYPES.add(ATTACHMENT_RELATION_TYPE.GHI_AM);
        ATTACHMENT_RELATION_TYPES.add(ATTACHMENT_RELATION_TYPE.KHAC);
    }

    public interface ATTACHMENT_RELATION_TYPE {

        Long CONG_VAN_GOC = 0L;
        Long PHU_LUC_DINH_KEM = 1L;
        Long BAN_DO = 2L;
        Long GHI_AM = 3L;
        Long KHAC = 4L;

    }
    public interface TYPE_OF_STATUTE {

        Long CHUA_PHAN_CONG = 1L;
        Long DA_PHAN_CONG = 2L;
        Long TAM_DUNG = 3L;
    }

    public interface TYPE_OF_CONTRACT {
        Long CHUA_XU_LY = 0L;
        Long DANG_XU_LY = 1L;
        Long KY_HOP_DONG = 2L;
        Long KHONG_KY_HOP_DONG = 3L;
        Long DA_HUY = 4L;
    }


    public interface AUCTION_TYPE {

        Long TRUC_TUYEN = 0L;
        Long KHAC = 1L;
    }

    public interface MANAGER_STATUS {
        Long ACTIVE = 1L;
        Long INACTIVE = 0L;
    }

}
