package com.pab.mooneyq.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.pab.mooneyq.databinding.ActivityFilterBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FilterActivity extends BaseActivity {

    private ActivityFilterBinding binding;
    private final Calendar calendar = Calendar.getInstance();

    private Integer dateType = 0;
    private String dateStart = "", dateEnd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupListener();
    }

    private void setupListener() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                setLabel();
            }
        };

        binding.etDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FilterActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                dateType = 1;
            }
        });

        binding.etDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(FilterActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                dateType = 2;
            }
        });

        binding.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dateStart.isEmpty() && !dateEnd.isEmpty()) {
                    Intent intent = new Intent();
                    intent.putExtra("date_start", dateStart);
                    intent.putExtra("date_end", dateEnd);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void setLabel() {
        if (dateType == 1) {
            dateStart = dateFormat(calendar.getTime(), "yyyy-MM-dd");
            binding.etDateStart.setText( dateFormat(calendar.getTime(), "MMM, dd yyyy") );
        } else if (dateType == 2) {
            dateEnd = dateFormat(calendar.getTime(), "yyyy-MM-dd");
            binding.etDateEnd.setText( dateFormat(calendar.getTime(), "MMM, dd yyyy") );
        }
    }

    private String dateFormat(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

}