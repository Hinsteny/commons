package com.github.hinsteny.commons.warp.io.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 单个sheet页的数据，第一行为表头，其余为数据
 *
 * @author Hinsteny
 * @version SheetData: SheetData 2019-05-10 09:41 All rights reserved.$
 */
public class SheetData {

    /**
     * 表头
     */
    private List<String> headers;

    /**
     * key列表
     */
    private List<String> keys;

    /**
     * 表头与map.key的对应关系
     */
    private List<HeaderKey> headerKeys;

    /**
     * map型数据
     */
    private List<Map<String,String>> dataMap;

    /**
     * list型数据
     */
    private List<List<String>> dataList;

    public SheetData(){
    }

    public SheetData(List<HeaderKey> headerKeys, List<Map<String,String>> dataMap) {
        this.headers = getHeadersByHeaderKeyMap(headerKeys);
        this.keys = getKeysByHeaderKeyMap(headerKeys);
        this.headerKeys = headerKeys;
        this.dataMap = dataMap;

    }

    public SheetData(List<HeaderKey> headerKeys, List<Map<String,String>> dataMap, List<List<String>> dataList) {
        this.headers = getHeadersByHeaderKeyMap(headerKeys);
        this.keys = getKeysByHeaderKeyMap(headerKeys);
        this.headerKeys = headerKeys;
        this.dataMap = dataMap;
        this.dataList = dataList;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<HeaderKey> getHeaderKeys() {
        return headerKeys;
    }

    public void setHeaderKeys(List<HeaderKey> headerKeys) {
        this.headerKeys = headerKeys;
    }

    public List<Map<String, String>> getDataMap() {
        return dataMap;
    }

    public void setDataMap(List<Map<String, String>> dataMap) {
        this.dataMap = dataMap;
    }

    public List<List<String>> getDataList() {
        return dataList;
    }

    public void setDataList(List<List<String>> dataList) {
        this.dataList = dataList;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    private List<String> getKeysByHeaderKeyMap(List<HeaderKey> headerKeys){
        List<String> keyList = new ArrayList<>();
        for(HeaderKey headerKey : headerKeys){
            keyList.add(headerKey.getKey());
        }
        return keyList;
    }

    private List<String> getHeadersByHeaderKeyMap(List<HeaderKey> headerKeys){
        List<String> headerList= new ArrayList<>();
        for(HeaderKey headerKey : headerKeys){
            headerList.add(headerKey.getName());
        }
        return headerList;
    }

}
