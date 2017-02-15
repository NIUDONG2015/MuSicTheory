package musictheory.xinweitech.cn.musictheory.entity;

import java.io.Serializable;

/**
 * Created by niudong on 2017/2/10.
 */

public class LangCheckBean implements Serializable {


    private int id;
    private String name;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LangCheckBean() {
    }

    public LangCheckBean(String name, String key) {
        this.id = id;
        this.name = name;
        this.key = key;
    }

    public LangCheckBean(int id, String name, String key) {
        this.id = id;
        this.name = name;
        this.key = key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestCheckBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
