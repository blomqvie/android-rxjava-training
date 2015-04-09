package fi.reaktor.android.rx.observables;

import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.widget.WidgetObservable;

public class AppObservables {

    public static Observable<String> inputs(TextView textView) {
        return WidgetObservable.text(textView, true)
                .debounce(1, TimeUnit.SECONDS)
                .map(e -> e.text().toString())
                .filter(text -> text.length() > 3);
    }
}
