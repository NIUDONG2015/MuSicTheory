package musictheory.xinweitech.cn.musictheory.entity;

import java.io.Serializable;

/**
 * Created by niudong on 2017/1/24.
 */


public class CheckStatusRequest implements Serializable {


    /**
     * userNo : de729c72f253ba836102f1e55cd35842
     * lessonId : 1
     * status : 0
     */

    private String userNo;
    private int lessonId;
    private int status;

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
