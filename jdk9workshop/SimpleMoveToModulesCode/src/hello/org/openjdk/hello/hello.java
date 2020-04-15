package org.openjdk.hello;

import org.openjdk.text.*;

class hello {
    //Originally - here - moved into separate module
    //
    // private static String leftPad(String s, int w) {
    //     StringBuilder sb = new StringBuilder();
    //     int n = w - s.length();
    //     for (int i = 0; i < n; i++) {
    //         sb.append(" ");
    //     }
    //     sb.append(s);
    //     return sb.toString();
    // }
    public static void main(String ... args){
        String adj = args.length > 0 ? (" " + args[0]) : "";
        System.out.println(Padder.leftPad("Hello," + adj + " world!", 90));
    }
}