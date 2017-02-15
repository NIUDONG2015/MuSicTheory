package musictheory.xinweitech.cn.musictheory.entity;

import java.io.Serializable;

/**
 * Created by niudong on 2017/1/25.
 */

public class LessonDetailReq implements Serializable {
    public String userNo;
    public int lessonId;

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
}
