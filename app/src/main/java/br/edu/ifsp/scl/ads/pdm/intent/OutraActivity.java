package br.edu.ifsp.scl.ads.pdm.intent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import br.edu.ifsp.scl.ads.pdm.intent.databinding.ActivityMainBinding;
import br.edu.ifsp.scl.ads.pdm.intent.databinding.ActivityOutraBinding;

public class OutraActivity extends AppCompatActivity {
    //Instância da classe de View Binding
    private ActivityOutraBinding activityOutraBinding;

    //Constatne para retorno para a MainActivity
    public static final String RETORNO = "RETORNO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOutraBinding = ActivityOutraBinding.inflate(getLayoutInflater());
        setContentView(activityOutraBinding.getRoot());

        //Recebendo parâmetros pela forma #1
        /*Bundle parametrosBundle = getIntent().getExtras();
        if(parametrosBundle != null){
            String parametro = parametrosBundle.getString(MainActivity.PARAMETRO, "");
            activityOutraBinding.recebidoTv.setText(parametro);
        }*/

        //Forma #2 de passagem de parâmetros
        String parametro = getIntent().getStringExtra(MainActivity.PARAMETRO);
        if(parametro != null){
            activityOutraBinding.recebidoTv.setText(parametro);
        }

        Log.v(getString(R.string.app_name) + "/" + getLocalClassName(), "onCreate: Iniciando ciclo completo");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(getString(R.string.app_name) + "/" + getLocalClassName(), "onStart: Iniciando ciclo visível");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(getString(R.string.app_name) + "/" + getLocalClassName(), "onResume: Iniciando ciclo foreground");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(getString(R.string.app_name) + "/" + getLocalClassName(), "onPause: Finalizando ciclo foreground");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(getString(R.string.app_name) + "/" + getLocalClassName(), "onStop: Finalizando ciclo visível");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(getString(R.string.app_name) + "/" + getLocalClassName(), "onDestroy: Finalizando ciclo completo");
    }

    public void onClick(View view) {
        // Retorno para a MainActivity
        Intent retornoIntent = new Intent();
        retornoIntent.putExtra(RETORNO, activityOutraBinding.retornoEt.getText().toString());
        setResult(RESULT_OK, retornoIntent);

        finish(); // Chama na sequência onPause, onStop, onDestroy
    }
}