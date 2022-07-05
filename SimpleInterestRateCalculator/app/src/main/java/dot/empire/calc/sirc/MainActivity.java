package dot.empire.calc.sirc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vj.widgets.AutoResizeTextView;

public final class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtR;
    private EditText txtP;
    private EditText txtT;
    private AutoResizeTextView txtOutput;

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);

        ((Button) findViewById(R.id.btnCalculate)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnClear)).setOnClickListener(this);

        this.txtR = (EditText) findViewById(R.id.txtR);
        this.txtP = (EditText) findViewById(R.id.txtP);
        this.txtT = (EditText) findViewById(R.id.txtT);

        this.txtOutput = (AutoResizeTextView) findViewById(R.id.txtOutput);

        try {
            FirebaseAnalytics analytics = FirebaseAnalytics.getInstance(this);
            analytics.setAnalyticsCollectionEnabled(true);

            MobileAds.initialize(this, getResources().getString(R.string.adUnitId));

            this.adView = (AdView) findViewById(R.id.adView);

            AdRequest.Builder builder = new AdRequest.Builder();
            this.adView.loadAd(builder.build());
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(), Toast.LENGTH_LONG).show();
        }

        Toast.makeText(this, "Welcome!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCalculate:
                this.calculate();
                return;
            case R.id.btnClear:
                this.txtR.setText("");
                this.txtP.setText("");
                this.txtT.setText("");
                this.txtOutput.setText("$0.00");
                return;
        }
    }

    private void calculate() {
        try {
            if (txtR.getText().toString().isEmpty()
                    || txtP.getText().toString().isEmpty()
                    || txtT.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter valid inputs.", Toast.LENGTH_SHORT).show();
                return;
            }

            float r = Float.parseFloat(txtR.getText().toString()) / 100;
            float p = Float.parseFloat(txtP.getText().toString());
            float t = Float.parseFloat(txtT.getText().toString());

            float ans = p * (1 + r * t);
            this.txtOutput.setText(String.format("$%.2f", ans));
        } catch (Exception ex) {
            Toast.makeText(this, ex.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
            this.txtOutput.setText(ex.getMessage());
        }
    }

    /**
     * Called when leaving the activity.
     */
    @Override
    public void onPause() {
        if (adView != null) {
            this.adView.pause();
        }
        super.onPause();
    }

    /**
     * Called when returning to the activity.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            this.adView.resume();
        }
    }

    /**
     * Called before the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        if (adView != null) {
            this.adView.destroy();
        }
        super.onDestroy();
    }
}
