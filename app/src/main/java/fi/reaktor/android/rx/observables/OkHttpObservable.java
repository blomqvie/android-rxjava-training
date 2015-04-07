package fi.reaktor.android.rx.observables;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.subscriptions.Subscriptions;

public class OkHttpObservable implements Observable.OnSubscribe<Response> {
    private final OkHttpClient client;
    private final Request request;

    public static Observable<Response> createObservable(OkHttpClient client, Request request) {
        return Observable.create(new OkHttpObservable(client, request));
    }

    private OkHttpObservable(OkHttpClient client, Request request) {
        this.client = client;
        this.request = request;
    }

    @Override
    public void call(Subscriber<? super Response> subscriber) {
        final Call call = client.newCall(request);

        subscriber.add(Subscriptions.create(call::cancel));

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                if (!call.isCanceled()) {
                    subscriber.onError(e);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Throwable error = getFailureExceptionOnBadStatus(response);
                if (error != null) {
                    subscriber.onError(error);
                    return;
                }

                subscriber.onNext(response);
                subscriber.onCompleted();
            }
        });
    }

    private static Throwable getFailureExceptionOnBadStatus(Response response) {
        if (response.code() < 399) return null;
        return new FailedResponseException(response);
    }

    private static class FailedResponseException extends Throwable {
        public final Response response;

        public FailedResponseException(Response response) {
            this.response = response;
        }

        @Override
        public String getMessage() {
            try {
                return response.code() + " " + response.body().string();
            } catch (IOException e) {
                return e.getMessage();
            }
        }
    }
}
