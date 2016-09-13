package com.example.androidexperimentfive;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpenHelper extends SQLiteOpenHelper {

	public static String name = "ExperimentFive.db";
	CursorFactory factory;
	@SuppressWarnings("unused")
	private static int version = 1;

	public DbOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public void onCreate(SQLiteDatabase db) {
		String sql = "create table people(_id integer primary key,"
				+ "name varchar(64),age integer,height float)";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
	
	//��ɾ�Ĳ���
	public boolean mulOperation(String sql){
		SQLiteDatabase db=null;
    	try{
			db=this.getWritableDatabase();//ʵ�ֶ����ݿ�Ĳ������
			db.execSQL(sql);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}finally{
			if(db!=null)
				db.close();
		}
	}
	
	//��ѯ����
	public Cursor select(String sql){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(sql, null);
	}

}
