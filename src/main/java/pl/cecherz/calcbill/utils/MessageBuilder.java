package pl.cecherz.calcbill.utils;

public class MessageBuilder {

    private Class TARGET_CLASS;

    public MessageBuilder(Class TARGET_CLASS) {
        this.TARGET_CLASS = TARGET_CLASS;
    }
    public void getInfo(Object... functionParameters) {
        StringBuilder diagnosticMessage = new StringBuilder();
        diagnosticMessage
                .append(TARGET_CLASS.toString())
                .append("\n");
        for (Object functionParameter : functionParameters) {
            diagnosticMessage
                    .append(functionParameter.toString())
                    .append("\n");
        }
        System.out.println(diagnosticMessage);
    }

}
