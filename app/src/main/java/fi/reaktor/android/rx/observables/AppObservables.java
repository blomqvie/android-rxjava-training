package fi.reaktor.android.rx.observables;

import android.widget.TextView;

import rx.Observable;

public class AppObservables {

    public static Observable<String> inputs(TextView textView) {
        /*
            Create an observable which emits text changes in given TextView.

            1. Have a look at WidgetObservable - it should provide means to catch all changes to a TextView.

            2. But, we don't want all text changes. If user is typing and observable is not throttled,
               subscribers will receive each and every change to the TextView. For image search application,
               it's better to throttle the input and emit item only after certain time interval to avoid too many searches.
               Tip: Take a look at RxJava documentation about backpressure: https://github.com/ReactiveX/RxJava/wiki/Backpressure

            3. The original observable emits OnTextChangeEvents. We are interested in the actual text, so tranform event into text it contains

            4. Emitting too short items would cause vague searches -> filter out too short items (e.g. less than 4 characters)
         */
        // TODO Replace this with your implementation
        return Observable.just("kittens");
    }
}
