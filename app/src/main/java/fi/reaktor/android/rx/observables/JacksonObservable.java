package fi.reaktor.android.rx.observables;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

import rx.Observable;
import rx.Subscriber;

public class JacksonObservable<T> implements Observable.OnSubscribe<T> {
    private final ObjectMapper objectMapper;
    private final InputStream is;
    private final TypeReference<T> typeOfT;

    public static <T> Observable<T> createObservable(ObjectMapper objectMapper, InputStream is, TypeReference<T> typeOfT) {
        return Observable.create(new JacksonObservable<>(objectMapper, is, typeOfT));
    }

    private JacksonObservable(ObjectMapper objectMapper, InputStream is, TypeReference<T> typeOfT) {
        this.objectMapper = objectMapper;
        this.is = is;
        this.typeOfT = typeOfT;
    }

    @Override
    public void call(Subscriber<? super T> subscriber) {
        try {
            subscriber.onNext(objectMapper.<T>readValue(is, typeOfT));
            subscriber.onCompleted();
        } catch (IOException e) {
            subscriber.onError(e);
        }
    }
}
