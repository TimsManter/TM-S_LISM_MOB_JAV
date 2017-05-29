package net.timsmanter.tm.app2;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    String[] columns = {
            DeviceContract.DeviceEntry._ID,
            DeviceContract.DeviceEntry.COLUMN_NAME_PRODUCER,
            DeviceContract.DeviceEntry.COLUMN_NAME_MODEL
    };

    String[] fromColumns = {
            DeviceContract.DeviceEntry.COLUMN_NAME_PRODUCER,
            DeviceContract.DeviceEntry.COLUMN_NAME_MODEL
    };
    int[] toViews = {R.id.producer_name, R.id.model_name};
    ListView listView;
    Toolbar toolbar;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DevicesDbHelper dbHelper = new DevicesDbHelper(this);
        db = dbHelper.getReadableDatabase();
        listView = (ListView) findViewById(R.id.devices_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
                intent.putExtra(DeviceContract.DeviceEntry._ID, Long.valueOf(id));
                startActivityForResult(intent, RESULT_OK);
            }
        });

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                toolbar.setVisibility(View.GONE);
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.menu_context, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_remove_device:
                        long[] selectedItems = listView.getCheckedItemIds();
                        String selection = DeviceContract.DeviceEntry._ID + " = ?";
                        for (int i = 0; i < selectedItems.length; i++) {
                            String[] selectionArgs = { String.valueOf(selectedItems[i]) };
                            db.delete(DeviceContract.DeviceEntry.TABLE_NAME, selection, selectionArgs);
                        }
                        return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                toolbar.setVisibility(View.VISIBLE);
            }
        });

        updateView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_device:
                Intent intent = new Intent(MainActivity.this, DeviceActivity.class);
                intent.putExtra(DeviceContract.DeviceEntry._ID, (long)-1);
                startActivityForResult(intent, 0);
                return true;
            case R.id.action_refresh_devices:
                updateView();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) updateView();
    }

    private void updateView() {
        Cursor cursor = db.query(
                DeviceContract.DeviceEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.device_entry,
                cursor,
                fromColumns,
                toViews,
                0
        );

        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
    }
}
