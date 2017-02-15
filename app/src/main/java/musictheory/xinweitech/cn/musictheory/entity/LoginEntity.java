package musictheory.xinweitech.cn.musictheory.entity;

import java.io.Serializable;

/**
 * Created by niudong on 2017/1/19.
 * <p>
 * 服务器返回的数据
 */


public class LoginEntity implements Serializable {


    /**
     * err : 0
     * data : {"loginName":"1@163.com","userNo":"de729c72f253ba836102f1e55cd35842","status":0}
     */

    private int err;
    /**
     * loginName : 1@163.com
     * userNo : de729c72f253ba836102f1e55cd35842
     * satus : 0
     */
    public String errMsg;

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

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
        private String loginName;
        private String userNo;
        private int status;

        public String getLoginName() {
            return loginName;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public String getUserNo() {
            return userNo;
        }

        public void setUserNo(String userNo) {
            this.userNo = userNo;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
