package com.appvn.ketquaxoso.database;

import java.util.ArrayList;

import com.appvn.ketquaxoso.model.ItemNewFeed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "KetQua";
	private static final String DATABASE = "KetQua.db";

	public DatabaseHelper(Context context) {
		super(context, DATABASE, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_TABLE = "CREATE TABLE "
				+ TABLE_NAME
				+ " (id TEXT PRIMARY KEY , message TEXT, image TEXT, time TEXT)";
		db.execSQL(CREATE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

	public void addBook(ItemNewFeed item, String time) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id", item.getPost_id());
		values.put("message", item.getMessage());
		values.put("image", item.getImageBig());
		values.put("time", time);
		db.insert(TABLE_NAME, null, values);
		db.close();
	}

	public void deleteAll() {
		SQLiteDatabase db = getReadableDatabase();
		db.delete(TABLE_NAME, null, null);
		db.close();
	}

	public ArrayList<ItemNewFeed> getSearchData(ArrayList<ItemNewFeed> list,
			String time) {
		SQLiteDatabase db = getReadableDatabase();
		String SQL = "SELECT * FROM " + TABLE_NAME + " WHERE time='" + time
				+ "'";
		// db.execSQL(SQL);
		Cursor cursor = db.rawQuery(SQL, null);
		ItemNewFeed item = null;
		while (cursor.moveToNext()) {
			item = new ItemNewFeed();
			item.setPost_id(cursor.getString(cursor.getColumnIndex("id")));
			item.setImageBig(cursor.getString(cursor.getColumnIndex("image")));
			item.setTime(cursor.getString(cursor.getColumnIndex("time")));
			item.setMessage(cursor.getString(cursor.getColumnIndex("message")));
			list.add(item);

		}
		return list;
	}

}
