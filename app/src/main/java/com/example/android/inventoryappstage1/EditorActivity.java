package com.example.android.inventoryappstage1;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.inventoryappstage1.data.ItemContract.ItemEntry;
import com.example.android.inventoryappstage1.data.ItemDbHelper;

/**
 * Allows user to create a new item or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity {

    /** EditText field to enter the item's name */
    private EditText mNameEditText;

    /** EditText field to enter the items's price */
    private EditText mPriceEditText;

    /** EditText field to enter the item's quantity */
    private EditText mQuantityEditText;

    /** EditText field to enter the item's supplier name */
    private EditText mSupplierEditText;

    /** EditText field to enter the item's supplier phone */
    private EditText mPhoneEditText;

    /** EditText field to enter the item's description */
    private EditText mDescrEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = findViewById(R.id.edit_item_name);
        mPriceEditText = findViewById(R.id.edit_item_price);
        mQuantityEditText = findViewById(R.id.edit_item_quantity);
        mSupplierEditText = findViewById(R.id.edit_item_supplier);
        mPhoneEditText = findViewById(R.id.edit_item_phone);
        mDescrEditText = findViewById(R.id.edit_item_descr);
    }

    /**
     * Get user input from editor and save new item into database.
     */
    private void insertItem() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String priceString = mPriceEditText.getText().toString().trim();
        String quantityString = mQuantityEditText.getText().toString().trim();
        String supplierString = mSupplierEditText.getText().toString().trim();
        String phoneString = mPhoneEditText.getText().toString().trim();
        String descrString = mDescrEditText.getText().toString().trim();
        int price = Integer.parseInt(priceString);
        int quantity = Integer.parseInt(quantityString);
        int phone = Integer.parseInt(phoneString);

        // Create database helper
        ItemDbHelper mDbHelper = new ItemDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and item attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(ItemEntry.COLUMN_PRODUCT_NAME, nameString);
        values.put(ItemEntry.COLUMN_PRICE, price);
        values.put(ItemEntry.COLUMN_QUANTITY, quantity);
        values.put(ItemEntry.COLUMN_SUPPLIER_NAME, supplierString);
        values.put(ItemEntry.COLUMN_PHONE_NUMBER, phone);
        values.put(ItemEntry.COLUMN_DESCRIPTION, descrString);

        // Insert a new row for item in the database, returning the ID of that new row.
        long newRowId = db.insert(ItemEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving item", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Item saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save item to database
                insertItem();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}