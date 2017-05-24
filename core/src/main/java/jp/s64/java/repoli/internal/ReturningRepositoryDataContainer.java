package jp.s64.java.repoli.internal;

import jp.s64.java.repoli.core.IRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/19.
 */

public class ReturningRepositoryDataContainer<T, A> implements IRepositoryDataContainer<T, A> {

    private T body;
    private A attachment;
    private Long savedAtTimeMillis = null;
    private Long requestedAtTimeMillis = null;

    public ReturningRepositoryDataContainer() {

    }

    public ReturningRepositoryDataContainer(IRepositoryDataContainer<T, A> original) {
        setBody(original.getBody());
        setAttachment(original.getAttachment());
        setSavedAtTimeMillis(original.getSavedAtTimeMillis());
        setRequestedAtTimeMillis(original.getRequestedAtTimeMillis());
    }

    @Override
    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public A getAttachment() {
        return attachment;
    }

    public void setAttachment(A attachment) {
        this.attachment = attachment;
    }

    @Override
    public Long getSavedAtTimeMillis() {
        return savedAtTimeMillis;
    }

    public void setSavedAtTimeMillis(Long savedAtTimeMillis) {
        this.savedAtTimeMillis = savedAtTimeMillis;
    }

    @Override
    public Long getRequestedAtTimeMillis() {
        return requestedAtTimeMillis;
    }

    public void setRequestedAtTimeMillis(Long requestedAtTimeMillis) {
        this.requestedAtTimeMillis = requestedAtTimeMillis;
    }

}
