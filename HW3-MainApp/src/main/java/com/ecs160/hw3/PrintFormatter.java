package com.ecs160.hw3;

public class PrintFormatter {
    public void printFormattedResponse(int index, int total, String content, String response, boolean isReply) {
        String prefix = isReply ? "---> " : "";
        if (!response.contains("#")) {
            System.out.println(prefix + "(" + index + "/" + total + ") [" + response + "]");
        } else {
            System.out.println(prefix + "(" + index + "/" + total + ") " + content + " " + response);
        }
    }
}
