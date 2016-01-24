package razrsword.databaseStorage;

import android.provider.BaseColumns;

/**
 * Created by razrs on 03-Jan-16.
 */
public final class database {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public database() {
    }

    /* Inner class that defines the table contents */
    public static abstract class SettingsData implements BaseColumns {
        public static final String TABLE_NAME = "settings";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";

    }

    /* Inner class that defines the table contents */
    public static abstract class MainActivityData implements BaseColumns {
        public static final String TABLE_NAME = "main";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";

    }

    /* Inner class that defines the table contents */
    public static abstract class DiningActivityData implements BaseColumns {
        public static final String TABLE_NAME = "dining";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";

    }

}
