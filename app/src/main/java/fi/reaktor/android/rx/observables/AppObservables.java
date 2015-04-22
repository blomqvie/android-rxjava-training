package fi.reaktor.android.rx.observables;

import android.util.Log;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.totallylazy.Option;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import fi.reaktor.android.rx.json.ImageSearch;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.widget.WidgetObservable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.googlecode.totallylazy.Sequences.sequence;

public class AppObservables {

    public static final String IMG_SEARCH_BASE_URL = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&rsz=8&imgsz=xlarge&q=";

    public static Observable<String> inputs(TextView textView) {
        return WidgetObservable.text(textView, true)
                .debounce(1, TimeUnit.SECONDS)
                .map(e -> e.text().toString())
                .filter(text -> text.length() > 3);
    }

    public static Observable<String> doWhenSearching(Observable<String> input, Action1<String> action) {
        return input
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(action);
    }

    public static Observable<ImageSearch.ImageSearchResponse> pictures(Observable<String> inputs, OkHttpClient okHttpClient, ObjectMapper objectMapper) {
        return inputs.map(createFullUrl)
                .switchMap(url -> OkHttpObservable.createObservable(okHttpClient, new Request.Builder().url(url).get().build()))
                .flatMap(resp -> JacksonObservable.createObservable(objectMapper, resp.body().byteStream(), ImageSearch.ImageSearchResponse.class))
                .observeOn(AndroidSchedulers.mainThread());
    }

    /*
 * Helper function for creating full search URL
 */
    private static Func1<String, String> createFullUrl = term -> {
        Option<String> param = encodeUrl(term);
        return IMG_SEARCH_BASE_URL + param.getOrElse("kittens");
    };

    private static Option<String> encodeUrl(CharSequence term) {
        try {
            return Option.some(URLEncoder.encode(term.toString(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return Option.none();
        }
    }


}
