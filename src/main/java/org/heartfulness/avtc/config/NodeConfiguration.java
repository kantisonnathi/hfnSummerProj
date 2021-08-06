package org.heartfulness.avtc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("node")
public class NodeConfiguration {

    private String englishNode;

    private String hindiNode;

    private String anonymousUUID;

    public String getAnonymousUUID() {
        return anonymousUUID;
    }

    public void setAnonymousUUID(String anonymousUUID) {
        this.anonymousUUID = anonymousUUID;
    }

    public String getEnglishNode() {
        return englishNode;
    }

    public void setEnglishNode(String englishNode) {
        this.englishNode = englishNode;
    }

    public String getHindiNode() {
        return hindiNode;
    }

    public void setHindiNode(String hindiNode) {
        this.hindiNode = hindiNode;
    }

}
