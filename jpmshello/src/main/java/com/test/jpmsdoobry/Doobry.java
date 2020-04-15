package com.test.jpmsdoobry;

import com.test.jpmshi.HiModules;
import com.test.serviceproviderinterface.Service;

import java.lang.reflect.Method;

import static javax.xml.XMLConstants.XML_NS_PREFIX;

public class Doobry {

    private static void checkExportsDirectiveWithReflection() {
        try {
            Class c = Class.forName("com.test.jpmshi.HiModules");
            Method m = c.getMethod("getHi");
            System.out.println(m.invoke(c.getDeclaredConstructor().newInstance()));
            Method[] methods = c.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                System.out.println(methods[i].toString() + " is method: " + methods[i].getName());
            }
        }
        catch (Throwable e) {
            System.err.println(e);
        }
    }
    private static void checkOpensDirectiveWithReflection() {
        try {
            Class c = Class.forName("com.test.jpmsopens.OpensModules");
            Method m = c.getMethod("getHiOpens");
            System.out.println(m.invoke(c.getDeclaredConstructor().newInstance()));
        }
        catch (Throwable e) {
            System.err.println(e);
        }
    }
    private static void checkProvidesWith() {
        Service service = Service.getInstance();
        service.printServiceNames();
    }
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
        checkExportsDirectiveWithReflection();
        checkOpensDirectiveWithReflection();
        checkProvidesWith();
    }
}