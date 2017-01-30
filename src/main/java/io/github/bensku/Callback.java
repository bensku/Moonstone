package io.github.bensku;

public interface Callback<T> {
    
    /**
     * Called when the task is completed.
     * @param result Result for the task.
     */
    void onComplete(T result);
    
    /**
     * Called when there was an exception.
     * @param e Exception.
     */
    void onException(Exception e);
}
