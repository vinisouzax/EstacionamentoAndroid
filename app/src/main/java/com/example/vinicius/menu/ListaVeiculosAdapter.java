package com.example.vinicius.menu;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vinicius.menu.modelo.Veiculo;

import java.util.List;

public class ListaVeiculosAdapter extends BaseAdapter{
    private final List<Veiculo> veiculos;
    final Activity activity;

    public ListaVeiculosAdapter(List<Veiculo> veiculos, Activity activity) {
        this.veiculos = veiculos;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return veiculos.size();
    }

    @Override
    public Object getItem(int i) {
        return veiculos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return veiculos.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = activity.getLayoutInflater().inflate(R.layout.item, null);
        Veiculo veiculo = veiculos.get(i);
        TextView nome = v.findViewById(R.id.textItem);
        nome.setText(veiculo.getPlaca());
        /*Bitmap imagemFoto = null;
        if(veiculo.getCaminhoFoto() != null){
            ImageView foto = v.findViewById(R.id.imageItem);
            imagemFoto = BitmapFactory.decodeFile(veiculo.getCaminhoFoto());
            Bitmap imagemFotoReduzida = Bitmap.createScaledBitmap(imagemFoto, 100, 100, true);
            foto.setImageBitmap(imagemFotoReduzida);
        }*/
        TextView status = v.findViewById(R.id.textItem1);
        status.setText(veiculo.getStatus());
        return v;
    }
}
