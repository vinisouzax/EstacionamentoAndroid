package com.example.vinicius.menu.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.vinicius.menu.FormularioActivity;
import com.example.vinicius.menu.R;

import com.example.vinicius.menu.modelo.Veiculo;

public class FormularioHelper {

    private Veiculo v;

    private EditText nome;
    private EditText telefone;
    private EditText veiculo;
    private EditText modelo;
    private EditText placa;
    private ImageView foto;
    private EditText password;

    public ImageView getFoto() {
        return foto;
    }

    public FormularioHelper(FormularioActivity activity){
        this.v = new Veiculo();
        this.nome = (EditText) activity.findViewById(R.id.editText);
        this.telefone = (EditText) activity.findViewById(R.id.editText2);
        this.veiculo = (EditText) activity.findViewById(R.id.editText3);
        this.modelo = (EditText) activity.findViewById(R.id.editText4);
        this.placa = (EditText) activity.findViewById(R.id.editText5);
        this.password = (EditText) activity.findViewById(R.id.editText6);
        this.foto = (ImageView) activity.findViewById(R.id.imageView);
    }
    public void carregaImagem(String localArquivoFoto){
        v.setCaminhoFoto(localArquivoFoto);
        Bitmap imagemFoto = BitmapFactory.decodeFile(localArquivoFoto);
        Bitmap imagemFotoReduzida = Bitmap.createScaledBitmap(imagemFoto, 100, 100, true);
        foto.setImageBitmap(imagemFotoReduzida);
    }
    public void colocaVeiculoDoFormulario(Veiculo a){
        nome.setText(a.getNome()+"");
        telefone.setText(a.getTelefone()+"");
        veiculo.setText(a.getVeiculo()+"");
        modelo.setText(a.getModelo()+"");
        placa.setText(a.getPlaca()+"");
        password.setText(a.getPassword()+"");

        if(a.getCaminhoFoto() != null){
            Bitmap imagemFoto = BitmapFactory.decodeFile(a.getCaminhoFoto());
            Bitmap imagemFotoReduzida = Bitmap.createScaledBitmap(imagemFoto, 100, 100, true);
            foto.setImageBitmap(imagemFotoReduzida);
        }
        this.v = a;
    }

    public Veiculo pegaVeiculoDoFormulario(){
        v.setNome(nome.getText().toString());
        v.setTelefone(telefone.getText().toString());
        v.setVeiculo(veiculo.getText().toString());
        v.setModelo(modelo.getText().toString());
        v.setPlaca(placa.getText().toString());
        v.setPassword(password.getText().toString());

        return v;
    }

}
