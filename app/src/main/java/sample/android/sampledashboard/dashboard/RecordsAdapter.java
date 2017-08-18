package sample.android.sampledashboard.dashboard;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import sample.android.sampledashboard.R;
import sample.android.sampledashboard.dashboard.record.Record;

public class RecordsAdapter extends ArrayAdapter<Record> {
    @NonNull
    private Context context;
    private int resource = 0;
    @NonNull
    private List<Record> records;
    private RecordListener listener;

    public RecordsAdapter(@NonNull Context context, @LayoutRes int resource,
                          @NonNull List<Record> records, RecordListener listener) {
        super(context, resource, records);
        this.context = context;
        this.resource = resource;
        this.records = records;
        this.listener = listener;
    }

    public RecordsAdapter(@NonNull Context context, @LayoutRes int resource,
                          @NonNull List<Record> records) {
        super(context, resource, records);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Record record = records.get(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(resource, parent, false);
            viewHolder.timestamp = (TextView) convertView.findViewById(R.id.timestamp);
            viewHolder.message = (TextView) convertView.findViewById(R.id.message);
            viewHolder.editBtn = (Button) convertView.findViewById(R.id.edit);
            viewHolder.deleteBtn = (Button) convertView.findViewById(R.id.delete);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.timestamp.setText(record.getTimestamp());
        viewHolder.message.setText(record.getMessage());
        viewHolder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.editRecord(record);
            }
        });
        viewHolder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.deleteRecord(record);
            }
        });

        return result;
    }

    // View lookup cache
    public class ViewHolder {
        TextView timestamp;
        TextView message;
        Button editBtn;
        Button deleteBtn;
    }
}
