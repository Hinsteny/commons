package com.github.hinsteny.commons.core.utils;

import java.net.InetAddress;

/**
 * @author Hinsteny
 * @version IPUtil: IPUtil 2019-05-10 10:18 All rights reserved.$
 */
public class IPUtil {

    /**
     * 虚拟机IP
     */
    public static final String JVM_SERVER_IP = "server.ip";

    /**
     * 容器IP
     */
    public static final String ENV_SERVER_IP = "container.ip";

    private static String VM_IP;

    /**
     * 获取JVM的IP
     *
     * @return ip
     */
    public static String getVMIp() {
        if (StringUtil.isNullOrEmpty(VM_IP)) {
            try {
                // 如果配置了vm参数，已vm参数为准
                String vmIp = System.getProperty(JVM_SERVER_IP);
                if (!StringUtil.isNullOrEmpty(vmIp)) {
                    VM_IP = vmIp;
                    return VM_IP;
                }

                // 主要针对docker容器，网络是nat模式，需要将映射IP放入环境变量
                String envIp = System.getenv(ENV_SERVER_IP);
                if (!StringUtil.isNullOrEmpty(envIp)) {
                    VM_IP = envIp;
                    return VM_IP;
                }

                if (StringUtil.isNullOrEmpty(VM_IP)) {
                    VM_IP = InetAddress.getLocalHost().getHostAddress();
                }
            } catch (Exception e) {
                VM_IP = "127.0.0.1";
            }
        }
        return VM_IP;
    }

}
