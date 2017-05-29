package net.timsmanter.tm.app2;

import android.provider.BaseColumns;

public final class DeviceContract {

    private DeviceContract() {}

    /* Inner class that defines the table contents */
    public static class DeviceEntry implements BaseColumns {
        public static final String TABLE_NAME = "device";
        public static final String COLUMN_NAME_PRODUCER = "producer";
        public static final String COLUMN_NAME_MODEL = "model";
        public static final String COLUMN_NAME_ANDROID_VER = "android_version";
        public static final String COLUMN_NAME_WEBSITE = "website_url";
    }
}