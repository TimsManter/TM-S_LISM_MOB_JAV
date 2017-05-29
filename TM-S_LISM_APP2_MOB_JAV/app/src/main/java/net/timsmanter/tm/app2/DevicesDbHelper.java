package net.timsmanter.tm.app2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DevicesDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Devices.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DeviceContract.DeviceEntry.TABLE_NAME + " (" +
                    DeviceContract.DeviceEntry._ID + " INTEGER PRIMARY KEY," +
                    DeviceContract.DeviceEntry.COLUMN_NAME_PRODUCER + " TEXT," +
                    DeviceContract.DeviceEntry.COLUMN_NAME_MODEL + " TEXT," +
                    DeviceContract.DeviceEntry.COLUMN_NAME_ANDROID_VER + " REAL," +
                    DeviceContract.DeviceEntry.COLUMN_NAME_WEBSITE  + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DeviceContract.DeviceEntry.TABLE_NAME;

    public DevicesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
