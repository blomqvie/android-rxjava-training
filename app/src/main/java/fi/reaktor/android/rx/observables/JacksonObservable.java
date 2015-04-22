package fi.reaktor.android.rx.observables;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

import rx.Observable;
import rx.Subscriber;

public class JacksonObservable<T> implements Observable.OnSubscribe<T> {
    private final ObjectMapper objectMapper;
    private final InputStream is;
    private Class<T> klass;

    public static <T> Observable<T> createObservable(ObjectMapper objectMapper, InputStream is, Class<T> klass) {
        return Observable.create(new JacksonObservable<>(objectMapper, is, klass));
    }

    private JacksonObservable(ObjectMapper objectMapper, InputStream is, Class<T> klass) {
        this.objectMapper = objectMapper;
        this.is = is;
        this.klass = klass;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        try {
            subscriber.onNext(objectMapper.<T>readValue(is, klass));
            subscriber.onCompleted();
        } catch (IOException e) {
            subscriber.onError(e);
        }
    }
}
