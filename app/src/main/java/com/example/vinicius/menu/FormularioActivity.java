package com.example.vinicius.menu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vinicius.menu.DAO.EstacionamentoDAO;
import com.example.vinicius.menu.helper.*;
import com.example.vinicius.menu.modelo.Veiculo;

import java.io.File;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class FormularioActivity extends AppCompatActivity {
    FormularioHelper f;
    ImageView imageView;
    String localArquivoFoto;
    private static final int TIRA_FOTO = 123;
    Veiculo a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario);
        Button btnScan = (Button) findViewById(R.id.scan);
        final Activity activity = this;
        EditText placa = (EditText) findViewById(R.id.editText5);
        placa.addTextChangedListener(MaskEditUtil.mask(placa, MaskEditUtil.FORMAT_PLACA));
        EditText telefone = (EditText) findViewById(R.id.editText2);
        telefone.addTextChangedListener(MaskEditUtil.mask(telefone, MaskEditUtil.FORMAT_FONE));

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Camera Scan");
                integrator.setCameraId(0);
                integrator.initiateScan();
            }
        });

        a = new Veiculo();
        //Bundle intent = getIntent().getExtras();
        a = (Veiculo) getIntent().getSerializableExtra("veiculo");
        Button botao = (Button) findViewById(R.id.button);
        f = new FormularioHelper(this);
        if(a == null) {

            botao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Veiculo veiculo = new Veiculo();
                    veiculo = f.pegaVeiculoDoFormulario();
                    EstacionamentoDAO dao = new EstacionamentoDAO(FormularioActivity.this);
                    dao.insere(veiculo);
                    dao.close();
                    finish();
                }
            });
        }else{
            f.colocaVeiculoDoFormulario(a);
            botao.setText("Alterar");
            botao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Veiculo veiculo;
                    veiculo = f.pegaVeiculoDoFormulario();
                    EstacionamentoDAO dao = new EstacionamentoDAO(FormularioActivity.this);
                    veiculo.setId(a.getId());
                    dao.update(veiculo);
                    dao.close();
                    finish();
                }
            });
        }
        ImageView foto = f.getFoto();
        foto.setRotation(270);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                localArquivoFoto = getExternalFilesDir(null)+"/"+System.currentTimeMillis()+".jpg";
                Uri localFoto = Uri.fromFile(new File(localArquivoFoto));
                Intent irParaCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                irParaCamera.putExtra(MediaStore.EXTRA_OUTPUT, localFoto);
                startActivityForResult(irParaCamera, 123);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if(requestCode == TIRA_FOTO){
            if(resultCode == Activity.RESULT_OK){
                f.carregaImagem(this.localArquivoFoto);
            }else{
                this.localArquivoFoto = null;
            }
        }else{
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
            if(result != null){
                EditText password = (EditText) findViewById(R.id.editText6);
                if (result.getContents() !=  null){
                    password.setText(result.getContents());
                }else{
                    Toast.makeText(FormularioActivity.this, "Scan cancelado", Toast.LENGTH_SHORT).show();
                }
            }else{
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


}
