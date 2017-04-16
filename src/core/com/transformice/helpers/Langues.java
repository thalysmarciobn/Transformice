package com.transformice.helpers;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.THashMap;

public class Langues {
    public TMap<Integer, String> langues;

    public Langues() {
        this.langues = new THashMap();
        this.langues.put(0, "EN");
        this.langues.put(1, "FR");
        this.langues.put(2, "RU");
        this.langues.put(3, "BR");
        this.langues.put(4, "ES");
        this.langues.put(5, "CN");
        this.langues.put(6, "TR");
        this.langues.put(7, "VK");
        this.langues.put(8, "PL");
        this.langues.put(9, "HU");
        this.langues.put(10, "NL");
        this.langues.put(11, "RO");
        this.langues.put(12, "ID");
        this.langues.put(13, "DE");
        this.langues.put(14, "E2");
        this.langues.put(15, "AR");
        this.langues.put(16, "PH");
        this.langues.put(17, "LT");
        this.langues.put(18, "JP");
        this.langues.put(19, "CH");
        this.langues.put(20, "FI");
        this.langues.put(21, "CZ");
        this.langues.put(22, "SK");
        this.langues.put(23, "HR");
        this.langues.put(24, "BU");
        this.langues.put(25, "LV");
        this.langues.put(26, "HE");
        this.langues.put(27, "IT");
        this.langues.put(29, "ET");
        this.langues.put(30, "AZ");
        this.langues.put(31, "PT");
    }

    public String getLangue(int langueByte) {
        if (this.langues.containsKey(langueByte)) {
            return this.langues.get(langueByte);
        }
        return this.langues.get(0);
    }
}