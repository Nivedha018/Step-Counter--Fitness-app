package com.example.user.stepcounterfitness;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Achievement extends AppCompatActivity {
    String name,weight,height,Age;
    BarChart chart ;
    ArrayList<BarEntry> BARENTRY,BARENTRY1 ;
    ArrayList<String> BarEntryLabels,BarEntryLabels1 ;
    BarDataSet Bardataset ;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    BarData BARDATA ;
    private Object ValueEventListener;
    private com.google.firebase.database.ValueEventListener valueEventListener;
    String date,dat;
    Button cal,dist,step,picker,okay;
    DatePickerDialog dpd;
    TextView choosendate;
    int day,month,year;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievement);
        Bundle extras = getIntent().getExtras();
        name = extras.getString("Name");
        weight = (extras.getString("Weight"));
        height = extras.getString("Height");
        Age = (extras.getString("Age"));
        chart = (BarChart) findViewById(R.id.chart1);
        DatePicker datePicker = (DatePicker) findViewById(R.id.date_picker);
        choosendate=findViewById(R.id.dateview);
        picker=findViewById(R.id.pick);
        okay=findViewById(R.id.ok);
        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<String>();


        BARENTRY.add(new BarEntry(0f, 1));
        BARENTRY.add(new BarEntry(0f, 2));
        BARENTRY.add(new BarEntry(0f, 3));
        BARENTRY.add(new BarEntry(0f, 4));
        BARENTRY.add(new BarEntry(0f, 5));
        BARENTRY.add(new BarEntry(0f, 6));
        BARENTRY.add(new BarEntry(0f, 7));


        BarEntryLabels.add("Day1");
        BarEntryLabels.add("Day2");
        BarEntryLabels.add("Day3");
        BarEntryLabels.add("Day4");
        BarEntryLabels.add("Day5");
        BarEntryLabels.add("Day6");
        BarEntryLabels.add("Day7");






        Bardataset = new BarDataSet(BARENTRY, "Achievements");

        BARDATA = new BarData(BarEntryLabels, Bardataset);

        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

        chart.setData(BARDATA);

        chart.animateY(3000);
        picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cldr = Calendar.getInstance();
                day = cldr.get(Calendar.DAY_OF_MONTH);
               month = cldr.get(Calendar.MONTH)+1;
                 year = cldr.get(Calendar.YEAR);



                dpd= new DatePickerDialog(Achievement.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        if((month+1)/10!=0)
                        {
                            date= String.valueOf(day)+String.valueOf(month+1)+String.valueOf(year);
                        }
                        else
                        date= String.valueOf(day)+"0"+String.valueOf(month+1)+String.valueOf(year);
                        Log.d("Choose","Date"+date);
                        choosendate.setText(String.valueOf(day)+"/0"+String.valueOf(month+1)+"/"+String.valueOf(year));


                    }
                },day,month,year);
                dpd.show();


            }


        });

       okay.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               myRef.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                   {

                       ShowData(dataSnapshot);
                   }


                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {

                   }
               });

           }
       });



        cal=findViewById(R.id.calories);
        dist=findViewById(R.id.distance);
        step=findViewById(R.id.steps);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();




    }
        public void ShowData(DataSnapshot dataSnapshot) {

            BARENTRY1 = new ArrayList<>();
            BarEntryLabels1 = new ArrayList<String>();
            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                 Information info = new Information();

                   if (ds.child(name).child(date).exists()) {
                       info.setSteps(ds.child(name).child(date).getValue(Information.class).getSteps());
                       info.setDistance(ds.child(name).child(date).getValue(Information.class).getDistance());
                       info.setCalorie(ds.child(name).child(date).getValue(Information.class).getCalorie());
                       Log.d("Steps", "Read" + info.getSteps());
                       Log.d("Distance", "Read" + info.getDistance());
                       Log.d("Calories", "Read" + info.getCalorie());
                       int s = info.getSteps();
                       float dis = info.getDistance();
                       double c = info.getCalorie();
                       cal.setText("" + c);
                       dist.setText("" + dis);
                       step.setText("" + s);

                      for (int i = 6; i >= 0; i--) {
                          Log.d("Inside","for");

                          if ((month + 1) / 10 != 0)
                               dat = String.valueOf(day - i) + String.valueOf(month ) + String.valueOf(year);
                           else
                               dat = String.valueOf(day - i) + "0" + String.valueOf(month) + String.valueOf(year);

                           if (ds.child(name).child(dat).exists())
                           {
                               info.setSteps(ds.child(name).child(dat).getValue(Information.class).getSteps());

                                s=info.getSteps();

                             BARENTRY1.add(new BarEntry((s),i));

                               BarEntryLabels1.add("Day"+(7-i));





                          } else {
                               BARENTRY1.add(new BarEntry(0, i));
                               BarEntryLabels1.add("Day"+(7-i));

                           }


                       }

                       Bardataset = new BarDataSet(BARENTRY1, "Achievements");

                       BARDATA = new BarData(BarEntryLabels1, Bardataset);

                       Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);

                       chart.setData(BARDATA);

                       chart.animateY(3000);
                   }
                   else
                   {
                       AlertDialog.Builder builder
                               = new AlertDialog
                               .Builder(Achievement.this);

                       // Set the message show for the Alert time
                       builder.setMessage("No work done  on this date..." +
                               "Do you Want to choose other Date?");

                       // Set Alert Title
                       builder.setTitle("Alert !");

                       builder.setCancelable(false);
                       builder
                               .setPositiveButton(
                                       "Yes",
                                       new DialogInterface
                                               .OnClickListener() {

                                           @Override
                                           public void onClick(DialogInterface dialog,
                                                               int which) {

                                               dialog.cancel();
                                               Calendar cldr = Calendar.getInstance();
                                               int day = cldr.get(Calendar.DAY_OF_MONTH);
                                               int month = cldr.get(Calendar.MONTH) + 1;
                                               int year = cldr.get(Calendar.YEAR);


                                               dpd = new DatePickerDialog(Achievement.this, new DatePickerDialog.OnDateSetListener() {
                                                   @Override
                                                   public void onDateSet(DatePicker view, int year, int month, int day) {
                                                       date = String.valueOf(day) + "0" + String.valueOf(month + 1) + String.valueOf(year);

                                                       choosendate.setText(String.valueOf(day) + "/0" + String.valueOf(month + 1) + "/" + String.valueOf(year));


                                                   }
                                               }, day, month, year);
                                               dpd.show();
                                           }


                                       });

                       builder.setNegativeButton(
                                       "No",
                                       new DialogInterface
                                               .OnClickListener() {

                                           @Override
                                           public void onClick(DialogInterface dialog,
                                                               int which)
                                           {
                                               dialog.cancel();
                                           }
                                       });

                       AlertDialog alertDialog = builder.create();

                       alertDialog.show();
                   }

               }

            }








}
