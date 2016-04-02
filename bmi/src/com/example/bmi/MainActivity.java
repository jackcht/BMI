package com.example.bmi;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import java.text.DecimalFormat;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViews();
		restorePrefs();
		setListeners();
	}



	protected static final int MENU_ABOUT = Menu.FIRST;				//FIRST == 1
	protected static final int MENU_Quit = Menu.FIRST+1;
	
	private static final String TAG = "Bmi";
	public static final String PREF = "BMI_PREF";
	public static final String PREF_HEIGHT = "BMI_Height";
	public static final String PREF_WEIGHT = "BMI_Weight";
	
	private Button button_calc;
	private EditText field_height;
	private EditText field_weight;
	
	private void findViews()
	{
		button_calc = (Button) findViewById(R.id.submit);
		field_height = (EditText) findViewById(R.id.height);
		field_weight = (EditText) findViewById(R.id.weight);
	}
	
	private void restorePrefs() {
		/*
		 * 我们宣告了一个偏好设定（SharedPreferences）型别"settings"，并使用"getSharedPreferences"函式，
		 * 来寻找系统中有无符合以"BMI_PREF"字串（PREF 参数）作为档名的偏好设定档。
		 * 如果有符合条件的偏好设定档存在的话，就将这个偏好设定指定使用"settings"作为代号来操作。
		 * 如果没有的话，"getSharedPreferences"函式会回传0 给"settings"。
		 */
		SharedPreferences settings = getSharedPreferences(PREF, 0);
		String pref_height = settings.getString(PREF_HEIGHT, "");
		String pref_weight = settings.getString(PREF_WEIGHT, "");
		
		if (! "".equals(pref_height) && ! "".equals(pref_weight))
		{
			field_height.setText(pref_height);
			field_weight.setText(pref_weight);
		}
		else if(! "".equals(pref_height))
		{
			field_height.setText(pref_height);
			/*
			 * 同时，因为身高栏位已经预先填好了，使用者只需要再填入体重值即可开始计算自己的BMI值。但是当程序一执行，预设的焦点栏位（游标）还是停在"身高"栏位上。
			 * 因此我们可以在"field_weight"栏位识别符号上，使用"requestFocus"函式，来手动将焦点栏位改到"体重"栏位上
			 */
			field_weight.requestFocus();
		}
		else if(! "".equals(pref_weight))
		{
			field_weight.setText(pref_weight);
			field_height.requestFocus();
		}
		
	}
	
	private void setListeners() {
		button_calc.setOnClickListener(createBMIReport);
	}
	
	@Override
	protected void onStop(){
		super.onStop();
		// Save user preferences. use Editor object to make changes.
		SharedPreferences settings = getSharedPreferences(PREF, 0);
		/*
		 * 要改变偏好设定(SharedPreferences)型别的内容，需要透过"edit"函式来编辑。
		 * 编辑结束后，要透过"commit"函式来将改变写到系统中。我们可以透过"putXXX"函式来为偏好设定(SharedPreferences)填入不同型别的内容
		 */
		settings.edit().putString(PREF_HEIGHT, field_height.getText().toString()).commit();
		settings.edit().putString(PREF_WEIGHT, field_weight.getText().toString()).commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu);
		//menu.add(0, 识别符号(identifer), 0, 字串或资源识别符号);
		//	"字串或资源识别符号"就是显示在萤幕上的叙述。而"识别符号"的目的则是作为这个选项的标籤，以供后续处理选项动作时，更容易辨认出所对应的选项。
		menu.add(0, MENU_ABOUT, 0, "About");
		menu.add(0, MENU_Quit, 0, "Quit");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		/*
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
		*/
		
		super.onOptionsItemSelected(item);
		//我们可以用"item.getItemId()"函数来取得在萤幕上选取的选项所对应的识别符号代码(identifer)。
		switch(item.getItemId()){
			case MENU_ABOUT:
				openOptionsDialog();
				break;
			case MENU_Quit:
				finish();
				break;
		}
		return true;
	}
	
	
	private void openOptionsDialog() {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(MainActivity.this)
		.setTitle(R.string.about_title)
		.setMessage(R.string.about_msg)
		.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialoginterface, int i){}
				})
		.setNegativeButton(R.string.homepage_label,new DialogInterface.OnClickListener(){
				public void onClick( DialogInterface dialoginterface, int i){
					Uri uri = Uri.parse(getString(R.string.homepage_uri));
					//Intent intent = new Intent(动作, 内容);
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);	//ACTION_VIEW:依所提供内容的不同，开启对应的程序以检视内容资料
					startActivity(intent);
				}
				})
		.show();
	}

	private OnClickListener createBMIReport = new OnClickListener()
	{
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, Report.class);
			Bundle bundle = new Bundle();
			bundle.putString("KEY_HEIGHT", field_height.getText().toString());
			bundle.putString("KEY_WEIGHT", field_weight.getText().toString());
			
			/*
			* 传送intent 时，我们可以在其上附加一些讯息，这些附加在Intent上的讯息都储存在Bundle 物件中。
			* 透过"intent.putExtras(bundle)"叙述，我们将"bundle" 物件附加在Intent 上，随着Intent 送出而送出。
			*/
			intent.putExtras(bundle);
			
			startActivity(intent);	
		}
	};
}
