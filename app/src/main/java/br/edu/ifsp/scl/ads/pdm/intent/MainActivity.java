package br.edu.ifsp.scl.ads.pdm.intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import br.edu.ifsp.scl.ads.pdm.intent.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    // Instância da classe de View Binding
    private ActivityMainBinding activityMainBinding;

    // Constante para passagem de parâmetro e retorno
    public static final String PARAMETRO = "PARAMETRO";

    // Resquest code para OutraAcitivity
    public static final int OUTRA_ACITIVITY_REQUEST_CODE = 0;

    // Request code para permissao de callphone
    private final int CALL_PHONE_PERMISSION_REQUEST_CODE = 1;

    // Request code para pegar arquivo de imagem
    private final int PICK_IMAGE_FILE_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());

        getSupportActionBar().setTitle("Tratando Intents");
        getSupportActionBar().setSubtitle("Tem subtitulo também");

        setContentView(activityMainBinding.getRoot());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.outraActivityMi:
                //Abrir OutraActivity
                //Intent outraActivityIntent = new Intent(this, OutraActivity.class);
                Intent outraActivityIntent = new Intent("RECEBER_E_RETORNAR_ACTION");

                //Forma #1 de passagem de parâmetros
                /*Bundle parametrosBundle = new Bundle();
                parametrosBundle.putString(PARAMETRO, activityMainBinding.parametroEt.getText().toString());
                outraActivityIntent.putExtras(parametrosBundle);*/

                //Forma #2 de passagem de parâmetros
                outraActivityIntent.putExtra(PARAMETRO, activityMainBinding.parametroEt.getText().toString());

                startActivityForResult(outraActivityIntent, OUTRA_ACITIVITY_REQUEST_CODE);
                return true;
            case R.id.viewMi:
                // Abrindo navegador
                Intent abrirNavegadorIntent = new Intent(Intent.ACTION_VIEW);
                abrirNavegadorIntent.setData(Uri.parse(activityMainBinding.parametroEt.getText().toString()));
                startActivity(abrirNavegadorIntent);

                return true;
            case R.id.callMi:
                // Fazendo uma ligação
                verifyCallPhonePermission();
                return true;
            case R.id.dialMi:
                // Abri o discador
                Intent discarIntent = new Intent(Intent.ACTION_DIAL);
                discarIntent.setData(Uri.parse("tel:" + activityMainBinding.parametroEt.getText().toString()));
                startActivity(discarIntent);
                return true;
            case R.id.pickMi:
                startActivityForResult(getPickImageIntent(), PICK_IMAGE_FILE_REQUEST_CODE);
                return true;
            case R.id.chooserMi:
                //Força que o usuário escolha entre uma lista de aplicativos MESMO QUE EXISTA um app padrão
                Intent escolherActivityIntent = new Intent(Intent.ACTION_CHOOSER);
                escolherActivityIntent.putExtra(Intent.EXTRA_INTENT, getPickImageIntent());
                escolherActivityIntent.putExtra(Intent.EXTRA_TITLE, "Escolha um app para selecionar imagens");
                startActivityForResult(escolherActivityIntent, PICK_IMAGE_FILE_REQUEST_CODE);
                return true;
        }
        return false;
    }

    private Intent getPickImageIntent(){
        Intent pegarImagemIntent = new Intent(Intent.ACTION_PICK);
        String diretiorioImagens = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
        pegarImagemIntent.setDataAndType(Uri.parse(diretiorioImagens), "image/*");

        return pegarImagemIntent;
    }

    private void verifyCallPhonePermission() {
        Intent ligarIntent = new Intent(Intent.ACTION_CALL);
        ligarIntent.setData(Uri.parse("tel:" + activityMainBinding.parametroEt.getText().toString()));
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                // Usuário ja concedeu a permissão
                startActivity(ligarIntent);
            }
            else {
                // Solicitar permissão para o usuário em tempo de execução
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_PERMISSION_REQUEST_CODE);

            }
        }else{
            // A permissão foi solicitada no Manifest (Android < M)
            startActivity(ligarIntent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if( requestCode == CALL_PHONE_PERMISSION_REQUEST_CODE){
            if(permissions[0].equals(Manifest.permission.CALL_PHONE) && grantResults[0] != PackageManager.PERMISSION_GRANTED){
                //Usuario deu permissao
                Toast.makeText(this, "Permissão de ligação necessario para essa funcionalidade!", Toast.LENGTH_SHORT).show();
            }
            verifyCallPhonePermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OUTRA_ACITIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            //Recebendo retorno da OutraActivity
            Toast.makeText(this, "Voltou para MainAcitivity", Toast.LENGTH_SHORT).show();

            String retorno = data.getStringExtra(OutraActivity.RETORNO);
            if( retorno != null ){
                activityMainBinding.retornoTv.setText(retorno);
            }
        }else{
            //Recebendo retorno da Activity de pegar imagem
            if(requestCode == PICK_IMAGE_FILE_REQUEST_CODE && resultCode == RESULT_OK){
                Uri imagemUri = data.getData();
                Toast.makeText(this, imagemUri.toString(), Toast.LENGTH_SHORT).show();

                // Visualizando imagem
                Intent visualziarImagem = new Intent(Intent.ACTION_VIEW, imagemUri);
                startActivity(visualziarImagem);
            }
        }
    }
}