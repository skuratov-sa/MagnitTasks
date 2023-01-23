package com.skuratov.jmxProject.simple.jmx;

public interface UserServiceMBean {

    void addUser(String name, String sex, short age);

    String getTalkUser(String user);

    void sendMoney(String user, float money);
}
