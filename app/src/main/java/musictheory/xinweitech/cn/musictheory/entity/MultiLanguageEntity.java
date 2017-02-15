package musictheory.xinweitech.cn.musictheory.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by niudong on 2017/2/11.
 */


public class MultiLanguageEntity implements Serializable {


    /**
     * err : 0
     * data : {"count":1,"list":[{"itemKey":"zh","label":"简体中文","weight":1}]}
     */
    private String errMsg;

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    private int err;
    /**
     * count : 1
     * list : [{"itemKey":"zh","label":"简体中文","weight":1}]
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
         * itemKey : zh
         * label : 简体中文
         * weight : 1
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
            private String itemKey;
            private String label;
            private int weight;

            public String getItemKey() {
                return itemKey;
            }

            public void setItemKey(String itemKey) {
                this.itemKey = itemKey;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public int getWeight() {
                return weight;
            }

            public void setWeight(int weight) {
                this.weight = weight;
            }
        }
    }
}
