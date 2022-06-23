package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidtracker.api.ApiUtilities;
import com.example.covidtracker.api.countryData;
import com.squareup.picasso.Picasso;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView confirm,active,recovered,death,tests;
    private TextView confirm_today,recovered_today,death_today,dateTV;
    private PieChart pieChart;
    private ImageView flag;
    private List<countryData> list;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list=new ArrayList<>();

        init();

        ApiUtilities.getApiInterface().getcountryData().enqueue(new Callback<List<countryData>>() {
            @Override
            public void onResponse(Call<List<countryData>> call, Response<List<countryData>> response) {
                list.addAll(response.body());
                for(int i=0;i<list.size();i++)
                {
                    if(list.get(i).getCountry().equals("India"))
                    {




                        Picasso.get().load("https://disease.sh/assets/img/flags/in.png").into(flag);

                        String nConfirm=list.get(i).getCases();
                        String nActive=list.get(i).getActive();
                        String nRecovered=list.get(i).getRecovered();
                        String nDeath=list.get(i).getDeaths();

                        confirm.setText(nConfirm);
                        active.setText(nActive);
                        recovered.setText(nRecovered);
                        death.setText(nDeath);

                        death_today.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayDeaths())));
                        confirm_today.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayCases())));
                        recovered_today.setText(NumberFormat.getInstance().format(Integer.parseInt(list.get(i).getTodayRecovered())));

                        setText(list.get(i).getUpdated());

                        pieChart.addPieSlice(new PieModel( "Confirm",Integer.parseInt(nConfirm),getResources().getColor(R.color.yellow)));
                        pieChart.addPieSlice(new PieModel( "Active",Integer.parseInt(nActive),getResources().getColor(R.color.blue_pie)));
                        pieChart.addPieSlice(new PieModel( "Recovered",Integer.parseInt(nRecovered) ,getResources().getColor(R.color.green_pie)));
                        pieChart.addPieSlice(new PieModel( "Deaths",Integer.parseInt(nDeath),getResources().getColor(R.color.red_pie)));

                        pieChart.startAnimation();


                    }
                }
            }

            @Override
            public void onFailure(Call<List<countryData>> call, Throwable t) {


                Toast.makeText(MainActivity.this,"Error :"+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void setText(String updated) {
        DateFormat format=new SimpleDateFormat("MMM dd, yyyy");
        long milliseconds=Long.parseLong(updated);
        Calendar calendar= Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);

        dateTV.setText("Updated at "+format.format(calendar.getTime()));
    }

    private void init()
    {
        confirm=findViewById(R.id.confirm);
        active=findViewById(R.id.active);
        recovered=findViewById(R.id.recovered);
        death=findViewById(R.id.death);
        tests=findViewById(R.id.tests);
        confirm_today=findViewById(R.id.confirm_today);
        recovered_today=findViewById(R.id.recovered_today);
        death_today=findViewById(R.id.death_today);

        dateTV=findViewById(R.id.date);
        pieChart=findViewById(R.id.pieChart);

        flag=findViewById(R.id.flag);

    }

}