package app.mbfeedreaders.com.mbfeedreaders.interfacepackage;

/**
 * Created by binayagurung on 10/12/2016 AD.
 */

public interface Copyable<T> {
    T copy();
    T createForCopy();
    void copyTo(T dest);
}