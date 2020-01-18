package com.example.vinicius.menu;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinicius.menu.DAO.EstacionamentoDAO;
import com.example.vinicius.menu.modelo.Veiculo;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;

public class ListaVeiculosActivity extends AppCompatActivity {
    private ListView listaVeiculos;
    private Veiculo veiculoSelecionado;
    private int max = 50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listagem_alunos);
        carregaLista();
        registerForContextMenu(listaVeiculos);
        final EstacionamentoDAO dao = new EstacionamentoDAO(this);
        int qtd = dao.getEstacionados();
        TextView qtdText = (TextView) findViewById(R.id.textView);
        qtdText.setText("Veículos estacionados: "+qtd);
        dao.close();

        listaVeiculos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                veiculoSelecionado = (Veiculo) adapterView.getItemAtPosition(i);
                return false;
            }
        });

        Button btnScan = (Button) findViewById(R.id.button2);
        final Activity activity = this;

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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            if (result.getContents() !=  null){
                EstacionamentoDAO dao = new EstacionamentoDAO(ListaVeiculosActivity.this);
                int qtd = dao.getEstacionados();
                TextView qtdText = (TextView) findViewById(R.id.textView);
                veiculoSelecionado = dao.getVeiculoByPassword(result.getContents().toString());
                if(qtd < max && veiculoSelecionado.getStatus().equals("Não Estacionado")) {
                    dao.estacionar(veiculoSelecionado);
                    qtdText.setText("Carros estacionados: "+(qtd+1));
                }else if(veiculoSelecionado.getStatus().equals("Estacionado")){
                    dao.saiu(veiculoSelecionado);
                    qtdText.setText("Veículos estacionados: "+(qtd-1));
                }else{
                    Toast.makeText(ListaVeiculosActivity.this, "Estacionamento Lotado", Toast.LENGTH_LONG).show();
                }
                dao.close();
                onRestart();
            }else{
                Toast.makeText(ListaVeiculosActivity.this, "Ocorreu um erro no scan", Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        MenuItem ligar = menu.add("Ligar");
        MenuItem estacionado = menu.add("Estacionar");
        MenuItem saiu = menu.add("Desestacionar");
        MenuItem sms = menu.add("SMS");
        MenuItem alterar = menu.add("Alterar");
        MenuItem deletar = menu.add("Deletar");

        alterar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                EstacionamentoDAO dao = new EstacionamentoDAO(ListaVeiculosActivity.this);
                Veiculo a = dao.getVeiculo(veiculoSelecionado.getId());
                Intent it = new Intent(ListaVeiculosActivity.this, FormularioActivity.class);
                it.putExtra("veiculo", (Veiculo) a);
                startActivity(it);
                return false;
            }
        });

        estacionado.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                EstacionamentoDAO dao = new EstacionamentoDAO(ListaVeiculosActivity.this);
                int qtd = dao.getEstacionados();
                TextView qtdText = (TextView) findViewById(R.id.textView);
                if(qtd < max && !veiculoSelecionado.getStatus().equals("Estacionado")) {
                    dao.estacionar(veiculoSelecionado);
                    qtdText.setText("Carros estacionados: "+(qtd+1));
                }else{
                    Toast.makeText(ListaVeiculosActivity.this, "Estacionamento Lotado ou Carro já estacionado", Toast.LENGTH_LONG).show();
                }
                dao.close();
                onRestart();
                return false;
            }
        });

        saiu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                EstacionamentoDAO dao = new EstacionamentoDAO(ListaVeiculosActivity.this);
                dao.saiu(veiculoSelecionado);
                int qtd = dao.getEstacionados();
                TextView qtdText = (TextView) findViewById(R.id.textView);
                qtdText.setText("Carros estacionados: "+qtd);
                dao.close();
                onRestart();
                return false;
            }
        });

        Intent intentLigar = new Intent(Intent.ACTION_DIAL);
        intentLigar.setData(Uri.parse("tel:"+ veiculoSelecionado.getTelefone()));
        ligar.setIntent(intentLigar);

        Intent intentSms = new Intent(Intent.ACTION_VIEW);
        intentSms.setData(Uri.parse("sms:"+veiculoSelecionado.getTelefone()));
        intentSms.putExtra("sms_body", "");
        sms.setIntent(intentSms);


        deletar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                new AlertDialog.Builder(ListaVeiculosActivity.this)
                        .setTitle("Deletar")
                        .setMessage("Deseja mesmo deletar")
                        .setPositiveButton("Quero", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EstacionamentoDAO dao = new EstacionamentoDAO(ListaVeiculosActivity.this);
                                dao.delete(veiculoSelecionado.getId());
                                dao.close();
                                onRestart();
                            }
                        }).setNegativeButton("Não", null).show();
                return false;

            }
        });

        super.onCreateContextMenu(menu, v, menuInfo);
    }


    @Override
    protected void onResume() {
        super.onResume();
        carregaLista();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        carregaLista();
    }

    public void carregaLista(){
        EstacionamentoDAO dao = new EstacionamentoDAO(this);
        List<Veiculo> veiculos = dao.getLista();
        dao.close();
        ListaVeiculosAdapter adapter2 = new ListaVeiculosAdapter(veiculos, this);
        //int layout = android.R.layout.simple_list_item_1;
        //final ArrayAdapter<Veiculo> adapter = new ArrayAdapter<>(ListaVeiculosActivity.this, layout, veiculos);
        listaVeiculos = (ListView) findViewById(R.id.lista_alunos);
        listaVeiculos.setAdapter(adapter2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_example, menu); // set your file name
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.cadastro:
                Intent intent = new Intent(this, FormularioActivity.class);
                startActivity(intent);
                return false;
            case R.id.smsgeral:
                EstacionamentoDAO dao = new EstacionamentoDAO(this);
                List<String> telefones = dao.getTelefones();
                Intent intentSms = new Intent(Intent.ACTION_VIEW);
                intentSms.setData(Uri.parse("sms:"+telefones));
                intentSms.putExtra("sms_body", "");
                startActivity(intentSms);
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
