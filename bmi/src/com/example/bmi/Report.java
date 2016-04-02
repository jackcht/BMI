package com.example.bmi;

import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
//import android.widget.Button;
import android.widget.TextView;

public class Report extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.report);		//此处为R.layout.report 对应我们新定义的XML 描述档产生的资源识别符号
		findViews();
		showResults();
		setListeners();
	}

	private Button button_back;
	private TextView view_result;
	private TextView view_suggest;
	
	
	private void findViews() {
		// TODO Auto-generated method stub
		button_back = (Button) findViewById(R.id.report_back);
		view_result = (TextView) findViewById(R.id.result);
		view_suggest = (TextView) findViewById(R.id.suggest);
	}
	
	
	private void setListeners() {
		// TODO Auto-generated method stub
		button_back.setOnClickListener(backMain);
	}
	
	private OnClickListener backMain = new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Report.this.finish();
		}
	};

	private void showResults() {
		// TODO Auto-generated method stub
		DecimalFormat nf = new DecimalFormat("0.00");
		/*
		 * 当我们透过Intent 传到新的Activity 后，只要使用Activity.getIntent() 函数，就可以得到传来的Intent 物件。
		 * 然后使用"getExtras"函式，就能取得附加在Intent 上的bunde 物件。
		 */
		Bundle bundle = this.getIntent().getExtras();		//getIntent(): Return the intent that started this activity. 	
		double height = Double.parseDouble(bundle.getString("KEY_HEIGHT"))/100;
		double weight = Double.parseDouble(bundle.getString("KEY_WEIGHT"));
		double BMI = weight / (height * height);
		
		view_result.setText(getText(R.string.bmi_result)+nf.format(BMI));
		
		//Give health advice
		if(BMI>25){
			view_suggest.setText(R.string.advice_heavy);
		}
		else if(BMI<20){
			view_suggest.setText(R.string.advice_light);
		}
		else {
			view_suggest.setText(R.string.advice_average);
		}
		
		openOptionsDialog();

	}


	private void openOptionsDialog() {
		// TODO Auto-generated method stub
		// "Toast "界面元件的作用是弹出一个讯息框，快速在屏幕上显示一小段讯息。
		Toast.makeText(Report.this, "BMI Calculator", Toast.LENGTH_SHORT).show();		
	}
	
}
