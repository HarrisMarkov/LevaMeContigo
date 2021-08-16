package pt.ubi.di.levamecontigo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class databaseHelper extends  SQLiteOpenHelper{

    public static final String db_name = "LevaMeContigo.db";

    // Boleia -> ID, categoria, data, hora, origem, destino, contribuição, lugares, deu, pediu
    public static final String boleia = "Boleia";
    public static final String boleia_id = "ID";
    public static final String boleia_categoria = "Categoria";
    public static final String boleia_user_deu = "Oferece";
    public static final String boleia_user_pediu = "Pede";
    public static final String boleia_data = "Data";
    public static final String boleia_hora = "Hora";
    public static final String boleia_origem = "Origem";
    public static final String boleia_destino = "Destino";
    public static final String boleia_lugares = "Lugares";
    public static final String boleia_contribuicao = "Contribuicao";

    public static final String user = "User";
    public static final String user_id = "ID";
    public static final String user_nome = "Nome";
    public static final String user_email = "Email";
    public static final String user_pass = "Pass";
    public static final String user_ofertas = "Ofertas";
    public static final String user_pediu = "Pedidos";
    public static final String user_rating = "Rating";


    public databaseHelper(Context context) {
        super(context, db_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Boleia (ID INTEGER PRIMARY KEY AUTOINCREMENT, Categoria TEXT, Oferece TEXT, Pede TEXT, Data TEXT, Hora TEXT, Origem TEXT, Destino TEXT, Lugares INTEGER, Contribuicao REAL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS User (ID INTEGER PRIMARY KEY AUTOINCREMENT, Nome TEXT, Email TEXT, Pass TEXT, Ofertas INTEGER, Pedidos INTEGER, Rating REAL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Pendente (ID INTEGER PRIMARY KEY AUTOINCREMENT, BoleiaID INTEGER, Pede TEXT, Confirma TEXT, Data TEXT, Hora TEXT, Origem TEXT, Destino TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public void onUpgrade(SQLiteDatabase db, int i, int i1, String table_name) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
        onCreate(db);
    }

    public long addPendente(int id, String pede, String confirma, String data, String hora, String origem, String destino){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("BoleiaID", id);
        contentValues.put("Pede", pede);
        contentValues.put("Confirma", confirma);
        contentValues.put("Data", data);
        contentValues.put("Hora", hora);
        contentValues.put("Origem", origem);
        contentValues.put("Destino", destino);
        long res = db.insert("Pendente",null, contentValues);
        db.close();
        return res;
    }

    public Cursor checkPendentes(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Pendente WHERE Confirma=?", new String[] {email});
        return cursor;
    }

    public Boolean deletePendente(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Pendente WHERE ID = '" + id + "'");
        return true;
    }

    public long addBoleia(String categoria, String nome, String data, String hora, String origem, String destino, String lugares, float contribuicao){
        SQLiteDatabase db = this.getWritableDatabase();
        Date currentTime = Calendar.getInstance().getTime();
        //String date = currentTime.toString();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Categoria", categoria);
        if(categoria.equalsIgnoreCase("oferta"))
            contentValues.put("Oferece", nome);
        else contentValues.put("pede", nome);

        contentValues.put("Data", data);
        contentValues.put("Hora", hora);
        contentValues.put("Origem", origem);
        contentValues.put("Destino", destino);
        contentValues.put("Lugares", lugares);
        contentValues.put("Contribuicao", contribuicao);
        long res = db.insert("Boleia",null, contentValues);
        db.close();
        return res;
    }

    public Cursor getAllBoleias(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Boleia ORDER BY Data, Hora ASC", null);
        return cursor;
    }

    public Cursor getBoleiasCombinadasOferece(String user){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Boleia WHERE Oferece = '" + user + "' AND Pede IS NOT NULL", null);
        return cursor;
    }

    public Cursor getBoleiasCombinadasPede(String user){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Boleia WHERE Pede = '" + user + "' AND Oferece IS NOT NULL", null);
        return cursor;
    }

    public Cursor getAllBoleiasNextDays(){
        SQLiteDatabase db = this.getWritableDatabase();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String str_today = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DATE, 1);
        String str_tomorrow = dateFormat.format(calendar.getTime());
        Cursor cursor = db.rawQuery("SELECT * FROM Boleia WHERE Data >= " + str_today, null);

        return cursor;
    }

    public Boolean updateBoleia(int id, String oferece, String pede){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE Boleia SET Pede = '" + pede + "' WHERE ID = '" + id + "'");
        return true;
    }

    public Boolean deleteBoleia(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Boleia WHERE ID = '" + id + "'");
        return true;
    }

    public long addUser(String email, String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Nome", "Username");
        contentValues.put("Email", email);
        contentValues.put("Pass", pass);
        contentValues.put("Ofertas", 0);
        contentValues.put("Pedidos", 0);
        contentValues.put("Rating", 0.0);
        long res = db.insert("User", null, contentValues);
        db.close();
        return res;
    }

    public Cursor getAllUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User", null);
        return cursor;
    }

    public Cursor getUser(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE Email=?", new String[] {email});
        return cursor;
    }

    public void deleteNullUsers(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("DELETE FROM User WHERE Email IS NULL", null);
    }

    public Boolean updateUser(String name, String email, String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Nome", name);
        contentValues.put("Pass", pass);
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE Email = ?", new String[] {email});
        if(cursor.getCount() > 0){
            long res = db.update("User", contentValues, "Email=?", new String[] {email});
            if(res == -1)
                return false;
            else return true;
        }
        else return false;
    }

    public Boolean deleteUser(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM User WHERE Email = ?", new String[] {email});
        if(cursor.getCount() > 0){
            long res = db.delete("User","Email=?", new String[] {email});
            if(res == -1)
                return false;
            else return true;
        }
        else return false;
    }

    public boolean checkUser(String email, String pass){
        String[] columns = { user_id };
        SQLiteDatabase db = getWritableDatabase();
        String selection = user_email + "=?" + " and " + user_pass + "=?";
        String[] selectionArgs = { email, pass };
        Cursor cursor = db.query(user, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if(count > 0)
            return true;
        else return false;
    }

    public boolean checkUser(String email){
        String[] columns = { user_id };
        SQLiteDatabase db = getWritableDatabase();
        String selection = user_email + "=?";
        String[] selectionArgs = { email};
        Cursor cursor = db.query(user, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if(count > 0)
            return true;
        else return false;
    }
}
