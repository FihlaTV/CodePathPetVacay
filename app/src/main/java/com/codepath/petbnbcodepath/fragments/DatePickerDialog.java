package com.codepath.petbnbcodepath.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.andexert.calendarlistview.library.DayPickerView;
import com.andexert.calendarlistview.library.SimpleMonthAdapter;

import com.codepath.petbnbcodepath.R;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.Date;

/**
 * Created by vibhalaljani on 3/16/15.
 */
public class DatePickerDialog extends DialogFragment implements
        com.andexert.calendarlistview.library.DatePickerController {

    private DayPickerView pickerView;

    private OnDatePickerDialogListener listener;

    private int num_dates_selected = 0;
    private Date drop_date;
    private Date pick_date;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnDatePickerDialogListener) {
            listener = (OnDatePickerDialogListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }


    public interface OnDatePickerDialogListener {
        public void onDatesSelected(Date dropOffDate, Date pickUpDate);
    }

    public DatePickerDialog() {

    }

    public static DatePickerDialog newInstance() {
        DatePickerDialog datePickerDialog = new DatePickerDialog();
        return datePickerDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_picker, container);

        getDialog().setTitle(getResources().getString(R.string.sel_drop_date));

        pickerView = (DayPickerView) view.findViewById(R.id.pickerView);
        pickerView.setmController(this);

        //getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        return view;
    }

    @Override
    public int getMaxYear()
    {
        return 2015;
    }


    @Override
    public void onDayOfMonthSelected(int year, int month, int day)
    {

        num_dates_selected++;
        if (num_dates_selected == 1) {
            getDialog().setTitle(getResources().getString(R.string.sel_pickup_date));
            drop_date = new Date(year, month, day);
            Toast.makeText(getActivity(), getResources().getString(R.string.sel_pickup_date),
                    Toast.LENGTH_SHORT).show();
        }
        if (num_dates_selected == 2) {
            pick_date = new Date(year, month, day);
            LocalDate dropOffDateJoda = new LocalDate(drop_date);
            LocalDate pickUpDateJoda = new LocalDate(pick_date);
            int total_nights = Days.daysBetween(dropOffDateJoda, pickUpDateJoda).getDays();
            if (total_nights < 1) {
                Toast.makeText(getActivity(), getResources().getString(R.string.pick_proper_date),
                               Toast.LENGTH_SHORT).show();
                num_dates_selected = 0;
                getDialog().setTitle(getResources().getString(R.string.sel_drop_date));
            } else {
                listener.onDatesSelected(drop_date, pick_date);
                num_dates_selected = 0;
                dismiss();
            }
        }

    }


    //@Override
    public void onDateRangeSelected(final SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> selectedDays)
    {
        Log.e("Date range selected", selectedDays.getFirst().toString() + " --> " + selectedDays.getLast().toString());
        listener.onDatesSelected(selectedDays.getFirst().getDate(), selectedDays.getLast().getDate());
        dismiss();
    }
}
