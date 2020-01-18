package com.example.vinicius.menu.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vinicius.menu.modelo.Veiculo;

import java.util.ArrayList;
import java.util.List;

public class EstacionamentoDAO extends SQLiteOpenHelper {
    private static final int VERSAO = 5;
    private static final String TABELA = "Alunos";
    private static final String DATABASE = "CadastroDB";

    public EstacionamentoDAO(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String dd1 = "CREATE TABLE "+ TABELA + " (id INTEGER PRIMARY KEY, " + " nome TEXT UNIQUE NOT NULL, telefone TEXT, veiculo TEXT, " + "modelo TEXT, placa TEXT, status TEXT, password TEXT, caminhoFoto TEXT);";
        database.execSQL(dd1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int versaoAntiga, int versaoNova) {
        String sql = "DROP TABLE IF EXISTS "+TABELA;
        database.execSQL(sql);
        onCreate(database);
    }

    public void insere(Veiculo veiculo){
        ContentValues values = new ContentValues();
        values.put("nome", veiculo.getNome());
        values.put("telefone", veiculo.getTelefone());
        values.put("veiculo", veiculo.getVeiculo());
        values.put("modelo", veiculo.getModelo());
        values.put("placa", veiculo.getPlaca());
        values.put("caminhoFoto", veiculo.getCaminhoFoto());
        values.put("status", "Não Estacionado");
        values.put("password", veiculo.getPassword());

        getReadableDatabase().insert(TABELA, null, values);
    }
    public void update(Veiculo veiculo){
        ContentValues values = new ContentValues();
        values.put("nome", veiculo.getNome());
        values.put("telefone", veiculo.getTelefone());
        values.put("veiculo", veiculo.getVeiculo());
        values.put("modelo", veiculo.getModelo());
        values.put("placa", veiculo.getPlaca());
        values.put("caminhoFoto", veiculo.getCaminhoFoto());
        values.put("status", veiculo.getStatus());
        values.put("password", veiculo.getPassword());
        long id = veiculo.getId();
        //getReadableDatabase().update(TABELA, values, "id="+ game.getId(), null);
        getWritableDatabase().update(TABELA, values, "id=?", new String[] {veiculo.getId().toString()});
    }

    public void delete(long id){
        getReadableDatabase().delete(TABELA, "id=" + id, null);
    }

    public void estacionar(Veiculo veiculo){
        ContentValues values = new ContentValues();
        values.put("nome", veiculo.getNome());
        values.put("telefone", veiculo.getTelefone());
        values.put("veiculo", veiculo.getVeiculo());
        values.put("modelo", veiculo.getModelo());
        values.put("placa", veiculo.getPlaca());
        values.put("caminhoFoto", veiculo.getCaminhoFoto());
        values.put("status", "Estacionado");
        values.put("password", veiculo.getPassword());
        long id = veiculo.getId();
        getWritableDatabase().update(TABELA, values, "id=?", new String[] {veiculo.getId().toString()});
    }

    public void saiu(Veiculo veiculo){
        ContentValues values = new ContentValues();
        values.put("nome", veiculo.getNome());
        values.put("telefone", veiculo.getTelefone());
        values.put("veiculo", veiculo.getVeiculo());
        values.put("modelo", veiculo.getModelo());
        values.put("placa", veiculo.getPlaca());
        values.put("caminhoFoto", veiculo.getCaminhoFoto());
        values.put("status", "Não Estacionado");
        values.put("password", veiculo.getPassword());
        long id = veiculo.getId();
        getWritableDatabase().update(TABELA, values, "id=?", new String[] {veiculo.getId().toString()});
    }

    public int getEstacionados(){
        List<Veiculo> veiculos = new ArrayList<>();

        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM "+TABELA+" WHERE status='Estacionado';",null);

        while(c.moveToNext()){
            Veiculo veiculo = new Veiculo();
            veiculo.setId(c.getLong(c.getColumnIndex("id")));
            veiculo.setNome(c.getString(c.getColumnIndex("nome")));
            veiculo.setTelefone(c.getString(c.getColumnIndex("telefone")));
            veiculo.setVeiculo(c.getString(c.getColumnIndex("veiculo")));
            veiculo.setModelo(c.getString(c.getColumnIndex("modelo")));
            veiculo.setPlaca(c.getString(c.getColumnIndex("placa")));
            veiculo.setStatus(c.getString(c.getColumnIndex("status")));
            veiculo.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));
            veiculo.setPassword(c.getString(c.getColumnIndex("password")));
            veiculos.add(veiculo);
        }
        c.close();
        return veiculos.size();
    }

    public Veiculo getVeiculo(long id){
        List<Veiculo> veiculos = new ArrayList<>();

        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM "+TABELA+" WHERE id="+id+";",null);


        while(c.moveToNext()){
            Veiculo veiculo = new Veiculo();
            veiculo.setId(c.getLong(c.getColumnIndex("id")));
            veiculo.setNome(c.getString(c.getColumnIndex("nome")));
            veiculo.setTelefone(c.getString(c.getColumnIndex("telefone")));
            veiculo.setVeiculo(c.getString(c.getColumnIndex("veiculo")));
            veiculo.setModelo(c.getString(c.getColumnIndex("modelo")));
            veiculo.setPlaca(c.getString(c.getColumnIndex("placa")));
            veiculo.setStatus(c.getString(c.getColumnIndex("status")));
            veiculo.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));
            veiculo.setPassword(c.getString(c.getColumnIndex("password")));
            veiculos.add(veiculo);
        }
        c.close();
        return veiculos.get(0);
    }

    public Veiculo getVeiculoByPassword(String password){
        List<Veiculo> veiculos = new ArrayList<>();

        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM "+TABELA+" WHERE password='"+password+"';",null);


        while(c.moveToNext()){
            Veiculo veiculo = new Veiculo();
            veiculo.setId(c.getLong(c.getColumnIndex("id")));
            veiculo.setNome(c.getString(c.getColumnIndex("nome")));
            veiculo.setTelefone(c.getString(c.getColumnIndex("telefone")));
            veiculo.setVeiculo(c.getString(c.getColumnIndex("veiculo")));
            veiculo.setModelo(c.getString(c.getColumnIndex("modelo")));
            veiculo.setPlaca(c.getString(c.getColumnIndex("placa")));
            veiculo.setStatus(c.getString(c.getColumnIndex("status")));
            veiculo.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));
            veiculo.setPassword(c.getString(c.getColumnIndex("password")));
            veiculos.add(veiculo);
        }
        c.close();
        return veiculos.get(0);
    }

    public List<Veiculo> getLista(){
        List<Veiculo> veiculos = new ArrayList<>();

        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM "+TABELA+" ORDER BY placa ASC;",null);

        while(c.moveToNext()){
            Veiculo veiculo = new Veiculo();
            veiculo.setId(c.getLong(c.getColumnIndex("id")));
            veiculo.setNome(c.getString(c.getColumnIndex("nome")));
            veiculo.setTelefone(c.getString(c.getColumnIndex("telefone")));
            veiculo.setVeiculo(c.getString(c.getColumnIndex("veiculo")));
            veiculo.setModelo(c.getString(c.getColumnIndex("modelo")));
            veiculo.setPlaca(c.getString(c.getColumnIndex("placa")));
            veiculo.setStatus(c.getString(c.getColumnIndex("status")));
            veiculo.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));
            veiculo.setPassword(c.getString(c.getColumnIndex("password")));
            veiculos.add(veiculo);
        }
        c.close();
        return veiculos;
    }

    public List<String> getTelefones(){
        List<String> telefones = new ArrayList<>();

        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM "+TABELA+" ORDER BY placa ASC;",null);

        while(c.moveToNext()){
            String telefone = new String();
            telefone = c.getString(c.getColumnIndex("telefone"));
            telefone += ";";
            telefones.add(telefone);
        }
        c.close();
        return telefones;
    }
}
