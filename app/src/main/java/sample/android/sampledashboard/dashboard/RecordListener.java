package sample.android.sampledashboard.dashboard;

import sample.android.sampledashboard.dashboard.record.Record;

public interface RecordListener {
    void editRecord(Record record);
    void deleteRecord(Record record);
}
