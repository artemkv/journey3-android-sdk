```
<uses-permission android:name="android.permission.INTERNET" />
```

```
public final class JourneyConnectorProvider {
    private static Journey journey;

    private JourneyConnectorProvider() {
    }

    public static Journey getInstance() {
        return journey;
    }

    public static synchronized void initialize(Context context) {
        if (journey == null) {
            journey = new Journey(
                    "<YourAccountID>",
                    "<YourAppID>",
                    BuildConfig.VERSION_NAME,
                    !BuildConfig.DEBUG);
            journey.startSession(context);
        }
    }
}
```

```
public class MainActivity extends AppCompatActivity {
    private Button featureAButton;
    private Button featureBButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        featureAButton = (Button) findViewById(R.id.feature_a);
        featureBButton = (Button) findViewById(R.id.feature_b);

        featureAButton.setOnClickListener(view -> {
            Journey journey = JourneyConnectorProvider.getInstance();
            if (journey != null) {
                journey.reportEvent("FEATURE_A", this);
            }
        });
        featureBButton.setOnClickListener(view -> {
            Journey journey = JourneyConnectorProvider.getInstance();
            if (journey != null) {
                journey.reportEvent("FEATURE_B", this);
            }
        });

        JourneyConnectorProvider.initialize(this);
    }
```