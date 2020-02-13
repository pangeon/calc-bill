package pl.cecherz.calcbill.utils;

public class ContentHTMLWrapper {
    public static String wrapToHTML(Object content) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                    "<head>\n" +
                        "<title></title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                        content +
                    "</body>\n" +
                "</html>";
    }
    public static String wrapToHTML(Object content, String title) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                    "<head>\n" +
                        "<title>" + title + "</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                        content +
                    "</body>\n" +
                "</html>";
    }
    public static String wrapToHTML(Object content, String title, String style) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                    "<head>\n" +
                        "<title>" + title + "</title>\n" +
                        style +
                    "</head>\n" +
                    "<body>\n" +
                        content +
                    "</body>\n" +
                "</html>";
    }
}
