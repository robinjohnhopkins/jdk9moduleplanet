package com.test.jpmsdoobry;

import com.test.jpmshi.HiModules;

import static javax.xml.XMLConstants.XML_NS_PREFIX;

public class Doobry {

    //Originally - here - moved into separate module
    //
     private static String leftPad(String s, int w) {
         StringBuilder sb = new StringBuilder();
         int n = w - s.length();
         for (int i = 0; i < n; i++) {
             sb.append(" ");
         }
         sb.append(s);
         return sb.toString();
     }
    public static void main(String ... args){
        String adj = args.length > 0 ? (" " + args[0]) : "";
        System.out.println(leftPad("Hello," + adj + " world!", 90));
        System.out.println("The XML namespace prefix is: " + XML_NS_PREFIX);
        HiModules hiModules = new HiModules();
        System.out.println(hiModules.getHi());
    }
}