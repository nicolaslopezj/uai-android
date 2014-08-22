package cl.uai.uai.sports;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cl.uai.uai.R;

/**
 * Created by nicolaslopezj on 30-07-14.
 */
public class Sports extends Fragment {

    protected ListView itemsListView;
    protected ItemsArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.sports_main, null);

        itemsListView = (ListView) layout.findViewById(R.id.itemsListView);
        String[] items = new String[30];

        for (int i = 0; i < 30; i++) {
            if (i % 3 == 0) {
                items[i] = "Basketball";
            } else {
                items[i] = "Futbolito Hombres";
            }
        }
        //

        adapter = new ItemsArrayAdapter(layout.getContext(), items);
        itemsListView.setAdapter(adapter);

        return layout;
    }

    private class ItemsArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;

        public ItemsArrayAdapter(Context context, String[] values) {
            super(context, R.layout.sports_row, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final String identifier = values[position];
            View rowView = inflater.inflate(R.layout.sports_row, parent, false);

            TextView previewTextView = (TextView) rowView.findViewById(R.id.nameTextView);
            previewTextView.setText(identifier);

            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    //setContent(identifier);
                }
            });

            return rowView;
        }
    }

}
