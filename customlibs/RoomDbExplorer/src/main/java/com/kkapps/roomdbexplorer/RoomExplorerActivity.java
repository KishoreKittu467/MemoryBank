package com.kkapps.roomdbexplorer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RoomExplorerActivity extends Activity implements OnItemClickListener {

    public static final String DATABASE_CLASS_KEY = "dbClassName";
    public static final String DATABASE_NAME_KEY = "dbName";
    protected Class<? extends RoomDatabase> myClass;
    protected String databaseName;
    TableLayout tableLayout;
    TableRow.LayoutParams tableRowParams;
    HorizontalScrollView hsv;
    ScrollView mainscrollview;
    LinearLayout mainLayout;
    TextView tvmessage;
    Button previous;
    Button next;
    Spinner selectTable;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().hasExtra(DATABASE_CLASS_KEY)) {
            myClass = (Class<? extends RoomDatabase>) getIntent().getExtras().get(DATABASE_CLASS_KEY);
            databaseName = (String) getIntent().getExtras().get(DATABASE_NAME_KEY);
        } else {
            throw new RuntimeException("No database class name passed in the launch Intent.");
        }

        mainscrollview = new ScrollView(RoomExplorerActivity.this);

        //the main linear layout to which all tables spinners etc will be added.In this activity every element is created dynamically  to avoid using xml file
        mainLayout = new LinearLayout(RoomExplorerActivity.this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setBackgroundColor(Color.WHITE);
        mainLayout.setScrollContainer(true);
        mainscrollview.addView(mainLayout);

        //all required layouts are created dynamically and added to the main scrollview
        setContentView(mainscrollview);

        //the first row of layout which has a text view and spinner
        final LinearLayout firstrow = new LinearLayout(RoomExplorerActivity.this);
//        firstrow.setPadding(0, 10, 0, 20);
        LinearLayout.LayoutParams firstrowlp = new LinearLayout.LayoutParams(0, 150);
        firstrowlp.weight = 1;

        TextView maintext = new TextView(RoomExplorerActivity.this);
        maintext.setText("Select Table");
        maintext.setTextSize(22);
        maintext.setLayoutParams(firstrowlp);
        selectTable = new Spinner(RoomExplorerActivity.this);
        selectTable.setLayoutParams(firstrowlp);

        firstrow.addView(maintext);
        firstrow.addView(selectTable);
        mainLayout.addView(firstrow);

        List<Cursor> alc;

        //the horizontal scroll view for table if the table content doesnot fit into screen
        hsv = new HorizontalScrollView(RoomExplorerActivity.this);

        //the main table layout where the content of the sql tables will be displayed when user selects a table
        tableLayout = new TableLayout(RoomExplorerActivity.this);
        tableLayout.setHorizontalScrollBarEnabled(true);
        hsv.addView(tableLayout);

        //the second row of the layout which shows number of records in the table selected by user
        final LinearLayout secondrow = new LinearLayout(this);
//        secondrow.setPadding(0, 20, 0, 10);
        LinearLayout.LayoutParams secondrowlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        secondrowlp.weight = 1;
        TextView secondrowtext = new TextView(RoomExplorerActivity.this);
        secondrowtext.setText("No. Of Records : ");
        secondrowtext.setTextSize(20);
        secondrowtext.setLayoutParams(secondrowlp);
        tv = new TextView(RoomExplorerActivity.this);
        tv.setTextSize(20);
        tv.setLayoutParams(secondrowlp);
        secondrow.addView(secondrowtext);
        secondrow.addView(tv);
        mainLayout.addView(secondrow);
        //A button which generates a text view from which user can write custom queries
        final EditText customQueryEditText = new EditText(this);
        customQueryEditText.setVisibility(View.GONE);
        customQueryEditText.setHint("Enter Your query here and Click on Submit query Button .Results will be displayed below");
        mainLayout.addView(customQueryEditText);

        final Button submitQuery = new Button(this);
        submitQuery.setVisibility(View.GONE);
        submitQuery.setText("Submit query");

        submitQuery.setBackgroundColor(Color.parseColor("#BAE7F6"));
        mainLayout.addView(submitQuery);

        final TextView help = new TextView(this);
        help.setText("Click on the row below to update values or delete the tuple");
//        help.setPadding(0, 5, 0, 5);

        // the spinner which gives user a option to add new row , drop or delete table
        final Spinner spinnerTable = new Spinner(this);
        mainLayout.addView(spinnerTable);
        mainLayout.addView(help);
//        hsv.setPadding(0, 10, 0, 10);
        hsv.setScrollbarFadingEnabled(false);
        hsv.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
        mainLayout.addView(hsv);
        //the third layout which has buttons for the pagination of content from database
        final LinearLayout thirdRow = new LinearLayout(this);
        previous = new Button(this);
        previous.setText("Previous");

        previous.setBackgroundColor(Color.parseColor("#BAE7F6"));
        previous.setLayoutParams(secondrowlp);
        next = new Button(this);
        next.setText("Next");
        next.setBackgroundColor(Color.parseColor("#BAE7F6"));
        next.setLayoutParams(secondrowlp);
        TextView tvblank = new TextView(this);
        tvblank.setLayoutParams(secondrowlp);
//        thirdRow.setPadding(0, 10, 0, 10);
        thirdRow.addView(previous);
        thirdRow.addView(tvblank);
        thirdRow.addView(next);
        mainLayout.addView(thirdRow);

        //the text view at the bottom of the screen which displays error or success messages after a query is executed
        tvmessage = new TextView(RoomExplorerActivity.this);

        tvmessage.setText("Error Messages will be displayed here");
        String query = "SELECT name _id FROM sqlite_master WHERE type ='table'";
        tvmessage.setTextSize(18);
        mainLayout.addView(tvmessage);

        final Button customQuery = new Button(RoomExplorerActivity.this);
        customQuery.setText("Custom query");
        customQuery.setBackgroundColor(Color.parseColor("#BAE7F6"));
        mainLayout.addView(customQuery);
        customQuery.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //set drop down to custom query
                IndexInfo.isCustomQuery = true;
                secondrow.setVisibility(View.GONE);
                spinnerTable.setVisibility(View.GONE);
                help.setVisibility(View.GONE);
                customQueryEditText.setVisibility(View.VISIBLE);
                submitQuery.setVisibility(View.VISIBLE);
                selectTable.setSelection(0);
                customQuery.setVisibility(View.GONE);
            }
        });


        //when user enter a custom query in text view and clicks on submit query button
        //display results in tablelayout
        submitQuery.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                tableLayout.removeAllViews();
                customQuery.setVisibility(View.GONE);

                String Query10 = customQueryEditText.getText().toString();
                //pass the query to getdata method and get results
                List<Cursor> alc2 = getData(Query10);
                final Cursor c4 = alc2.get(0);
                Cursor Message2 = alc2.get(1);
                Message2.moveToLast();

                //if the query returns results display the results in table layout
                if (Message2.getString(0).equalsIgnoreCase("Success")) {

                    tvmessage.setBackgroundColor(Color.parseColor("#2ecc71"));
                    if (c4 != null) {
                        tvmessage.setText("query Executed successfully.Number of rows returned :" + c4.getCount());
                        if (c4.getCount() > 0) {
                            IndexInfo.mainCursor = c4;
                            refreshTable(1);
                        }
                    } else {
                        tvmessage.setText("query Executed successfully");
                        refreshTable(1);
                    }

                } else {
                    //if there is any error we displayed the error message at the bottom of the screen
                    tvmessage.setBackgroundColor(Color.parseColor("#e74c3c"));
                    tvmessage.setText("Error:" + Message2.getString(0));

                }
            }
        });
        //layout parameters for each row in the table
        tableRowParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tableRowParams.setMargins(0, 0, 2, 0);

        // a query which returns a cursor with the list of tables in the database.We use this cursor to populate spinner in the first row
        alc = getData(query);

        //the first cursor has reults of the query
        final Cursor c = alc.get(0);

        //the second cursor has error messages
        Cursor Message = alc.get(1);

        Message.moveToLast();
        String msg = Message.getString(0);

        List<String> tableNames = new ArrayList<>();

        if (c != null) {

            c.moveToFirst();
            tableNames.add("click here");
            do {
                //add names of the table to tableNames array list
                tableNames.add(c.getString(0));
            } while (c.moveToNext());
        }
        //an array adapter with above created array list
        ArrayAdapter<String> tableNamesAdapter = new ArrayAdapter<String>(RoomExplorerActivity.this,
                android.R.layout.simple_spinner_item, tableNames) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                v.setBackgroundColor(Color.WHITE);
                TextView adap = (TextView) v;
                adap.setTextSize(20);
                return adap;
            }


            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);

                v.setBackgroundColor(Color.WHITE);

                return v;
            }
        };

        tableNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set the adapter to select_table spinner
        selectTable.setAdapter(tableNamesAdapter);

        // when a table names is selected display the table contents
        selectTable.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                if (pos == 0 && !IndexInfo.isCustomQuery) {
                    secondrow.setVisibility(View.GONE);
                    hsv.setVisibility(View.GONE);
                    thirdRow.setVisibility(View.GONE);
                    spinnerTable.setVisibility(View.GONE);
                    help.setVisibility(View.GONE);
                    tvmessage.setVisibility(View.GONE);
                    customQueryEditText.setVisibility(View.GONE);
                    submitQuery.setVisibility(View.GONE);
                    customQuery.setVisibility(View.GONE);
                }
                if (pos != 0) {
                    secondrow.setVisibility(View.VISIBLE);
                    spinnerTable.setVisibility(View.VISIBLE);
                    help.setVisibility(View.VISIBLE);
                    customQueryEditText.setVisibility(View.GONE);
                    submitQuery.setVisibility(View.GONE);
                    customQuery.setVisibility(View.VISIBLE);
                    hsv.setVisibility(View.VISIBLE);

                    tvmessage.setVisibility(View.VISIBLE);

                    thirdRow.setVisibility(View.VISIBLE);
                    c.moveToPosition(pos - 1);
                    IndexInfo.cursorPosition = pos - 1;
                    //displaying the content of the table which is selected in the select_table spinner
                    IndexInfo.tableName = c.getString(0);
                    tvmessage.setText("Error Messages will be displayed here");
                    tvmessage.setBackgroundColor(Color.WHITE);

                    //removes any data if present in the table layout
                    tableLayout.removeAllViews();
                    List<String> spinnerTableValues = new ArrayList<>();
                    spinnerTableValues.add("Click here to change this table");
                    spinnerTableValues.add("Add row to this table");
                    spinnerTableValues.add("Delete this table");
                    spinnerTableValues.add("Drop this table");
                    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(RoomExplorerActivity.this, android.R.layout.simple_spinner_dropdown_item, spinnerTableValues);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

                    // a array adapter which add values to the spinner which helps in user making changes to the table
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RoomExplorerActivity.this,
                            android.R.layout.simple_spinner_item, spinnerTableValues) {

                        public View getView(int position, View convertView, ViewGroup parent) {
                            View v = super.getView(position, convertView, parent);

                            v.setBackgroundColor(Color.WHITE);
                            TextView adap = (TextView) v;
                            adap.setTextSize(20);

                            return adap;
                        }

                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            View v = super.getDropDownView(position, convertView, parent);

                            v.setBackgroundColor(Color.WHITE);

                            return v;
                        }
                    };

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTable.setAdapter(adapter);
                    String Query2 = "select * from " + c.getString(0);

                    //getting contents of the table which user selected from the select_table spinner
                    List<Cursor> alc2 = getData(Query2);
                    final Cursor c2 = alc2.get(0);
                    //saving cursor to the static IndexInfo class which can be resued by the other functions
                    IndexInfo.mainCursor = c2;

                    // if the cursor returned form the database is not null we display the data in table layout
                    if (c2 != null) {
                        int counts = c2.getCount();
                        IndexInfo.isEmpty = false;
                        tv.setText("" + counts);


                        //the spinnerTable has the 3 items to drop , delete , add row to the table selected by the user
                        //here we handle the 3 operations.
                        spinnerTable.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
                            @SuppressLint("ResourceType")
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                                ((TextView) parentView.getChildAt(0)).setTextColor(Color.rgb(0, 0, 0));
                                //when user selects to drop the table the below code in if block will be executed
                                if (spinnerTable.getSelectedItem().toString().equals("Drop this table")) {
                                    // an alert dialog to confirm user selection
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!isFinishing()) {

                                                new AlertDialog.Builder(RoomExplorerActivity.this)
                                                        .setTitle("Are you sure ?")
                                                        .setMessage("Pressing yes will remove " + IndexInfo.tableName + " table from database")
                                                        .setPositiveButton("yes",
                                                                new DialogInterface.OnClickListener() {
                                                                    // when user confirms by clicking on yes we drop the table by executing drop table query
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                        String Query6 = "Drop table " + IndexInfo.tableName;
                                                                        List<Cursor> aldropt = getData(Query6);
                                                                        Cursor tempc = aldropt.get(1);
                                                                        tempc.moveToLast();
                                                                        if (tempc.getString(0).equalsIgnoreCase("Success")) {
                                                                            tvmessage.setBackgroundColor(Color.parseColor("#2ecc71"));
                                                                            tvmessage.setText(IndexInfo.tableName + "Dropped successfully");
                                                                            refreshActivity();
                                                                        } else {
                                                                            //if there is any error we displayd the error message at the bottom of the screen
                                                                            tvmessage.setBackgroundColor(Color.parseColor("#e74c3c"));
                                                                            tvmessage.setText("Error:" + tempc.getString(0));
                                                                            spinnerTable.setSelection(0);
                                                                        }
                                                                    }
                                                                })
                                                        .setNegativeButton("No",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        spinnerTable.setSelection(0);
                                                                    }
                                                                })
                                                        .create().show();
                                            }
                                        }
                                    });

                                }
                                //when user selects to drop the table the below code in if block will be executed
                                if (spinnerTable.getSelectedItem().toString().equals("Delete this table")) {    // an alert dialog to confirm user selection
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!isFinishing()) {

                                                new AlertDialog.Builder(RoomExplorerActivity.this)
                                                        .setTitle("Are you sure?")
                                                        .setMessage("Clicking on yes will delete all the contents of " + IndexInfo.tableName + " table from database")
                                                        .setPositiveButton("yes",
                                                                new DialogInterface.OnClickListener() {

                                                                    // when user confirms by clicking on yes we drop the table by executing delete table query
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        String query7 = "Delete  from " + IndexInfo.tableName;
                                                                        List<Cursor> aldeletet = getData(query7);
                                                                        Cursor tempc = aldeletet.get(1);
                                                                        tempc.moveToLast();
                                                                        if (tempc.getString(0).equalsIgnoreCase("Success")) {
                                                                            tvmessage.setBackgroundColor(Color.parseColor("#2ecc71"));
                                                                            tvmessage.setText(IndexInfo.tableName + " table content deleted successfully");
                                                                            IndexInfo.isEmpty = true;
                                                                            refreshTable(0);
                                                                        } else {
                                                                            tvmessage.setBackgroundColor(Color.parseColor("#e74c3c"));
                                                                            tvmessage.setText("Error:" + tempc.getString(0));
                                                                            spinnerTable.setSelection(0);
                                                                        }
                                                                    }
                                                                })
                                                        .setNegativeButton("No",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        spinnerTable.setSelection(0);
                                                                    }
                                                                })
                                                        .create().show();
                                            }
                                        }
                                    });

                                }

                                //when user selects to add row to the table the below code in if block will be executed
                                if (spinnerTable.getSelectedItem().toString().equals("Add row to this table")) {
                                    //we create a layout which has textviews with column names of the table and edittexts where
                                    //user can enter value which will be inserted into the datbase.
                                    final List<TextView> addNewRowNames = new LinkedList<>();
                                    final List<EditText> addNewRowValues = new LinkedList<>();
                                    final ScrollView scrollView = new ScrollView(RoomExplorerActivity.this);
                                    Cursor c4 = IndexInfo.mainCursor;
                                    if (IndexInfo.isEmpty) {
                                        getColumnNames();
                                        for (int i = 0; i < IndexInfo.emptyTableColumnNames.size(); i++) {
                                            String cname = IndexInfo.emptyTableColumnNames.get(i);
                                            TextView tv = new TextView(RoomExplorerActivity.this);
                                            tv.setText(cname);
                                            addNewRowNames.add(tv);

                                        }
                                        for (int i = 0; i < addNewRowNames.size(); i++) {
                                            EditText et = new EditText(RoomExplorerActivity.this);
                                            et.setPadding(0,0,0,0);
                                            addNewRowValues.add(et);
                                        }

                                    } else {
                                        for (int i = 0; i < c4.getColumnCount(); i++) {
                                            String cname = c4.getColumnName(i);
                                            TextView tv = new TextView(RoomExplorerActivity.this);
                                            tv.setText(cname);
                                            addNewRowNames.add(tv);

                                        }
                                        for (int i = 0; i < addNewRowNames.size(); i++) {
                                            EditText et = new EditText(RoomExplorerActivity.this);
                                            et.setPadding(0,0,0,0);
                                            addNewRowValues.add(et);
                                        }
                                    }
                                    final RelativeLayout addNewLayout = new RelativeLayout(RoomExplorerActivity.this);
                                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                    for (int i = 0; i < addNewRowNames.size(); i++) {
                                        TextView tv = addNewRowNames.get(i);
                                        EditText et = addNewRowValues.get(i);
                                        int t = i + 400;
                                        int k = i + 500;
                                        int lid = i + 600;

                                        tv.setId(t);
                                        tv.setTextColor(Color.parseColor("#000000"));
                                        et.setPadding(0,0,0,0);
                                        et.setBackgroundColor(Color.parseColor("#F2F2F2"));
                                        et.setTextColor(Color.parseColor("#000000"));
                                        et.setId(k);
                                        final LinearLayout ll = new LinearLayout(RoomExplorerActivity.this);
                                        LinearLayout.LayoutParams tvl = new LinearLayout.LayoutParams(0, 100);
                                        tvl.weight = 1;
                                        ll.addView(tv, tvl);
                                        ll.addView(et, tvl);
                                        ll.setId(lid);

                                        RelativeLayout.LayoutParams rll = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                        rll.addRule(RelativeLayout.BELOW, ll.getId() - 1);
                                        rll.setMargins(0, 20, 0, 0);
                                        addNewLayout.addView(ll, rll);

                                    }
                                    addNewLayout.setBackgroundColor(Color.WHITE);
                                    scrollView.addView(addNewLayout);
                                    //the above form layout which we have created above will be displayed in an alert dialog
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!isFinishing()) {
                                                new AlertDialog.Builder(RoomExplorerActivity.this)
                                                        .setTitle("values")
                                                        .setCancelable(false)
                                                        .setView(scrollView)
                                                        .setPositiveButton("Add",
                                                                new DialogInterface.OnClickListener() {
                                                                    // after entering values if user clicks on add we take the values and run a insert query
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                        IndexInfo.index = 10;
                                                                        //tableLayout.removeAllViews();
                                                                        //trigger select table listener to be triggerd
                                                                        String query4 = "Insert into " + IndexInfo.tableName + " (";
                                                                        for (int i = 0; i < addNewRowNames.size(); i++) {

                                                                            TextView tv = addNewRowNames.get(i);
                                                                            tv.getText().toString();
                                                                            if (i == addNewRowNames.size() - 1) {

                                                                                query4 = query4 + tv.getText().toString();

                                                                            } else {
                                                                                query4 = query4 + tv.getText().toString() + ", ";
                                                                            }
                                                                        }
                                                                        query4 = query4 + " ) VALUES ( ";
                                                                        for (int i = 0; i < addNewRowNames.size(); i++) {
                                                                            EditText et = addNewRowValues.get(i);
                                                                            et.getText().toString();

                                                                            if (i == addNewRowNames.size() - 1) {

                                                                                query4 = query4 + "'" + et.getText().toString() + "' ) ";
                                                                            } else {
                                                                                query4 = query4 + "'" + et.getText().toString() + "' , ";
                                                                            }


                                                                        }
                                                                        //this is the insert query which has been generated
                                                                        List<Cursor> altc = getData(query4);
                                                                        Cursor tempc = altc.get(1);
                                                                        tempc.moveToLast();
                                                                        if (tempc.getString(0).equalsIgnoreCase("Success")) {
                                                                            tvmessage.setBackgroundColor(Color.parseColor("#2ecc71"));
                                                                            tvmessage.setText("New Row added succesfully to " + IndexInfo.tableName);
                                                                            refreshTable(0);
                                                                        } else {
                                                                            tvmessage.setBackgroundColor(Color.parseColor("#e74c3c"));
                                                                            tvmessage.setText("Error:" + tempc.getString(0));
                                                                            spinnerTable.setSelection(0);
                                                                        }

                                                                    }
                                                                })
                                                        .setNegativeButton("close",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        spinnerTable.setSelection(0);
                                                                    }
                                                                })
                                                        .create().show();
                                            }
                                        }
                                    });
                                }
                            }

                            public void onNothingSelected(AdapterView<?> arg0) {
                            }
                        }));

                        //display the first row of the table with column names of the table selected by the user
                        TableRow tableHeader = new TableRow(RoomExplorerActivity.this);

                        tableHeader.setBackgroundColor(Color.BLACK);
