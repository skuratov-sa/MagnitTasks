package com.skuratov.xml.model;


public record Product(String idArt, String name, String code, String guid) {
    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s", idArt, name, code, guid);
    }
}