package com.github.hinsteny.commons.core.io.image;

/**
 * 图片文件描述对象
 *
 * @author Hinsteny
 * @version ImageInfo: ImageInfo 2019-05-09 15:13 All rights reserved.$
 */
public class ImageInfo {

    /**
     * 图片类型, jpg, png, 等
     */
    private String type;

    /**
     * 图片文件名称
     */
    private String name;

    /**
     * 图片分辨率 (640x480)
     */
    private String geometry;

    /**
     * 图片宽度
     */
    private int width;

    /**
     * 图片高度
     */
    private int height;

    /**
     * 图片文件大小 (15.3ki)
     */
    private String size;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGeometry() {
        return geometry;
    }

    public void setGeometry(String geometry) {
        this.geometry = geometry;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
            "type='" + type + '\'' +
            ", name='" + name + '\'' +
            ", geometry='" + geometry + '\'' +
            ", width=" + width +
            ", height=" + height +
            ", size='" + size + '\'' +
            '}';
    }
}
