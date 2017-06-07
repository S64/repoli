package jp.s64.java.repoli.internal;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import jp.s64.java.repoli.core.IRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/19.
 */

public class ReturningRepositoryDataContainer<T, A> implements IRepositoryDataContainer<T, A> {

    @Nullable
    private T body;

    @Nullable
    private A attachment;

    @Nullable
    private Long savedAtTimeMillis = null;

    @Nullable
    private Long requestedAtTimeMillis = null;

    public ReturningRepositoryDataContainer() {

    }

    public ReturningRepositoryDataContainer(@NotNull IRepositoryDataContainer<T, A> original) {
        setBody(original.getBody());
        setAttachment(original.getAttachment());
        setSavedAtTimeMillis(original.getSavedAtTimeMillis());
        setRequestedAtTimeMillis(original.getRequestedAtTimeMillis());
    }

    @Nullable
    @Override
    public T getBody() {
        return body;
    }

    public void setBody(@Nullable T body) {
        this.body = body;
    }

    @Nullable
    @Override
    public A getAttachment() {
        return attachment;
    }

    public void setAttachment(@Nullable A attachment) {
        this.attachment = attachment;
    }

    @Nullable
    @Override
    public Long getSavedAtTimeMillis() {
        return savedAtTimeMillis;
    }

    public void setSavedAtTimeMillis(@Nullable Long savedAtTimeMillis) {
        this.savedAtTimeMillis = savedAtTimeMillis;
    }

    @Nullable
    @Override
    public Long getRequestedAtTimeMillis() {
        return requestedAtTimeMillis;
    }

    public void setRequestedAtTimeMillis(@Nullable Long requestedAtTimeMillis) {
        this.requestedAtTimeMillis = requestedAtTimeMillis;
    }

}
