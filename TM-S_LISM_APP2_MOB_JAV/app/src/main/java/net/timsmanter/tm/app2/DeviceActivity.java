package net.timsmanter.tm.app2;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DeviceActivity extends AppCompatActivity {
    EditText producerEdit;
    EditText modelEdit;
    EditText versionEdit;
    EditText websiteEdit;

    Button wwwButton;
    Button cancelButton;
    Button saveButton;

    String[] columns = {
            DeviceContract.DeviceEntry._ID,
            DeviceContract.DeviceEntry.COLUMN_NAME_PRODUCER,
            DeviceContract.DeviceEntry.COLUMN_NAME_MODEL,
            DeviceContract.DeviceEntry.COLUMN_NAME_ANDROID_VER,
            DeviceContract.DeviceEntry.COLUMN_NAME_WEBSITE
    };

    SQLiteDatabase db;
    long deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        deviceId = getIntent().getExtras().getLong(DeviceContract.DeviceEntry._ID);

        DevicesDbHelper dbHelper = new DevicesDbHelper(this);
        db = dbHelper.getReadableDatabase();

        producerEdit = (EditText) findViewById(R.id.producer_edit);
        modelEdit = (EditText) findViewById(R.id.model_edit);
        versionEdit = (EditText) findViewById(R.id.version_edit);
        websiteEdit = (EditText) findViewById(R.id.website_edit);

        wwwButton = (Button) findViewById(R.id.www_button);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        saveButton = (Button) findViewById(R.id.save_button);

        if (deviceId >= 0) fillView();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.close();
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        wwwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = websiteEdit.getText().toString();
                if (!url.isEmpty()) {
                    Intent browser = new Intent("android.intent.action.VIEW", Uri.parse(url.toString()));
                    startActivity(browser);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!areValuesCorrect()) return;
                ContentValues values = new ContentValues();
                values.put(DeviceContract.DeviceEntry.COLUMN_NAME_PRODUCER, producerEdit.getText().toString());
                values.put(DeviceContract.DeviceEntry.COLUMN_NAME_MODEL, modelEdit.getText().toString());
                values.put(DeviceContract.DeviceEntry.COLUMN_NAME_ANDROID_VER, Float.parseFloat(versionEdit.getText().toString()));
                values.put(DeviceContract.DeviceEntry.COLUMN_NAME_WEBSITE, websiteEdit.getText().toString());
                if (deviceId < 0)
                    db.insert(DeviceContract.DeviceEntry.TABLE_NAME, null, values);
                else {
                    String selection = DeviceContract.DeviceEntry._ID + " = ?";
                    String[] selectionArgs = { String.valueOf(deviceId) };

                    db.update(
                            DeviceContract.DeviceEntry.TABLE_NAME,
                            values,
                            selection,
                            selectionArgs
                    );
                }
                db.close();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private boolean areValuesCorrect() {
        if (producerEdit.getText().toString().isEmpty()) {
            Toast.makeText(this, "Brak producenta", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (modelEdit.getText().toString().isEmpty()) {
            Toast.makeText(this, "Brak modelu", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (versionEdit.getText().toString().isEmpty()) {
            Toast.makeText(this, "Brak wersji androida", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (websiteEdit.getText().toString().isEmpty()) {
            Toast.makeText(this, "Brak strony internetowej", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void fillView() {
        String selection = DeviceContract.DeviceEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(deviceId) };

        Cursor cursor = db.query(
                DeviceContract.DeviceEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndexOrThrow(DeviceContract.DeviceEntry.COLUMN_NAME_PRODUCER);
        producerEdit.setText(cursor.getString(columnIndex));

        columnIndex = cursor.getColumnIndexOrThrow(DeviceContract.DeviceEntry.COLUMN_NAME_MODEL);
        modelEdit.setText(cursor.getString(columnIndex));

        columnIndex = cursor.getColumnIndexOrThrow(DeviceContract.DeviceEntry.COLUMN_NAME_ANDROID_VER);
        versionEdit.setText(cursor.getString(columnIndex));

        columnIndex = cursor.getColumnIndexOrThrow(DeviceContract.DeviceEntry.COLUMN_NAME_WEBSITE);
        websiteEdit.setText(cursor.getString(columnIndex));
    }
}
