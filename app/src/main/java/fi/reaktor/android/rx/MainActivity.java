package fi.reaktor.android.rx;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;

import fi.reaktor.android.rx.jackson.deser.TotallylazyModule;
import fi.reaktor.android.rx.json.ImageSearch;
import fi.reaktor.android.rx.observables.AppObservables;
import rx.Observable;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.widget.WidgetObservable;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends Activity {

    private OkHttpClient okHttpClient = new OkHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();
    private CompositeSubscription compositeSubscription;
    private Subscription alphaSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        objectMapper.registerModule(new TotallylazyModule());
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView searchTerm = (TextView) findViewById(R.id.searchTerm);
        Observable<String> inputs = AppObservables.inputs(searchTerm);
        compositeSubscription = new CompositeSubscription();
        // show progress bar when a new valid input is emitted from inputs
        compositeSubscription.add(AppObservable.bindActivity(this, AppObservables.doWhenSearching(inputs, showProgressBar)).subscribe());

        // TODO: this subscription will die on first error, for example missing network.
        // This should be improved e.g. by subscribing again in errorHandler
        // or using RxJava error handlers: https://github.com/ReactiveX/RxJava/wiki/Error-Handling-Operators
        compositeSubscription.add(AppObservable.bindActivity(this, AppObservables.pictures(inputs, okHttpClient, objectMapper)).subscribe(resultHandler, errorHandler));
    }

    // shows progressbar when new search starts
    private Action1<String> showProgressBar = resp ->
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);

    // takes results of image search and creates adapter for listview based on the result
    private Action1<ImageSearch.ImageSearchResponse> resultHandler = parsed -> {
        final ListView resultList = (ListView) findViewById(R.id.results);

        TextView searchTerm = (TextView) findViewById(R.id.searchTerm);
        resultList.setAlpha(1.0f);

        alphaSubscription = AppObservable.bindActivity(this, WidgetObservable.text(searchTerm, false).take(1)).subscribe(_a -> {
            resultList.setAlpha(0.3f);
        });

        List<String> urlList = parsed.responseData.results.map(i -> i.url).toList();
        resultList.setAdapter(new ImageListAdapter(urlList, this));
        // hide progress bar when search completes
        findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
    };

    // logs errors. Could update UI to notify about error as well.
    private Action1<Throwable> errorHandler = t -> Log.e("MainActivity", "Couldn't load images", t);

    @Override
    protected void onPause() {
        compositeSubscription.unsubscribe();
        if(alphaSubscription != null) {
            alphaSubscription.unsubscribe();
        }
        super.onPause();
    }
}
