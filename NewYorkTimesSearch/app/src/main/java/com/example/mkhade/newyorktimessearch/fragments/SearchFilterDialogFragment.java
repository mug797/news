package com.example.mkhade.newyorktimessearch.fragments;

import butterknife.BindView;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mkhade.newyorktimessearch.R;
import com.example.mkhade.newyorktimessearch.activity.SearchActivity;
import com.example.mkhade.newyorktimessearch.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;

import static android.R.attr.id;
import static com.example.mkhade.newyorktimessearch.activity.SearchActivity.Filters.beginDate;
import static com.example.mkhade.newyorktimessearch.activity.SearchActivity.Filters.newsDesk;

public class SearchFilterDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    @BindView(R.id.etBeginDate) EditText etBeginDate;
    @BindView(R.id.spSortOrder) Spinner spSortOrder;
    @BindView(R.id.cbArts) CheckBox cbArts;
    @BindView(R.id.cbSports) CheckBox cbSports;
    @BindView(R.id.cbFashionStyle) CheckBox cbFashionStyle;
    @BindView(R.id.btCancel) Button btcancel;
    @BindView(R.id.btSave) Button btsave;

    private StringBuilder dateQuery;

    public SearchFilterDialogFragment() {
        // Required empty public constructor
    }

    void setUpSpinner(){
        //Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.spSortOrderEnteries, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spSortOrder.setAdapter(adapter);
        spSortOrder.setSelection(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_filter_dialog, container, false);
        ButterKnife.bind(this, view);

        getDialog().setTitle("Apply Filter");
        setUpSpinner();
        dateQuery = new StringBuilder();
        return view;
    }

    void updateFilterWindow(){
        getBeginDateSelection();
        getSortOrderSelection();
        getNewsDeskSelection();
    }

    void getSortOrderSelection(){
        int pos = spSortOrder.getSelectedItemPosition();

        switch (pos) {
            case 0:
                SearchActivity.Filters.sortOrder = SearchActivity.SortOrder.OLDEST;
                break;
            case 1:
                SearchActivity.Filters.sortOrder = SearchActivity.SortOrder.NEWEST;
                break;
            default:
                Log.e("MK:ERROR", "WRONG SORT ORDER SELECTION");
                break;
        }
    }

    void getBeginDateSelection(){
        SearchActivity.Filters.beginDate = new SearchActivity.BeginDate(etBeginDate.getText().toString(), dateQuery.toString());
    }

    void getNewsDeskSelection(){
        ArrayList<SearchActivity.NewsDesk> checkedNewsDesks = new ArrayList<SearchActivity.NewsDesk>();
        if (cbArts.isChecked()) {
            checkedNewsDesks.add(SearchActivity.NewsDesk.ARTS);
        }
        if (cbFashionStyle.isChecked()) {
            checkedNewsDesks.add(SearchActivity.NewsDesk.FASHION_AND_STYLR);
        }
        if (cbSports.isChecked()) {
            checkedNewsDesks.add(SearchActivity.NewsDesk.SPORTS);
        }
        Log.i("MKKK:", "Checked news desk: " + checkedNewsDesks);
        SearchActivity.Filters.newsDesk = checkedNewsDesks;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        etBeginDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        btsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFilterWindow();
                ((SearchActivity)getActivity()).onNewArticleSearch();
                dismiss();
            }
        });

        btcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void showDatePickerDialog(View v) {
        FragmentManager fm = getFragmentManager();
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(SearchFilterDialogFragment.this, Constants.getFragmentsStatusCodeResult());
        newFragment.show(getFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        Log.i("MKKK", "onDataSet Returned: " + year + "/" + monthOfYear + "/" + dayOfMonth);

        /*final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);*/

        etBeginDate.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
        //dateQuery = new StringBuilder();
        dateQuery.append(year);

        if (monthOfYear < 10) {
            dateQuery.append("0" + monthOfYear);
        } else {
            dateQuery.append(monthOfYear);
        }

        if (dayOfMonth < 10) {
            dateQuery.append("0" + dayOfMonth);
        } else {
            dateQuery.append(dayOfMonth);
        }
    }
}