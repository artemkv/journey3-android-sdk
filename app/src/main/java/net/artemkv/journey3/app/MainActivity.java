package net.artemkv.journey3.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import net.artemkv.journey3.Journey;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button featureAButton;
    private Button featureBButton;
    private Button featureCButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        featureAButton = (Button) findViewById(R.id.feature_a);
        featureBButton = (Button) findViewById(R.id.feature_b);
        featureCButton = (Button) findViewById(R.id.feature_c);

        featureAButton.setOnClickListener(view -> {
            showToast(R.string.feature_a_clicked);

            Journey journey = JourneyConnectorProvider.getInstance();
            if (journey != null) {
                journey.reportEvent("FEATURE_A", this);
            }
        });
        featureBButton.setOnClickListener(view -> {
            showToast(R.string.feature_b_clicked);

            Journey journey = JourneyConnectorProvider.getInstance();
            if (journey != null) {
                journey.reportEvent("FEATURE_B", this);
            }
        });
        featureCButton.setOnClickListener(view -> {
            showToast(R.string.feature_c_clicked);

            Journey journey = JourneyConnectorProvider.getInstance();
            if (journey != null) {
                journey.reportEvent("FEATURE_C", this);
            }
        });

        JourneyConnectorProvider.initialize(this);
    }

    private void showToast(int resId) {
        Toast toast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
        toast.show();
    }
}