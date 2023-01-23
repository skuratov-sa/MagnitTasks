package com.skuratov.jmxProject.simple;

import com.skuratov.jmxProject.simple.jmx.UserServiceMBean;
import com.skuratov.jmxProject.simple.services.UserService;
import lombok.SneakyThrows;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;
import java.lang.management.ManagementFactory;

public class AppStart {

    @SneakyThrows
    public static void main(String[] args) {
        MBeanServer mServer = ManagementFactory.getPlatformMBeanServer();
        var name = new ObjectName("com.skuratov.jmxProject.simple.services:type=UserService");
        StandardMBean standardMBean = new StandardMBean(new UserService(), UserServiceMBean.class);
        mServer.registerMBean(standardMBean, name);
        System.out.println("Sleep thread");

        Thread.sleep(Long.MAX_VALUE);
    }
}