//                        tableHeader.setPadding(0, 2, 0, 2);
                        for (int k = 0; k < c2.getColumnCount(); k++) {
                            LinearLayout cell = new LinearLayout(RoomExplorerActivity.this);
                            cell.setBackgroundColor(Color.WHITE);
                            cell.setLayoutParams(tableRowParams);
                            final TextView tableHeaderColumns = new TextView(RoomExplorerActivity.this);
                            // tableHeaderColumns.setBackgroundDrawable(gd);
//                            tableHeaderColumns.setPadding(0, 0, 4, 3);
                            tableHeaderColumns.setText("" + c2.getColumnName(k));
                            tableHeaderColumns.setTextColor(Color.parseColor("#000000"));

                            //columnsView.setLayoutParams(tableRowParams);
                            cell.addView(tableHeaderColumns);
                            tableHeader.addView(cell);

                        }
                        tableLayout.addView(tableHeader);
                        c2.moveToFirst();

                        //after displaying columnNames in the first row  we display data in the remaining columns
                        //the below paginateTable function will display the first 10 tuples of the tables
                        //the remaining tuples can be viewed by clicking on the next button
                        paginateTable(c2.getCount());

                    } else {
                        //if the cursor returned from the database is empty we show that table is empty
                        help.setVisibility(View.GONE);
                        tableLayout.removeAllViews();
                        getColumnNames();
                        TableRow tableHeader2 = new TableRow(RoomExplorerActivity.this);
                        tableHeader2.setBackgroundColor(Color.BLACK);
//                        tableHeader2.setPadding(0, 2, 0, 2);

                        LinearLayout cell = new LinearLayout(RoomExplorerActivity.this);
                        cell.setBackgroundColor(Color.WHITE);
                        cell.setLayoutParams(tableRowParams);
                        final TextView tableHeaderColumns = new TextView(RoomExplorerActivity.this);

//                        tableHeaderColumns.setPadding(0, 0, 4, 3);
                        tableHeaderColumns.setText("   Table   Is   Empty   ");
                        tableHeaderColumns.setTextSize(30);
                        tableHeaderColumns.setTextColor(Color.RED);

                        cell.addView(tableHeaderColumns);
                        tableHeader2.addView(cell);


                        tableLayout.addView(tableHeader2);

                        tv.setText("0");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    //get columnNames of the empty tables and save them in a array list
    public void getColumnNames() {
        List<Cursor> alc3 = getData("PRAGMA table_info(" + IndexInfo.tableName + ")");
        Cursor c5 = alc3.get(0);
        IndexInfo.isEmpty = true;
        if (c5 != null) {
            List<String> emptyTableColumnNames = new ArrayList<>();
            c5.moveToFirst();
            do {
                emptyTableColumnNames.add(c5.getString(1));
            } while (c5.moveToNext());
            IndexInfo.emptyTableColumnNames = emptyTableColumnNames;
        }


    }

    //displays alert dialog from which use can update or delete a row
    @SuppressLint("ResourceType")
    public void updateDeletePopup(int row) {
        Cursor c2 = IndexInfo.mainCursor;
        // a spinner which gives options to update or delete the row which user has selected
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Click Here to Change this row");
        spinnerArray.add("Update this row");
        spinnerArray.add("Delete this row");

        //create a layout with text values which has the column names and
        //edit texts which has the values of the row which user has selected
        final List<String> valueStrings = IndexInfo.valueString;
        final List<TextView> columnNames = new LinkedList<>();
        final List<EditText> columnValues = new LinkedList<>();

        for (int i = 0; i < c2.getColumnCount(); i++) {
            String cname = c2.getColumnName(i);
            TextView tv = new TextView(RoomExplorerActivity.this);
            tv.setText(cname);
            columnNames.add(tv);

        }
        for (int i = 0; i < columnNames.size(); i++) {
            String cv = valueStrings.get(i);
            EditText et = new EditText(RoomExplorerActivity.this);
            valueStrings.add(cv);
            et.setText(cv);
            et.setPadding(0,0,0,0);
            columnValues.add(et);
        }

        int lastrid = 0;
        // all text views , edit texts are added to this relative layout lp
        final RelativeLayout lp = new RelativeLayout(RoomExplorerActivity.this);
        lp.setBackgroundColor(Color.WHITE);
        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lay.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        final ScrollView updaterowsv = new ScrollView(RoomExplorerActivity.this);
        LinearLayout lcrud = new LinearLayout(RoomExplorerActivity.this);

        LinearLayout.LayoutParams paramcrudtext = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        paramcrudtext.setMargins(0, 20, 0, 0);

        //spinner which displays update , delete options
        final Spinner crud_dropdown = new Spinner(RoomExplorerActivity.this);

        ArrayAdapter<String> crudAdapter = new ArrayAdapter<String>(RoomExplorerActivity.this,
                android.R.layout.simple_spinner_item, spinnerArray) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                v.setBackgroundColor(Color.WHITE);
                TextView adap = (TextView) v;
                adap.setTextSize(20);
                adap.setPadding(0,0,0,0);
                return adap;
            }


            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);

                v.setBackgroundColor(Color.WHITE);

                return v;
            }
        };


        crudAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        crud_dropdown.setAdapter(crudAdapter);
        lcrud.setId(299);
        lcrud.addView(crud_dropdown, paramcrudtext);

        RelativeLayout.LayoutParams rlcrudparam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        rlcrudparam.addRule(RelativeLayout.BELOW, lastrid);

        lp.addView(lcrud, rlcrudparam);
        for (int i = 0; i < columnNames.size(); i++) {
            TextView tv = columnNames.get(i);
            EditText et = columnValues.get(i);
            int t = i + 100;
            int k = i + 200;
            int lid = i + 300;

            tv.setId(t);
            tv.setPadding(0,0,0,0);
            tv.setTextColor(Color.parseColor("#000000"));
            et.setBackgroundColor(Color.parseColor("#F2F2F2"));
            et.setPadding(0,0,0,0);
            et.setTextColor(Color.parseColor("#000000"));
            et.setId(k);
            final LinearLayout ll = new LinearLayout(RoomExplorerActivity.this);
            ll.setBackgroundColor(Color.parseColor("#FFFFFF"));
            ll.setId(lid);
            LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(0, 100);
            lpp.weight = 1;
            tv.setLayoutParams(lpp);
            et.setLayoutParams(lpp);
            ll.addView(tv);
            ll.addView(et);

            RelativeLayout.LayoutParams rll = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            rll.addRule(RelativeLayout.BELOW, ll.getId() - 1);
            rll.setMargins(0, 20, 0, 0);
            lastrid = ll.getId();
            lp.addView(ll, rll);

        }

        updaterowsv.addView(lp);
        //after the layout has been created display it in a alert dialog
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    new AlertDialog.Builder(RoomExplorerActivity.this)
                            .setTitle("values")
                            .setView(updaterowsv)
                            .setCancelable(false)
                            .setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {

                                        //this code will be executed when user changes values of edit text or spinner and clicks on ok button
                                        public void onClick(DialogInterface dialog, int which) {

                                            //get spinner value
                                            String spinnerValue = crud_dropdown.getSelectedItem().toString();

                                            //it he spinner value is update this row get the values from
                                            //edit text fields generate a update query and execute it
                                            if (spinnerValue.equalsIgnoreCase("Update this row")) {
                                                IndexInfo.index = 10;
                                                String query3 = "UPDATE " + IndexInfo.tableName + " SET ";

                                                for (int i = 0; i < columnNames.size(); i++) {
                                                    TextView tvc = columnNames.get(i);
                                                    EditText etc = columnValues.get(i);

                                                    if (!etc.getText().toString().equals("null")) {

                                                        query3 = query3 + tvc.getText().toString() + " = ";

                                                        if (i == columnNames.size() - 1) {

                                                            query3 = query3 + "'" + etc.getText().toString() + "'";

                                                        } else {

                                                            query3 = query3 + "'" + etc.getText().toString() + "' , ";

                                                        }
                                                    }

                                                }
                                                query3 = query3 + " where ";
                                                for (int i = 0; i < columnNames.size(); i++) {
                                                    TextView tvc = columnNames.get(i);
                                                    if (!valueStrings.get(i).equals("null")) {

                                                        query3 = query3 + tvc.getText().toString() + " = ";

                                                        if (i == columnNames.size() - 1) {

                                                            query3 = query3 + "'" + valueStrings.get(i) + "' ";

                                                        } else {
                                                            query3 = query3 + "'" + valueStrings.get(i) + "' and ";
                                                        }

                                                    }
                                                }
                                                //dbm.getData(query3);
                                                List<Cursor> aluc = getData(query3);
                                                Cursor tempc = aluc.get(1);
                                                tempc.moveToLast();

                                                if (tempc.getString(0).equalsIgnoreCase("Success")) {
                                                    tvmessage.setBackgroundColor(Color.parseColor("#2ecc71"));
                                                    tvmessage.setText(IndexInfo.tableName + " table Updated Successfully");
                                                    refreshTable(0);
                                                } else {
                                                    tvmessage.setBackgroundColor(Color.parseColor("#e74c3c"));
                                                    tvmessage.setText("Error:" + tempc.getString(0));
                                                }
                                            }
                                            //it he spinner value is delete this row get the values from
                                            //edit text fields generate a delete query and execute it

                                            if (spinnerValue.equalsIgnoreCase("Delete this row")) {

                                                IndexInfo.index = 10;
                                                String query5 = "DELETE FROM " + IndexInfo.tableName + " WHERE ";

                                                for (int i = 0; i < columnNames.size(); i++) {
                                                    TextView tvc = columnNames.get(i);
                                                    if (!valueStrings.get(i).equals("null")) {

                                                        query5 = query5 + tvc.getText().toString() + " = ";

                                                        if (i == columnNames.size() - 1) {

                                                            query5 = query5 + "'" + valueStrings.get(i) + "' ";

                                                        } else {
                                                            query5 = query5 + "'" + valueStrings.get(i) + "' and ";
                                                        }

                                                    }
                                                }

                                                List<Cursor> aldc = getData(query5);
                                                Cursor tempc = aldc.get(1);
                                                tempc.moveToLast();

                                                if (tempc.getString(0).equalsIgnoreCase("Success")) {
                                                    tvmessage.setBackgroundColor(Color.parseColor("#2ecc71"));
                                                    tvmessage.setText("Row deleted from " + IndexInfo.tableName + " table");
                                                    refreshTable(0);
                                                } else {
                                                    tvmessage.setBackgroundColor(Color.parseColor("#e74c3c"));
                                                    tvmessage.setText("Error:" + tempc.getString(0));
                                                }
                                            }
                                        }

                                    })
                            .setNegativeButton("close",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                            .create().show();
                }
            }
        });
    }

    public void refreshActivity() {
        finish();
        startActivity(getIntent());
    }

    public void refreshTable(int d) {
        Cursor c3 = null;
        tableLayout.removeAllViews();
        if (d == 0) {
            String query8 = "select * from " + IndexInfo.tableName;
            List<Cursor> alc3 = getData(query8);
            c3 = alc3.get(0);
            //saving cursor to the static IndexInfo class which can be resued by the other functions
            IndexInfo.mainCursor = c3;
        }
        if (d == 1) {
            c3 = IndexInfo.mainCursor;
        }
        // if the cursor returened form tha database is not null we display the data in table layout
        if (c3 != null) {
            int counts = c3.getCount();

            tv.setText("" + counts);
            TableRow tableHeader = new TableRow(RoomExplorerActivity.this);

            tableHeader.setBackgroundColor(Color.BLACK);
//            tableHeader.setPadding(0, 2, 0, 2);
            for (int k = 0; k < c3.getColumnCount(); k++) {
                LinearLayout cell = new LinearLayout(RoomExplorerActivity.this);
                cell.setBackgroundColor(Color.WHITE);
                cell.setLayoutParams(tableRowParams);
                final TextView tableHeaderColumns = new TextView(RoomExplorerActivity.this);
//                tableHeaderColumns.setPadding(0, 0, 4, 3);
                tableHeaderColumns.setText("" + c3.getColumnName(k));
                tableHeaderColumns.setTextColor(Color.parseColor("#000000"));
                cell.addView(tableHeaderColumns);
                tableHeader.addView(cell);

            }
            tableLayout.addView(tableHeader);
            c3.moveToFirst();

            //after displaying column names in the first row  we display data in the remaining columns
            //the below paginate table function will display the first 10 tuples of the tables
            //the remaining tuples can be viewed by clicking on the next button
            paginateTable(c3.getCount());
        } else {

            TableRow tableHeader2 = new TableRow(RoomExplorerActivity.this);
            tableHeader2.setBackgroundColor(Color.BLACK);
//            tableHeader2.setPadding(0, 2, 0, 2);

            LinearLayout cell = new LinearLayout(RoomExplorerActivity.this);
            cell.setBackgroundColor(Color.WHITE);
            cell.setLayoutParams(tableRowParams);

            final TextView tableHeaderColumns = new TextView(RoomExplorerActivity.this);
//            tableHeaderColumns.setPadding(0, 0, 4, 3);
            tableHeaderColumns.setText("   Table   Is   Empty   ");
            tableHeaderColumns.setTextSize(30);
            tableHeaderColumns.setTextColor(Color.RED);

            cell.addView(tableHeaderColumns);
            tableHeader2.addView(cell);


            tableLayout.addView(tableHeader2);

            tv.setText("" + 0);
        }

    }

    //the function which displays tuples from database in a table layout
    public void paginateTable(final int number) {


        final Cursor c3 = IndexInfo.mainCursor;
        IndexInfo.numberOfPages = (c3.getCount() / 10) + 1;
        IndexInfo.currentPage = 1;
        c3.moveToFirst();
        int currentrow = 0;

        //display the first 10 tuples of the table selected by user
        do {

            final TableRow tableRow = new TableRow(RoomExplorerActivity.this);
            tableRow.setBackgroundColor(Color.BLACK);
//            tableRow.setPadding(0, 2, 0, 2);

            for (int j = 0; j < c3.getColumnCount(); j++) {
                LinearLayout cell = new LinearLayout(this);
                cell.setBackgroundColor(Color.WHITE);
                cell.setLayoutParams(tableRowParams);
                final TextView columnView = new TextView(RoomExplorerActivity.this);
                String columnData = "";
                try {
                    columnData = c3.getString(j);
                } catch (Exception e) {
                    // Column data is not a string , do not display it
                }
                columnView.setText(columnData);
                columnView.setTextColor(Color.parseColor("#000000"));
//                columnView.setPadding(0, 0, 4, 3);
                cell.addView(columnView);
                tableRow.addView(cell);

            }

            tableRow.setVisibility(View.VISIBLE);
            currentrow++;
            //we create listener for each table row when clicked a alert dialog will be displayed
            //from where user can update or delete the row
            tableRow.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {

                    final List<String> valueString = new ArrayList<>();
                    for (int i = 0; i < c3.getColumnCount(); i++) {
                        LinearLayout columnLinearLayout = (LinearLayout) tableRow.getChildAt(i);
                        TextView tc = (TextView) columnLinearLayout.getChildAt(0);

                        String cv = tc.getText().toString();
                        valueString.add(cv);

                    }
                    IndexInfo.valueString = valueString;
                    //the below function will display the alert dialog
                    updateDeletePopup(0);
                }
            });
            tableLayout.addView(tableRow);


        } while (c3.moveToNext() && currentrow < 10);

        IndexInfo.index = currentrow;


        // when user clicks on the previous button update the table with the previous 10 tuples from the database
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tableStartIndex = (IndexInfo.currentPage - 2) * 10;

                //if the table layout has the first 10 tuples then toast that this is the first page
                if (IndexInfo.currentPage == 1) {
                    Toast.makeText(RoomExplorerActivity.this, "This is the first page", Toast.LENGTH_LONG).show();
                } else {
                    IndexInfo.currentPage = IndexInfo.currentPage - 1;
                    c3.moveToPosition(tableStartIndex);

                    boolean decider = true;
                    for (int i = 1; i < tableLayout.getChildCount(); i++) {
                        TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
                        if (decider) {
                            tableRow.setVisibility(View.VISIBLE);
                            for (int j = 0; j < tableRow.getChildCount(); j++) {
                                LinearLayout columnLinearLayout = (LinearLayout) tableRow.getChildAt(j);
                                TextView columnView = (TextView) columnLinearLayout.getChildAt(0);

                                columnView.setText("" + c3.getString(j));

                            }
                            decider = !c3.isLast();
                            if (!c3.isLast()) {
                                c3.moveToNext();
                            }
                        } else {
                            tableRow.setVisibility(View.GONE);
                        }

                    }

                    IndexInfo.index = tableStartIndex;
                }
            }
        });

        // when user clicks on the next button update the table with the next 10 tuples from the database
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if there are no tuples to be shown toast that this the last page
                if (IndexInfo.currentPage >= IndexInfo.numberOfPages) {
                    Toast.makeText(RoomExplorerActivity.this, "This is the last page", Toast.LENGTH_LONG).show();
                } else {
                    IndexInfo.currentPage = IndexInfo.currentPage + 1;
                    boolean decider = true;


                    for (int i = 1; i < tableLayout.getChildCount(); i++) {
                        TableRow tableRow = (TableRow) tableLayout.getChildAt(i);


                        if (decider) {
                            tableRow.setVisibility(View.VISIBLE);
                            for (int j = 0; j < tableRow.getChildCount(); j++) {
                                LinearLayout columnLinearLayout = (LinearLayout) tableRow.getChildAt(j);
                                TextView columnView = (TextView) columnLinearLayout.getChildAt(0);

                                columnView.setText("" + c3.getString(j));

                            }
                            decider = !c3.isLast();
                            if (!c3.isLast()) {
                                c3.moveToNext();
                            }
                        } else {
                            tableRow.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub

    }

    public List<Cursor> getData(String Query) {
        if (myClass == null) {
            throw new RuntimeException("myClass is not initialized yet!");
        }

        RoomDatabase roomDatabase = Room.databaseBuilder(this, myClass, databaseName).build();

        SupportSQLiteDatabase sqlDB = roomDatabase.getOpenHelper().getWritableDatabase();

        String[] columns = new String[]{"mesage"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        List<Cursor> alc = new ArrayList<>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try {
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.query(Query, null);

            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});

            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0, c);
                c.moveToFirst();

                return alc;
            }
            return alc;
        } catch (Exception sqlEx) {
            sqlEx.printStackTrace();
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }
    }

    static class IndexInfo {
        public static int index = 10;
        public static int numberOfPages = 0;
        public static int currentPage = 0;
        public static String tableName = "";
        public static Cursor mainCursor;
        public static int cursorPosition = 0;
        public static List<String> valueString;
        public static List<String> tableHeaderNames;
        public static List<String> emptyTableColumnNames;
        public static boolean isEmpty;
        public static boolean isCustomQuery;
    }
}
