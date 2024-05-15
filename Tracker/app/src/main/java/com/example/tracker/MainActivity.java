package com.example.tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tracker.databinding.ActivityMainBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    int j=0,i;
    Fragment task_layout;
    Calendar calendar;
    RelativeLayout layout;
    LinearLayout host_layout,month_layout,week_layout;
    Button[][] btn;
    RadioGroup[] radioGroup_b;
    ActivityMainBinding binding;
    String[] days_of_week;
    int delta_l,delta_r,y,currentDay,WeekDay,width,height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setContentView(R.layout.activity_main);

        //Наш экран MainActivity
        layout = (RelativeLayout) findViewById(R.id.layout);
        host_layout = (LinearLayout) findViewById(R.id.host_layout);
        month_layout = (LinearLayout) findViewById(R.id.month_layout);
        week_layout = (LinearLayout) findViewById(R.id.week_layout);

        //Задать размеры TextView
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth() / 7;
        height = display.getHeight() / 8;

        //подпись к днем недели
        week_days_name();

        //Определить текущую дату
        calendar = Calendar.getInstance(TimeZone.getDefault());
        Date currentTime = calendar.getTime();
        currentDay = calendar.get(Calendar.DATE);
        WeekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR);


        //подпись даты
        String time = Integer.toString(currentDay) + "/" + Integer.toString(currentMonth) + "/" + Integer.toString(currentYear);
        binding.TVDATE.setText(time);

        fn(delta_r);

        binding.BUT1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delta_r = -1;
                currentDay = calendar.get(Calendar.DATE);

                int currentMonth = calendar.get(Calendar.MONTH) + 1 + delta_r;
                int currentYear = calendar.get(Calendar.YEAR);

                String time = Integer.toString(currentDay) + "/" + Integer.toString(currentMonth) + "/" + Integer.toString(currentYear);
                binding.TVDATE.setText(time);

                for (int j = 0; j < 6; j++)
                    host_layout.removeView(radioGroup_b[j]);

                fn(delta_r);
            }
        });

        binding.BUT2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delta_r = 1;

                for (int j = 0; j < 6; j++)
                    host_layout.removeView(radioGroup_b[j]);

                currentDay = calendar.get(Calendar.DATE);
                int currentMonth = calendar.get(Calendar.MONTH) + 1 + delta_r;
                int currentYear = calendar.get(Calendar.YEAR);

                String time = Integer.toString(currentDay) + "/" + Integer.toString(currentMonth) + "/" + Integer.toString(currentYear);
                binding.TVDATE.setText(time);

                fn(delta_r);
            }
        });
    }

    void week_days_name(){
        days_of_week = new String[]{"ПН","ВТ","СР","ЧТ","ПТ","СБ","ВС"};
        TextView[] tv = new TextView[7];
        RadioGroup radioGroup = new RadioGroup(this);
        LinearLayout.LayoutParams groupParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        radioGroup.setLayoutParams(groupParams);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);
        for (int j=0;j<7;j++) {
            tv[j] = new TextView(this);
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(width, height);
            tv[j].setLayoutParams(tvParams);
            tv[j].setText(days_of_week[j]);
            tv[j].setId(j);
            tv[j].setGravity(Gravity.CENTER);
            radioGroup.addView(tv[j], tvParams);
        }
        week_layout.addView(radioGroup);
    }


    void fn(int y){

        calendar.add(Calendar.MONTH, y);
        WeekDay = calendar.get(Calendar.DAY_OF_WEEK)-1;

        currentDay -= (currentDay/7)*7;

        if (WeekDay<currentDay)
            WeekDay = 7 - Math.abs(WeekDay - currentDay);
        else
            WeekDay = (WeekDay - currentDay)%7;
        int max_day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);


        btn = new Button[6][7];
        radioGroup_b = new RadioGroup[6];

        RelativeLayout.LayoutParams groupParams_b = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        groupParams_b.setMargins(width*WeekDay, 0, 0, 0);

        RelativeLayout.LayoutParams groupParams_b1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);

        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(width, height);

        j=0;
        radioGroup_b[j] = new RadioGroup(this);
        radioGroup_b[j].setOrientation(LinearLayout.HORIZONTAL);

        radioGroup_b[j].setLayoutParams(groupParams_b);
        j=0;
        for (i = 0; i<7-WeekDay;i++){
            btn[j][i] = new Button(this);
            btn[j][i].setLayoutParams(buttonParams);
            btn[j][i].setText(Integer.toString(i+1));//(days_of_week[WeekDay]);
            btn[j][i].setId(i);
            radioGroup_b[j].addView(btn[j][i], buttonParams);

            Button btn_x = btn[j][i];

            btn_x.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    btn_x.getBackground().setColorFilter(Color.parseColor("#1cd000"), PorterDuff.Mode.MULTIPLY);
                    Toast.makeText(getApplicationContext(),"day number "+Integer.toString(btn_x.getId()+1),Toast.LENGTH_SHORT).show();
                }
            });

        }
        month_layout.addView(radioGroup_b[j]);

        for (j=1;j<(max_day-7+WeekDay)/7+1;j++) {
            radioGroup_b[j] = new RadioGroup(this);
            radioGroup_b[j].setOrientation(LinearLayout.HORIZONTAL);
            radioGroup_b[j].setLayoutParams(groupParams_b1);

            for (i = 0; i < 7; i++) {
                btn[j][i] = new Button(this);
                btn[j][i].setLayoutParams(buttonParams);
                btn[j][i].setText(Integer.toString(7-WeekDay+i+(j-1)*7+1));
                btn[j][i].setId(j * 7 + i);
                radioGroup_b[j].addView(btn[j][i], buttonParams);
            }

            month_layout.addView(radioGroup_b[j]);
        }

        int val = 7-WeekDay+(i-1)+(j-2)*7+1;


        if (val<max_day){
            radioGroup_b[j] = new RadioGroup(this);
            radioGroup_b[j].setOrientation(LinearLayout.HORIZONTAL);

            radioGroup_b[j].setLayoutParams(groupParams_b1);

            for (i = 0; i<max_day - val; i++){
                btn[j][i] = new Button(this);
                btn[j][i].setLayoutParams(buttonParams);
                btn[j][i].setText(Integer.toString(val+i+1));
                btn[j][i].setId(val+i);
                radioGroup_b[j].addView(btn[j][i], buttonParams);

            }
            month_layout.addView(radioGroup_b[j]);
        }
        //layout.addView(days_layout);
        //TasksFragment tasksFragment = (TasksFragment) getSupportFragmentManager().findFragmentById(R.id.);

    }
}