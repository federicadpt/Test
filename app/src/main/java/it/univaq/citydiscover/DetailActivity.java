package it.univaq.citydiscover;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;




import it.univaq.citydiscover.model.City;

public class DetailActivity extends AppCompatActivity {

    private City city;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        try {
            city = (City) getIntent().getSerializableExtra("city");
            setupTextView();
        }catch(Exception e){
            e.printStackTrace();
            onBackPressed();  //serve per tornare indietro sulla schermata precedente se avvengono errori
        }

        Button button= findViewById(R.id.backButton);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setupTextView(){
        ((TextView) findViewById(R.id.textTitle)).setText(city.getName().toUpperCase());
        ((TextView) findViewById(R.id.textCode)).setText(String.valueOf("Codice: "+ city.getCode()));
        ((TextView) findViewById(R.id.textCadastralCode)).setText("Codice catastale: "+ city.getCadastralCode());
        ((TextView) findViewById(R.id.textPrefix)).setText("Prefisso telefonico: "+ city.getPrefix());
    }



}
