package pl.cecherz.calcbill.utils;

public class MessageBuilder {

    private Class TARGET_CLASS;

    private final static String diagnosticAlert = "NullPointerException\ngetInfo() :: Unknown data. Function parameter is probably null.";

    public MessageBuilder(Class TARGET_CLASS) {
        this.TARGET_CLASS = TARGET_CLASS;
    }
    public void getInfo(Object... functionParameters) {
        StringBuilder diagnosticMessage = new StringBuilder();
        diagnosticMessage
                .append(TARGET_CLASS.toString())
                .append("\n");
        try {
            for (Object functionParameter : functionParameters) {
                diagnosticMessage
                        .append(functionParameter.toString())
                        .append("\n");
            }
            System.out.println(diagnosticMessage);
        } catch (NullPointerException e) {
            System.out.println(diagnosticAlert);
        }

    }

}
