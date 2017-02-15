package musictheory.xinweitech.cn.musictheory.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by niudong on 2017/2/3.
 */


public class AdvertListEntity implements Serializable {


    /**
     * err : 0
     * data : {"count":1,"list":[{"icoUrl":"http://127.0.0.1/1.png","content":"hahaha","linkUrl":"http://127.0.0.1/2.png"}]}
     */

    private int err;
    private String errMsg;

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    /**
     * count : 1
     * list : [{"icoUrl":"http://127.0.0.1/1.png","content":"hahaha","linkUrl":"http://127.0.0.1/2.png"}]
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
        private int count;
        /**
         * icoUrl : http://127.0.0.1/1.png
         * content : hahaha
         * linkUrl : http://127.0.0.1/2.png
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
            private String icoUrl;
            private String content;
            private String linkUrl;

            public String getIcoUrl() {
                return icoUrl;
            }

            public void setIcoUrl(String icoUrl) {
                this.icoUrl = icoUrl;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getLinkUrl() {
                return linkUrl;
            }

            public void setLinkUrl(String linkUrl) {
                this.linkUrl = linkUrl;
            }
        }
    }
}
