package edu.curtin.terminalgriddemo;

public class Plugin {
    private String pluginId;
    private String key1;
    private String key2;

    public Plugin(String pluginId, String key1, String key2) {
        this.pluginId = pluginId;
        this.key1 = key1;
        this.key2 = key2;
    }

    public static Plugin parse(String line) {
        // You need to actually parse the line to get values for pluginId, key1, and key2
        // Here's a placeholder that you'll need to replace with actual parsing logic:
        return new Plugin("sampleId", "sampleKey1", "sampleKey2");
    }

    public String getPluginId() {
        return pluginId;
    }

    public String getKey1() {
        return key1;
    }

    public String getKey2() {
        return key2;
    }
}
