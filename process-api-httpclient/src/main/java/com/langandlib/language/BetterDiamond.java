package com.langandlib.language;

import java.util.ArrayList;

public class BetterDiamond {

    public static void main(String... args) {
        ArrayList<String> list1 = new ArrayList<String>();
        ArrayList<String> list2 = new ArrayList<>();
        ArrayList<String> list3 = new ArrayList<>() {
            @Override
            public boolean add(String s) {
                System.out.println("Adding " + s);
                return super.add(s);
            }
        };
    }

}