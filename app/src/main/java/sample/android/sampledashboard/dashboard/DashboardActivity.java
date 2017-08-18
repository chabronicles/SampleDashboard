package sample.android.sampledashboard.dashboard;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sample.android.sampledashboard.R;
import sample.android.sampledashboard.RecordProvider;
import sample.android.sampledashboard.dashboard.record.Record;

public class DashboardActivity extends AppCompatActivity implements RecordListener {

    private TextView welcomeMessage;
    private ListView recordList;
    private RecordsAdapter adapter;
    private List<Record> records = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        welcomeMessage = (TextView) findViewById(R.id.welcomeMessage);
        recordList = (ListView) findViewById(R.id.recordList);

        fetchIntent();
        records.addAll(fetchRecords());

        adapter = new RecordsAdapter(this, R.layout.list_record, records, this);
        recordList.setAdapter(adapter);
    }

    private void fetchIntent() {
        String name = getIntent().getStringExtra("USERNAME");
        welcomeMessage.setText(getString(R.string.welcome, name));
    }

    private List<Record> fetchRecords() {
        Uri recordsUri = RecordProvider.CONTENT_URI;
        Cursor c = managedQuery(recordsUri, null, null, null, null);

        List<Record> records = new ArrayList<>();
        if (c != null && c.moveToFirst()) {
            do {
                Record record = new Record();
                record.setId(c.getInt(c.getColumnIndex(RecordProvider._ID)));
                record.setTimestamp(c.getString(c.getColumnIndex(RecordProvider.TIMESTAMP)));
                record.setMessage(c.getString(c.getColumnIndex(RecordProvider.MESSAGE)));

                records.add(record);
            } while (c.moveToNext());
        }

        return records;
    }

    private void addRecord() {
        LayoutInflater inflater = LayoutInflater.from(DashboardActivity.this);
        View dialogView = inflater.inflate(R.layout.dialog_add_record, null);
        final EditText messageField = (EditText) dialogView.findViewById(R.id.message);
        final Button addBtn = (Button) dialogView.findViewById(R.id.add);
        final Button cancelBtn = (Button) dialogView.findViewById(R.id.cancel);

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCanceledOnTouchOutside(false);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageField == null || messageField.getText().toString().length() < 1) {
                    messageField.setError("Message cannot be empty");
                    messageField.requestFocus();
                } else {
                    // add new record
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(RecordProvider.TIMESTAMP, Calendar.getInstance().getTime().toString());
                    contentValues.put(RecordProvider.MESSAGE, messageField.getText().toString());

                    getContentResolver().insert(RecordProvider.CONTENT_URI, contentValues);
                    records.clear();
                    records.addAll(fetchRecords());
                    adapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public void editRecord(final Record record) {
        LayoutInflater inflater = LayoutInflater.from(DashboardActivity.this);
        View dialogView = inflater.inflate(R.layout.dialog_add_record, null);
        final EditText messageField = (EditText) dialogView.findViewById(R.id.message);
        final Button editBtn = (Button) dialogView.findViewById(R.id.add);
        final Button cancelBtn = (Button) dialogView.findViewById(R.id.cancel);

        editBtn.setText(getResources().getString(R.string.edit));
        messageField.setText(record.getMessage());

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setCanceledOnTouchOutside(false);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messageField == null || messageField.getText().toString().length() < 1) {
                    messageField.setError("Message cannot be empty");
                    messageField.requestFocus();
                } else if (record.getMessage().equals(messageField.getText().toString())) {
                    messageField.setError("There are no changes made");
                    messageField.requestFocus();
                } else {
                    // edit record
                    Uri uri = Uri.parse(RecordProvider.URL + "/" + record.getId());

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(RecordProvider.TIMESTAMP, Calendar.getInstance().getTime().toString());
                    contentValues.put(RecordProvider.MESSAGE, messageField.getText().toString());

                    getContentResolver().update(uri, contentValues, null, null);
                    records.clear();
                    records.addAll(fetchRecords());
                    adapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public void deleteRecord(Record record) {
        Uri uri = Uri.parse(RecordProvider.URL + "/" + record.getId());
        getContentResolver().delete(uri, null, null);
        records.clear();
        records.addAll(fetchRecords());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                addRecord();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
