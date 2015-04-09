package fi.reaktor.android.rx.observables;

import android.util.Log;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.totallylazy.Option;
import com.squareup.okhttp.OkHttpClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import fi.reaktor.android.rx.json.ImageSearch;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.widget.WidgetObservable;
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

    public static Observable<ImageSearch.ImageSearchResponse> pictures(Observable<String> inputs, OkHttpClient okHttpClient, ObjectMapper objectMapper) {

        /*
            Create an observable which takes items from textFilter observable, creates a full search URL to google image API,
            makes the HTTP request, parses the JSON and emits the parsed result.

            1. transform text from passed observable to full search URL (you can use helper functions below)

            2. Create HTTP request to created URL. This is the tricky part.

              a. Have a look at OkHttpObservable in observables package. It handles HTTP request asynchronously.

              b. Transform passed URL to OkHttpObservable. We are only interested in the latest input and request triggered by it.
                 Other possibly pending requests can be dropped when a new request is triggered.
                 So, we want to transform emitted URLs to OkHttpObservable and use only the emitted item from most recently transformed observable.
                 Tip: there is a function for this in RxJava - have a look at https://github.com/ReactiveX/RxJava/wiki/Transforming-Observables

              c. Now you should have an Observable which emits com.square.okhttp.Response objects

            3. Next, response parsing.

               To parse the JSON from Response object emitted from OkHttpObservable we can use JacksonObservable from observables package.
               We could also do the parsing "by hand" and simply map the Response object with jackson ObjectMapper, but for the sake of training
               we'll use JacksonObservable. So we should take the Response and transform it into JacksonObservable created from InputStream of response body.
               Again, have a look at https://github.com/ReactiveX/RxJava/wiki/Transforming-Observables. There's a function for this too.
               It's not map, because we want to get the items from transformed Observable, not an Observable emitting Observables like map would do.

            4. At this point, you have an observable which emits ImageSearch.ImageSearchResponse objects. Good!

            5. Make sure that this observable is observed on Android main thread - otherwise UI updates are not possible.

         */

        // TODO replace this with your implementation
        return inputs
                .doOnNext(s -> Log.d("AppObservables", "Text input was " + s)) // log search term for debugging
                .map(s -> new ImageSearch.ImageSearchResponse(new ImageSearch.ResponseData(sequence(new ImageSearch.Image("http://fixnation.org/wordpress/wp-content/uploads/2014/03/cats-kittens_00379052.jpg"))))) // return mock response
                .observeOn(AndroidSchedulers.mainThread()); // has to be observed on main thread because UI updates
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
