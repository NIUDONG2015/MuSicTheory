package musictheory.xinweitech.cn.musictheory.entity;

import java.io.Serializable;

/**
 * Created by niudong on 2017/1/24.
 */


public class ForgetPwd implements Serializable {


    /**
     * err : 0
     */

    public String errMsg;

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int err;

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }
}
