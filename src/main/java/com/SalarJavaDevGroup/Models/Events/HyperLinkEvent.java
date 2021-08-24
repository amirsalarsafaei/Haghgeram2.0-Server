package com.SalarJavaDevGroup.Models.Events;

import com.SalarJavaDevGroup.Models.HyperLinkText;

public class HyperLinkEvent {
    private String hyperlink;
    private HyperLinkText hyperLinkText;

    public HyperLinkText getHyperLinkText() {
        return hyperLinkText;
    }

    public void setHyperLinkText(HyperLinkText hyperLinkText) {
        this.hyperLinkText = hyperLinkText;
    }

    public String getHyperlink() {
        return hyperlink;
    }

    public void setHyperlink(String hyperlink) {
        this.hyperlink = hyperlink;
    }

    public HyperLinkEvent(String hyperlink, HyperLinkText hyperLinkText) {
        this.hyperlink = hyperlink;
        this.hyperLinkText = hyperLinkText;
    }
}
