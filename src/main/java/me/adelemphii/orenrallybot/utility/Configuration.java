package me.adelemphii.orenrallybot.utility;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Configuration {

    String token;
    String prefix;
    List<Long> adminRoles;
    List<Long> dmRoles;

    String status;
    String embedColor;

    public String getToken() {
        return token;
    }

    public String getPrefix() {
        return prefix;
    }

    public List<Long> getAdminRoles() {
        return adminRoles;
    }

    public List<Long> getDmRoles() {
        return dmRoles;
    }

    public String getStatus() {
        return status;
    }

    public String getEmbedColor() {
        return embedColor;
    }
}
