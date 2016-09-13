package com.example.androidexperimentfive;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private DbOpenHelper helper = null;
	private EditText name_et;
	private EditText age_et;
	private EditText height_et;
	private EditText id_et;
	private Button add_bt;
	private Button show_all_bt;
	private Button clear_show_bt;
	private Button delete_all_bt;
	private Button id_search_bt;
	private Button id_delete_bt;
	private Button id_update_bt;
	private TextView sqlArea;
	private static int id;
	private int name_etleft, name_ettop, name_et_bottom, name_et_right;
	private int age_etleft, age_ettop, age_et_bottom, age_et_right;
	private int height_etleft, height_ettop, height_et_bottom, height_et_right;
	private int id_etleft, id_ettop, id_et_bottom, id_et_right;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		helper = new DbOpenHelper(getApplicationContext(), DbOpenHelper.name,
				null, 1);
		getId_num();
		initView();
	}

	public void initView() {
		name_et = (EditText) findViewById(R.id.enterName);
		age_et = (EditText) findViewById(R.id.enterAge);
		height_et = (EditText) findViewById(R.id.enterHei);
		id_et = (EditText) findViewById(R.id.enterId);

		sqlArea = (TextView) findViewById(R.id.sqlArea);

		add_bt = (Button) findViewById(R.id.add);
		show_all_bt = (Button) findViewById(R.id.showAll);
		clear_show_bt = (Button) findViewById(R.id.clearShow);
		delete_all_bt = (Button) findViewById(R.id.deleteAll);
		id_search_bt = (Button) findViewById(R.id.ID_Search);
		id_delete_bt = (Button) findViewById(R.id.ID_Delete);
		id_update_bt = (Button) findViewById(R.id.ID_Update);

		add_bt.setOnClickListener(this);
		show_all_bt.setOnClickListener(this);
		clear_show_bt.setOnClickListener(this);
		delete_all_bt.setOnClickListener(this);
		id_search_bt.setOnClickListener(this);
		id_delete_bt.setOnClickListener(this);
		id_update_bt.setOnClickListener(this);

	}

	public void onClick(View v) {
		String sql = null;
		String name = null;
		String age = null;
		String height = null;
		String id_s = null;
		Cursor cursor;
		int id_us;
		boolean flag = false;
		String result = "���\t\t\t����\t\t\t����\t\t\t���\n";
		
		View vi = getCurrentFocus();
		if (vi instanceof EditText) {
			InputMethodManager imm = (InputMethodManager) 
					getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null) {
				imm.hideSoftInputFromWindow(vi.getWindowToken(), 0);
				vi.clearFocus();
			}
		}

		switch (v.getId()) {
		case R.id.add:
			if (name_et.getText().toString().isEmpty()
					|| age_et.getText().toString().isEmpty()
					|| height_et.getText().toString().isEmpty()) {
				printToast("���������������Ϣ����в��������");
			} else {
				name = name_et.getText().toString();
				age = age_et.getText().toString();
				height = height_et.getText().toString();
				id++;
				sql = "insert into people(_id,name,age,height) values("
						+ String.valueOf(id) + ",'" + name + "','" + age
						+ "','" + height + "')";
				flag = helper.mulOperation(sql);
				if (flag)
					printToast("�ѳɹ��������ݣ�");
				else
					printToast("�������ݳ��ִ���");
			}
			break;
		case R.id.showAll:
			sql = "select * from people";
			cursor = helper.select(sql);
			while (cursor.moveToNext()) {
				String result_id = cursor.getString(
						cursor.getColumnIndex("_id")).toString();
				String result_name = cursor.getString(
						cursor.getColumnIndex("name")).toString();
				String result_age = cursor.getString(
						cursor.getColumnIndex("age")).toString();
				String result_height = cursor.getString(
						cursor.getColumnIndex("height")).toString();
				result += result_id + "\t\t\t\t\t\t\t\t" + result_name
						+ "\t\t\t\t" + result_age + "\t\t\t\t\t\t"
						+ result_height + "\n";
			}
			cursor.close();
			sqlArea.setText(result);
			result = "���\t\t\t����\t\t\t����\t\t\t���\n";
			break;
		case R.id.clearShow:
			sqlArea.setText("");
			sqlArea.requestFocus();
			break;
		case R.id.deleteAll:
			sql = "delete from people";
			flag = helper.mulOperation(sql);
			if (flag) {
				printToast("�ѳɹ�ɾ�����ݣ�");
				id = 0;
			} else
				printToast("ɾ�����ݳ��ִ���");
			break;
		case R.id.ID_Delete:
			id_s = id_et.getText().toString();
			if (id_s.isEmpty())
				printToast("������Ҫ������id�����ɾ����");
			else {
				id_us = Integer.parseInt(id_s);
				id_us++;
				sql = "delete from people where _id=" + id_s;
				helper.mulOperation(sql);
				id--;
				sql = "select * from people";
				cursor = helper.select(sql);
				flag = true;
				while (cursor.moveToNext()) {
					if (cursor.getInt(cursor.getColumnIndex("_id")) >= id_us) {
						sql = "update people set _id=_id-1 where _id="
								+ cursor.getString(cursor.getColumnIndex("_id"));
						flag = helper.mulOperation(sql);
					}
					if (!flag)
						break;
				}
				cursor.close();
				if (flag)
					printToast("�ѳɹ�ɾ�����ݣ�");
				else
					printToast("ɾ�����ݳ��ִ���");
			}
			break;
		case R.id.ID_Search:
			id_s = id_et.getText().toString();
			if (id_s.isEmpty())
				printToast("������Ҫ������id����в�ѯ��");
			else {
				sql = "select * from people where _id=" + id_s;
				cursor = helper.select(sql);
				while (cursor.moveToNext()) {
					String result_id = cursor.getString(
							cursor.getColumnIndex("_id")).toString();
					String result_name = cursor.getString(
							cursor.getColumnIndex("name")).toString();
					String result_age = cursor.getString(
							cursor.getColumnIndex("age")).toString();
					String result_height = cursor.getString(
							cursor.getColumnIndex("height")).toString();
					result += result_id + "\t\t\t\t\t\t\t\t" + result_name
							+ "\t\t\t\t" + result_age + "\t\t\t\t\t\t"
							+ result_height + "\n";
				}
				sqlArea.setText(result);
				result = "���\t\t\t����\t\t\t����\t\t\t���\n";
			}
			break;
		case R.id.ID_Update:
			id_s = id_et.getText().toString();
			final String ids=id_s;
			if (id_s.isEmpty())
				printToast("������Ҫ������id����и��£�");
			else {
				LayoutInflater factory = LayoutInflater.from(MainActivity.this);
				final View textEntryView = factory.inflate(R.layout.dialog, null);
				final EditText tips_name = (EditText) textEntryView.findViewById(R.id.tips_nameEnter);
				final EditText tips_age = (EditText) textEntryView.findViewById(R.id.tips_ageEnter);
				final EditText tips_height = (EditText) textEntryView.findViewById(R.id.tips_heEnter);
				new AlertDialog.Builder(MainActivity.this)
						.setTitle("���ݿ����Tips")
						// ���öԻ������
						.setView(textEntryView)
//						.setMessage(
//								"�㽫����idΪ  \"" + id_s + "\" ������Ϊ�������ݣ�\n" + "����:"
//										+ nameL + "\n����:" + ageL + "\n���:"
//										+ heightL + "\n���ȷ�Ͻ��и��ģ����ȡ������"
//										+ "�����޸ģ�")
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {// ȷ����ť����Ӧ�¼�
										String sqlname=null;
										String sqlage=null;
										String sqlheight=null;
										String nameL=null,ageL=null,heightL=null;
										String sql2="select * from people where _id="+ids;
										Cursor cursor1=helper.select(sql2);
										while(cursor1.moveToNext()){
											sqlname=cursor1.getString(cursor1.getColumnIndex("name"))
													.toString();
											sqlage=String.valueOf(cursor1.getInt(cursor1
													.getColumnIndex("age")));
											sqlheight=cursor1.getString(cursor1.getColumnIndex("height"))
													.toString();
										}
										nameL = tips_name.getText().toString();
										ageL = tips_age.getText().toString();
										heightL = tips_height.getText().toString();
										if(nameL.isEmpty()&&ageL.isEmpty()&&heightL.isEmpty())
											printToast("��û�������κ����ݣ��������κ��޸�");
										else{
											if(nameL.isEmpty())
												nameL=sqlname;
											if(ageL.isEmpty())
												ageL=sqlage;
											if(heightL.isEmpty())
												heightL=sqlheight;
											final String sql1 = "update people set name='" + nameL
													+ "',age='" + ageL + "',height='" + heightL
													+ "' where _id=" + ids;
											boolean flag1 = helper
													.mulOperation(sql1);
											if (flag1)
												printToast("�ѳɹ��������ݣ�");
											else
												printToast("�������ݳ��ִ���");
										}
									}
								})
						.setNegativeButton("ȡ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {// ȷ����ť����Ӧ�¼�

									}
								}).show();
			}
			break;
		case R.id.area:

			break;
		case R.id.all_layout:

			break;
		}
	}

	public void printToast(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}

	public void getId_num() {
		String sql = "select * from people";
		Cursor cursor = helper.select(sql);
		if (cursor.moveToLast()) {
			int sql_id = cursor.getInt(cursor.getColumnIndex("_id"));
			id = sql_id;
		} else {
			id = 0;
		}
		cursor.close();
	}

	// �������뷨�㷨
	// ��дdispachTouchEvent���������ж�ʲôʱ���������
	public boolean dispatchTouchEvent(MotionEvent ev) {
		calculateEtXY();
		if (ev.getAction() == MotionEvent.ACTION_DOWN){
			return super.dispatchTouchEvent(ev);
		}
		else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return super.dispatchTouchEvent(ev);
		}
		else if (ev.getAction() == MotionEvent.ACTION_UP) {
			boolean b = false;
			View v = getCurrentFocus();
			float x = ev.getX();
			float y = ev.getY();
			b = isAlsoET(x, y);
				if (!b) {
					if (v instanceof EditText)
						v.clearFocus();
					v = getCurrentFocus();
				}
				if ((v.getId() == R.id.all_layout)) {
					// ������һ�仰��������ֻ�е���������������������ǲŽ��е��ж�
					InputMethodManager imm = (InputMethodManager) 
							getSystemService(Context.INPUT_METHOD_SERVICE);
					if (imm != null) {
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					}
				}
			return super.dispatchTouchEvent(ev);
		}
		// �ز����٣��������е������������TouchEvent��
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}

	// �жϵ���������Ƿ����ı���
	public boolean isAlsoET(float x, float y) {
		if ((x > name_etleft) && (y > name_ettop) && (x < name_et_right)
				&& (y < name_et_bottom))
			return true;
		if ((x > age_etleft) && (y > age_ettop) && (x < age_et_right)
				&& (y < age_et_bottom))
			return true;
		if ((x > height_etleft) && (y > height_ettop) && (x < height_et_right)
				&& (y < height_et_bottom))
			return true;
		if ((x > id_etleft) && (y > id_ettop) && (x < id_et_right)
				&& (y < id_et_bottom))
			return true;
		return false;
	}

	// ���ȼ��������EditText��x,y���꣬�õ���ʱ�����ظ�����
	public void calculateEtXY() {
		int[] name_etleftTop = { 0, 0 };
		name_et.getLocationInWindow(name_etleftTop);
		name_etleft = name_etleftTop[0];
		name_ettop = name_etleftTop[1];
		name_et_bottom = name_ettop + name_et.getHeight();
		name_et_right = name_etleft + name_et.getWidth();

		int[] age_etleftTop = { 0, 0 };
		age_et.getLocationInWindow(age_etleftTop);
		age_etleft = age_etleftTop[0];
		age_ettop = age_etleftTop[1];
		age_et_bottom = age_ettop + age_et.getHeight();
		age_et_right = age_etleft + age_et.getWidth();

		int[] height_etleftTop = { 0, 0 };
		height_et.getLocationInWindow(height_etleftTop);
		height_etleft = height_etleftTop[0];
		height_ettop = height_etleftTop[1];
		height_et_bottom = height_ettop + height_et.getHeight();
		height_et_right = height_etleft + height_et.getWidth();

		int[] id_etleftTop = { 0, 0 };
		id_et.getLocationInWindow(id_etleftTop);
		id_etleft = id_etleftTop[0];
		id_ettop = id_etleftTop[1];
		id_et_bottom = id_ettop + id_et.getHeight();
		id_et_right = id_etleft + id_et.getWidth();
	}

}
