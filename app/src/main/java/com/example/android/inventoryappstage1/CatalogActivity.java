package com.example.android.inventoryappstage1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.inventoryappstage1.data.ItemContract.ItemEntry;
import com.example.android.inventoryappstage1.data.ItemDbHelper;

// Displays list of items that were entered and stored in the app.
public class CatalogActivity extends AppCompatActivity {

    // Database helper that will provide us access to the database.
    private ItemDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity.
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new ItemDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    // Temporary helper method to display information in the onscreen TextViw about
    // the state of the items database.
    private void displayDatabaseInfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_PRODUCT_NAME,
                ItemEntry.COLUMN_PRICE,
                ItemEntry.COLUMN_QUANTITY,
                ItemEntry.COLUMN_SUPPLIER_NAME,
                ItemEntry.COLUMN_PHONE_NUMBER,
                ItemEntry.COLUMN_DESCRIPTION};

        // Perform a query on the items table.
        Cursor cursor = db.query(
                ItemEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        TextView displayView = findViewById(R.id.text_view_item);

        try {
            displayView.setText("The items table contains " + cursor.getCount() + " items.\n\n");
            displayView.append(ItemEntry._ID + " - " +
                ItemEntry.COLUMN_PRODUCT_NAME + " - " +
                ItemEntry.COLUMN_PRICE + " - " +
                ItemEntry.COLUMN_QUANTITY + " - " +
                ItemEntry.COLUMN_SUPPLIER_NAME + " - " +
                ItemEntry.COLUMN_PHONE_NUMBER + " - " +
                ItemEntry.COLUMN_DESCRIPTION + "\n");

            // Figure out the index of each column.
            int idColumnIndex = cursor.getColumnIndex(ItemEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_SUPPLIER_NAME);
            int phoneColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_PHONE_NUMBER);
            int descrColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_DESCRIPTION);

            // Iterate through all the returned rows in the cursor.
            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplier = cursor.getString(supplierColumnIndex);
                int currentPhone = cursor.getInt(phoneColumnIndex);
                String currentDescr = cursor.getString(descrColumnIndex);
                displayView.append(("\n" + currentID + " - " +
                    currentName + " - " +
                    currentPrice + " - " +
                    currentQuantity + " - " +
                    currentSupplier + " - " +
                    currentPhone + " - " +
                    currentDescr));
            }
        } finally {
            cursor.close();
        }
    }

    // Helper method to insert hardcoded item data into the database. For debugging purposes only.
    private void insertItem() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_PRODUCT_NAME, "CubeSat");
        values.put(ItemEntry.COLUMN_PRICE, 1000);
        values.put(ItemEntry.COLUMN_QUANTITY, 2);
        values.put(ItemEntry.COLUMN_SUPPLIER_NAME, "SOSA");
        values.put(ItemEntry.COLUMN_PHONE_NUMBER, 102345);
        values.put(ItemEntry.COLUMN_DESCRIPTION, "The best CubeSat");

        long newRowId = db.insert(ItemEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_fake_data:
                insertItem();
                displayDatabaseInfo();
                return true;
            case R.id.action_delete_everything:
                // Do nothing.
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}




























