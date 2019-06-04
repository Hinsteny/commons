package com.github.hinsteny.commons.warp.io.excel;

/**
 * @author Hinsteny
 * @version HeaderKey: HeaderKey 2019-05-10 09:41 All rights reserved.$
 */
public class HeaderKey {

    private String key;

    private String name;

    public HeaderKey(){
    }

    public HeaderKey(String key, String name){
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
