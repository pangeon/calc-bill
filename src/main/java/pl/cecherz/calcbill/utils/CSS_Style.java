package pl.cecherz.calcbill.utils;

public class CSS_Style {

    private String style;

    public void addParamStyle(String color) {
        style = "<style>" + "p {" + "color: " + color + "}" + "</style>";
    }
    public void addParamStyle(String color, String fontsize) {
        style = "<style>" + "p {"   + "color: " + color + "; "
                                    + "font-size: " + fontsize + "px" + "}" + "</style>";
    }

    public String getStyle() {
        return style;
    }
}
