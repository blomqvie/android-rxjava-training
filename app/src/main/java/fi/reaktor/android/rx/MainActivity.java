package fi.reaktor.android.rx;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import fi.reaktor.android.rx.observables.AppObservables;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends Activity {


    private Subscription inputsSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView searchTerm = (TextView) findViewById(R.id.searchTerm);
        inputsSubscription = AppObservables.inputs(searchTerm)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(string -> {
                    // show text from input field in another text view.
                    TextView text = (TextView) findViewById(R.id.enteredText);
                    text.setText(string);
                });
    }

    @Override
    protected void onPause() {
        inputsSubscription.unsubscribe();
        super.onPause();
    }
}
