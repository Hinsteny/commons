/**
 * @author Hinsteny
 * @version module-info: module-info 2019-05-09 15:57 All rights reserved.$
 * @since 9
 */
module com.github.hinsteny.commons.warp {

    requires java.xml;
    requires java.xml.bind;
    requires java.annotation;

    requires com.github.hinsteny.commons.core;

    requires slf4j.api;

    requires spring.context;

    requires poi;
    requires poi.ooxml;

    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.commons.collections4;

    requires fastjson;

    requires com.google.common;

}