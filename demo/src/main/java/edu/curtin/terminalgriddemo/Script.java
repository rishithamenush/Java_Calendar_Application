package edu.curtin.terminalgriddemo;

public class Script {
    private static String scriptContent;

    public Script(String scriptContent) {
        this.scriptContent = scriptContent;
    }

    public static Script parse(String line) {
        return new Script(scriptContent);
    }

    public String getScriptContent() {
        return scriptContent;
    }
}
