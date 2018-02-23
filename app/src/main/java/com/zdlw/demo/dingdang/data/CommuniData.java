package com.zdlw.demo.dingdang.data;

/**
 * @author LinWei on 2017/12/6 14:15
 */
public class CommuniData {
    private boolean isHost;
    private String pic;
    private String text;

    public CommuniData(boolean isHost, String pic, String text) {
        this.isHost = isHost;
        this.pic = pic;
        this.text = text;
    }

    public boolean isHost() {
        return isHost;
    }

    public void setHost(boolean host) {
        isHost = host;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
