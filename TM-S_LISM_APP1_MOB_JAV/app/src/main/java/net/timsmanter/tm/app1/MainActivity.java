package net.timsmanter.tm.app1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private double average;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        average = data.getDoubleExtra("average", 0.0);
        TextView averageText = (TextView) findViewById(R.id.averageText);
        averageText.setText(String.format("Twoja średnia to: %.2f", average));

        Button endButton = (Button) findViewById(R.id.endButton);
        if (average < 3)
            endButton.setText(R.string.sad_button);
        else
            endButton.setText(R.string.happy_button);
        endButton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void GoToRatesActivity(View view) {

        EditText ratesCount = (EditText) findViewById(R.id.ratesCount);
        Intent intent = new Intent(this, RatesActivity.class);
        if (validateInput()) {
            intent.putExtra("ratesCountString", Integer.parseInt(ratesCount.getText().toString()));
            startActivityForResult(intent, 1);
        }
    }

    public void GoToEnd(View view) {

        if (average < 3)
            Toast.makeText(this, R.string.sad_message, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, R.string.happy_message, Toast.LENGTH_LONG).show();

        finish();
    }

    private boolean validateInput() {
        EditText name = (EditText) findViewById(R.id.nameText);
        EditText surname = (EditText) findViewById(R.id.surnameText);
        EditText ratesCount = (EditText) findViewById(R.id.ratesCount);

        if (name.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.name_empty_toast, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (surname.getText().toString().isEmpty()) {
            Toast.makeText(this, R.string.surname_empty_toast, Toast.LENGTH_SHORT).show();
            return false;
        }

        int ratesCountNumber;
        try {
            ratesCountNumber = Integer.parseInt(ratesCount.getText().toString());
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, R.string.rates_count_toast_empty, Toast.LENGTH_SHORT).show();
            return false;
        }

        if (ratesCountNumber < 5 || ratesCountNumber > 15) {
            Toast.makeText(this, R.string.rates_count_toast_wrong, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
