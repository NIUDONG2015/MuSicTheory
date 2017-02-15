package musictheory.xinweitech.cn.musictheory.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by niudong on 2017/1/27.
 */


public class HomeDetailEntity implements Serializable {

    /**
     * err : 0
     * data : {"lessonId":1,"title":"五线谱、谱号和附加线","status":1,"list":[{"content":"五线谱的每一行..","type":0,"audioUrls":"http:127.0.0.1/a-1.mp3"},{"content":"五线谱的每一行..","type":0},{"content":"http://127.0.0.1/img.png","type":1}]}
     */

    private int err;
    /**
     * lessonId : 1
     * title : 五线谱、谱号和附加线
     * status : 1
     * list : [{"content":"五线谱的每一行..","type":0,"audioUrls":"http:127.0.0.1/a-1.mp3"},{"content":"五线谱的每一行..","type":0},{"content":"http://127.0.0.1/img.png","type":1}]
     */

    private DataBean data;

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int lessonId;
        private String title;
        private int status;
        /**
         * content : 五线谱的每一行..
         * type : 0
         * audioUrls : http:127.0.0.1/a-1.mp3
         */

        private List<ListBean> list;

        public int getLessonId() {
            return lessonId;
        }

        public void setLessonId(int lessonId) {
            this.lessonId = lessonId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String content;
            private int type;
            private String audioUrls;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getAudioUrls() {
                return audioUrls;
            }

            public void setAudioUrls(String audioUrls) {
                this.audioUrls = audioUrls;
            }
        }
    }
}
