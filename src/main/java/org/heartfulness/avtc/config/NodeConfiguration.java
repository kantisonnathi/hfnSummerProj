package org.heartfulness.avtc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("node")
public class NodeConfiguration {

    private String menuNode;

    private String departmentNode;

    private String customMessageNode;

    private String voicemailNode;

    private String extensionNode;

    private String inputAndResponseNode;


    public String getMenuNode() {
        return menuNode;
    }

    public void setMenuNode(String menuNode) {
        this.menuNode = menuNode;
    }

    public String getDepartmentNode() {
        return departmentNode;
    }

    public void setDepartmentNode(String departmentNode) {
        this.departmentNode = departmentNode;
    }

    public String getCustomMessageNode() {
        return customMessageNode;
    }

    public void setCustomMessageNode(String customMessageNode) {
        this.customMessageNode = customMessageNode;
    }

    public String getVoicemailNode() {
        return voicemailNode;
    }

    public void setVoicemailNode(String voicemailNode) {
        this.voicemailNode = voicemailNode;
    }

    public String getExtensionNode() {
        return extensionNode;
    }

    public void setExtensionNode(String extensionNode) {
        this.extensionNode = extensionNode;
    }

    public String getInputAndResponseNode() {
        return inputAndResponseNode;
    }

    public void setInputAndResponseNode(String inputAndResponseNode) {
        this.inputAndResponseNode = inputAndResponseNode;
    }
}
