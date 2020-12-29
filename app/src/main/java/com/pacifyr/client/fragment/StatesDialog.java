package com.pacifyr.client.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.pacifyr.client.R;
import com.pacifyr.client.customview.MainTextView;
import com.pacifyr.client.event.BusProvider;
import com.pacifyr.client.model.State;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

public class StatesDialog extends DialogFragment {

    private MainTextView text_selected;
    private ListView list_states;
    private View submit;
    private StateSelectionListener stateSelectionListener;
    private StateAdapter adapter;
    ArrayList<State> mstateList;

    public StatesDialog(ArrayList<State> mstateList) {
        this.mstateList = mstateList;
    }

    public StateSelectionListener getStateSelectionListener() {
        return stateSelectionListener;
    }

    public void setStateSelectionListener(StateSelectionListener stateSelectionListener) {
        this.stateSelectionListener = stateSelectionListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.getInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        BusProvider.getInstance().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.states_dialog, null);

        text_selected = view.findViewById(R.id.text_selected);
        list_states = view.findViewById(R.id.list_states);
        submit = view.findViewById(R.id.submit_ctxv);

        adapter = new StateAdapter(getActivity());
        list_states.setAdapter(adapter);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getStateSelectionListener() != null) {
                    getStateSelectionListener().onSelected(adapter.getSelected());
                }
                getDialog().dismiss();
            }
        });

        getDialog().setTitle("Select States");

        return view;
    }

    @Subscribe
    public void onStateSelected(State state) {
        if (adapter != null) {
            ArrayList<String> selected = adapter.getSelected();
            String selectedString = "";

            for (String sel : selected) {
                selectedString += sel ;
            }

            text_selected.setText(selectedString.isEmpty() ? "Selected : NONE" : "Selected: " + selectedString);

        }
    }

    public interface StateSelectionListener {
        void onSelected(ArrayList<String> states);
    }

    public class StateAdapter extends BaseAdapter {


        private final Context context;

        public StateAdapter(Context context) {
            this.context = context;

        }

        public ArrayList<String> getSelected() {
            ArrayList<String> selected = new ArrayList<>();
            for (int i = 0; i < mstateList.size(); i++) {
                if (mstateList.get(i).isSelected()) {
                    selected.add(0,mstateList.get(i).getName());
                }
            }
            return selected;

        }

        @Override
        public int getCount() {
            return mstateList != null ? mstateList.size() : 0;
        }

        @Override
        public State getItem(int position) {
            return mstateList != null ? mstateList.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView =
                    LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_checked, null);
            CheckedTextView textView = convertView.findViewById(android.R.id.text1);
            textView.setChecked(getItem(position).isSelected());
            textView.setText(getItem(position).getName());

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getItem(position).setSelected(!getItem(position).isSelected());
                    for (int i=0;i<mstateList.size();i++)
                    {
                        if (i!=position)
                        {
                            getItem(i).setSelected(false);
                        }
                    }
                    notifyDataSetChanged();
                    BusProvider.getInstance().post(getItem(position));
                }
            });
            return convertView;
        }
    }
}
