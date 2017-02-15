package musictheory.xinweitech.cn.musictheory.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by niudong on 2017/1/21.
 */


public class LessonListEntity implements Serializable {


    /**
     * err : 0
     * data : {"count":2,"list":[{"category":"基础知识","dList":[{"lessonId":1,"icoUrl":"http://127.0.0.1/1.png","title":"五线谱、谱号和附加线","status":1}]}]}
     */

    private int err;
    /**
     * count : 2
     * list : [{"category":"基础知识","dList":[{"lessonId":1,"icoUrl":"http://127.0.0.1/1.png","title":"五线谱、谱号和附加线","status":1}]}]
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

    public static class DataBean implements Serializable {
        private int count;
        /**
         * category : 基础知识
         * dList : [{"lessonId":1,"icoUrl":"http://127.0.0.1/1.png","title":"五线谱、谱号和附加线","status":1}]
         */

        private List<ListBean> list;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String category;
            /**
             * lessonId : 1
             * icoUrl : http://127.0.0.1/1.png
             * title : 五线谱、谱号和附加线
             * status : 1
             */

            private List<DListBean> dList;

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public List<DListBean> getDList() {
                return dList;
            }

            public void setDList(List<DListBean> dList) {
                this.dList = dList;
            }

            public static class DListBean implements Serializable{
                private int lessonId;
                private String icoUrl;
                private String title;
                private int status;

                public int getLessonId() {
                    return lessonId;
                }

                public void setLessonId(int lessonId) {
                    this.lessonId = lessonId;
                }

                public String getIcoUrl() {
                    return icoUrl;
                }

                public void setIcoUrl(String icoUrl) {
                    this.icoUrl = icoUrl;
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
            }
        }
    }
}