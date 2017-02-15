package musictheory.xinweitech.cn.musictheory.entity;

import java.io.Serializable;

/**
 * Created by niudong on 2017/1/24.
 */


public class ChangePwdEntity implements Serializable {


    /**
     * err : 1
     * errMsg : 系统操作异常
     */

    private int err;
    private String errMsg;

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
