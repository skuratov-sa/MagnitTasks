package com.skuratov.jmxProject.simple.services;

import com.skuratov.jmxProject.simple.jmx.UserServiceMBean;

import java.util.HashMap;
import java.util.Map;

public class UserService implements UserServiceMBean {

    private final Map<String, Float> moneyByUser = new HashMap<>();

    @Override
    public void addUser(String name, String sex, short age) {
        moneyByUser.put(name, 0F);
    }

    @Override
    public String getTalkUser(String name) {
        return String.format("%s say %s", name, moneyByUser.get(name) > 0 ? "'many money'" : "'lack money");
    }

    @Override
    public void sendMoney(String user, float money) {
        moneyByUser.put(user, money);
    }

}
