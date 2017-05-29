package net.timsmanter.tm.app1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class RatesActivity extends AppCompatActivity {

    private ArrayList<Rate> rates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rates);

        rates = new ArrayList<Rate>();
        fillList(rates, getIntent().getIntExtra("ratesCountString", 0));

        RatesAdapter adapter = new RatesAdapter(this, R.layout.rateview, rates);
        ListView listView = (ListView) findViewById(R.id.ratesList);
        listView.setAdapter(adapter);
    }

    public void BackToHome(View view) {
        double sum = 0.0;
        for (Rate rate : rates) {
            if (rate.getRate() == 0) {
                Toast.makeText(this, R.string.lack_of_rates, Toast.LENGTH_SHORT).show();
                return;
            }
            sum += rate.getRate();
        }

        getIntent().putExtra("average", sum/rates.size());
        setResult(RESULT_OK, getIntent());
        finish();
    }

    private void fillList(ArrayList<Rate> list, int count) {
        for (int i = 1; i <= count; i++) {
            String name = String.format("Ocena %d", i);
            list.add(new Rate(name));
        }
    }
}
