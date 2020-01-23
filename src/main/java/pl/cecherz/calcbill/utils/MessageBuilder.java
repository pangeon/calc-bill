package pl.cecherz.calcbill.utils;

public class MessageBuilder {

    private static Class TARGET_CLASS;

    public static void setTargetClass(Class targetClass) {
        TARGET_CLASS = targetClass;
    }
    private static String showClass() {
        return TARGET_CLASS.toString();
    }
    public static void getInfo(Object... functionParameters) {
        StringBuilder diagnosticMessageBilder = new StringBuilder();
        diagnosticMessageBilder
                .append(MessageBuilder.showClass())
                .append("\n");
        for (Object functionParameter : functionParameters) {
            diagnosticMessageBilder
                    .append(functionParameter.toString())
                    .append("\n");
        }
        System.out.println(diagnosticMessageBilder);
    }

}
