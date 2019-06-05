/**
 * @author Hinsteny
 * @version module-info: module-info 2019-05-09 16:05 All rights reserved.$
 * @since 9
 */
module com.github.hinsteny.commons.core {

    requires java.desktop;
    requires org.apache.logging.log4j;

    exports com.github.hinsteny.commons.core.base;
    exports com.github.hinsteny.commons.core.functional.page;
    exports com.github.hinsteny.commons.core.functional.secret;
    exports com.github.hinsteny.commons.core.io.csv;
    exports com.github.hinsteny.commons.core.io.image;
    exports com.github.hinsteny.commons.core.utils;

}