//package com.example.pccmedicare20;
//
//
//
//import androidx.appcompat.app.AppCompatActivity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
////import android.support.annotation.Nullable;
////import android.support.v7.app.AppCompatActivity;
//import android.widget.CalendarView;
//
//import java.util.ArrayList;
//
//public class track extends AppCompatActivity {
//
//    //private static final String TAG = "MainActivity";
//    private static final String TAG = "calendar";
//    String dt = "2022/11/14";
//    ArrayList<String[]> meds = new ArrayList<String[]>();
//    private TextView thedate, medInfo;
//    private Button buttonBack;
//    private CalendarView mCalendarView;
//    String startDate, medName;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_calendar);
//        thedate = (TextView) findViewById(R.id.textViewDate);
//        medInfo = (TextView) findViewById(R.id.textViewMedInfo);
//        buttonBack = (Button) findViewById(R.id.buttonBack);
//
//        // pull values out of intent
//        Intent in = getIntent();
//        meds = (ArrayList<String[]>) in.getSerializableExtra("xxx");// get curent list of meds
//        String B = "";
//        for(String[] a: meds) {
//            B=B+"\n"+"- "+a[0]+" "+a[1]+" "+a[2];
//            startDate = a[4];
//            medName = a[0];
//        }
//
//
//        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
//        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(CalendarView CalendarView, int year, int month, int dayOfMonth) {
//                month++;
//                String date = year + "-" + month + "-" + dayOfMonth;
//                thedate.setText(date);
//                if (startDate!=null) { // condition to display medication for selected day.
//                    medInfo.setText(medName);
//                }
//
//                int startDate = 5;
//                int displayDate = dayOfMonth;
//                        for(int i=0;i<4;i++);
////                        {
////                           if(displayDate == startDate+(7*i))
////                               setText;
////                        }
//
//            }
//        });
//    }
//    public void goBack (View view) {
//        Intent intent = new Intent(this, show.class);
//        startActivity(intent);
//    }
//}

package com.example.pccmedicare20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class track extends AppCompatActivity {
    String dt = "2022/11/14";
    Button goback;
    ArrayList<String[]> meds = new ArrayList<String[]>();
    LocalDate caldt;
    LocalDate smeddt;
    String date;
    private TextView thedate, medInfo;
    private CalendarView mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        goback = (Button) findViewById(R.id.goback);
        Intent in = getIntent();
        meds = (ArrayList<String[]>) in.getSerializableExtra("xxx");// get curent list of meds

        thedate = (TextView) findViewById(R.id.textViewDate);
        medInfo = (TextView) findViewById(R.id.textViewMedInfo);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);

        // code to populate output on opening tack page
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(today);
        thedate.setText(formattedDate);
        caldt = LocalDate.parse(formattedDate);
        displayInfo();


        mCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView CalendarView, int year, int month, int dayOfMonth) {
                month++;
                date = year + "-";
                if (month < 10)
                    date = date + "0" + month + "-";
                else
                    date = date + month + "-";
                if (dayOfMonth < 10)
                    date = date + "0" + dayOfMonth;
                else
                    date = date + dayOfMonth;

                thedate.setText(date);
                caldt = LocalDate.parse(date);
                displayInfo();
          }

        });
        goback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                home();
            }
        });
    }

    private void home() {
        Intent intent = new Intent(this, show.class);
        Bundle bundle = new Bundle();

        intent.putExtra("xxx", meds);
        intent.putExtras(bundle);

        startActivity(intent);
    }

    // before calling displayInfo() need to set 'caldt' variable
    private void displayInfo(){
        String take = "";

        for (String[] a : meds) {
            smeddt = LocalDate.parse(a[4]);
            if (a[2].equals("daily")) {
                if (caldt.isAfter(smeddt)) {
                    take = take + "Take " + a[0] + "\n";
                }
            }
            if (a[2].equals("weekly")) {
                LocalDate temdt = smeddt;
                for (int i = 0; i <= 52; i++) { // i<10 signifies 10 weeks on meds. Alter to 52 for whole year.
                    temdt = temdt.plusDays(7);
                    if (temdt.compareTo(caldt) == 0)
                        take = take + "Take " + a[0] + "\n";
                }
            }
        }

        // refill
        String refil = "";

        for (String[] a : meds) {

            if (LocalDate.parse(a[6]).equals(caldt)) {
                refil = refil + "Refill " + a[0] + "\n";
            }
        }
        medInfo.setText(refil + "\n" + take);
    }
}


