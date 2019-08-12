/**
 * @author Hinsteny
 * @version module-info: module-info 2019-05-09 15:57 All rights reserved.$
 * @since 9
 */
module com.github.hinsteny.commons.warp {

    requires java.xml;
    requires java.xml.bind;

    requires com.github.hinsteny.commons.core;

    requires slf4j.api;

    requires poi;
    requires poi.ooxml;

    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;

    requires fastjson;

    requires com.google.common;

}