package com.example.user.stepcounterfitness;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.common.collect.Range;
import com.google.firebase.database.DatabaseReference;



import com.google.firebase.database.FirebaseDatabase;

public class Details extends AppCompatActivity {
    public Button but1,but2;
    EditText Name, Wt, Ht, age;
    String gender;
    private AwesomeValidation awesomeValidation;
    RadioGroup radioGroup;
    private RadioButton radioButton;
    private Button btnDisplay;
    Bundle extras = new Bundle();

    public void init() {

      //  final
        radioGroup = (RadioGroup) findViewById(R.id.radio);
        but2=(Button) findViewById(R.id.achieve);
        but1 = (Button) findViewById(R.id.startwalk);
        Name = (EditText) findViewById(R.id.EditTextName);
        Wt = (EditText) findViewById(R.id.EditTextwt);
        Ht = (EditText) findViewById(R.id.EditTextht);
        age = (EditText) findViewById(R.id.EditTextage);


        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);


        // find the radiobutton by returned id

        awesomeValidation.addValidation(this, R.id.EditTextName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.EditTextage, Range.closed(13, 60), R.string.ageerror);
        awesomeValidation.addValidation(this, R.id.EditTextwt, Range.closed(13, 100), R.string.weighterror);
        awesomeValidation.addValidation(this, R.id.EditTextht, Range.closed(13, 200), R.string.heighterror);



        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(Details.this, Step.class);
                final String name = Details.this.Name.getText().toString();
                Log.d("Name","Print"+name);
                final String weight =Details.this. Wt.getText().toString();
                Log.d("Weight","Print"+weight);
                final String height =( Ht.getText().toString());
                final String Age =( age.getText().toString());
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                gender=(radioButton.getText().toString());
                if (awesomeValidation.validate()) {



                    extras.putString("Gender", radioButton.getText().toString());
                    Log.d("Gender", "radiobutton" + gender);


//database reference pointing to demo node

                    extras.putString("Name", name);
                    extras.putString("Weight", weight);
                    extras.putString("Height", height);
                    extras.putString("Age", Age);


                    go.putExtras(extras);

                    startActivity(go);
                }

            }


        });

        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go = new Intent(Details.this, Achievement.class);


                final String name = Details.this.Name.getText().toString();
                Log.d("Name", "Print" + name);
                final String weight = Details.this.Wt.getText().toString();
                Log.d("Weight", "Print" + weight);
                final String height = (Ht.getText().toString());
                final String Age = (age.getText().toString());
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                gender = (radioButton.getText().toString());
                if (awesomeValidation.validate()) {

                    extras.putString("Name", name);
                    extras.putString("Weight", weight);
                    extras.putString("Height", height);
                    extras.putString("Age", Age);
                    extras.putString("Gender", gender);

                    go.putExtras(extras);

                    startActivity(go);

                }
            }


        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);



//database reference pointing to demo node


        init();
    }


}
