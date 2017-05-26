package jp.s64.java.repoli.internal;

/**
 * Created by shuma on 2017/05/26.
 */

public class ByteArrayContainer {

    private byte[] body;
    private byte[] attachment;
    Long savedAtTimeMillis;
    Long requestedAtTimeMillis;

    public ByteArrayContainer() {

    }

    public ByteArrayContainer(ByteArrayContainer org) {
        setBody(org.getBody());
        setAttachment(org.getAttachment());
        setSavedAtTimeMillis(org.getSavedAtTimeMillis());
        setRequestedAtTimeMillis(org.getRequestedAtTimeMillis());
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public Long getSavedAtTimeMillis() {
        return savedAtTimeMillis;
    }

    public void setSavedAtTimeMillis(Long savedAtTimeMillis) {
        this.savedAtTimeMillis = savedAtTimeMillis;
    }

    public Long getRequestedAtTimeMillis() {
        return requestedAtTimeMillis;
    }

    public void setRequestedAtTimeMillis(Long requestedAtTimeMillis) {
        this.requestedAtTimeMillis = requestedAtTimeMillis;
    }

}
