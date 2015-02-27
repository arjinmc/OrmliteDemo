package com.arjinmc.ormlitedemo;

import com.arjinmc.ormlitedemo.model.PersonBean;
import com.arjinmc.ormlitedemo.utils.DataHelperUtil;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * @desciption edit data
 * @author Eminem Lu
 * @email arjinmc@hicsg.com
 * @create 2015/2/26
 */
public class EditActivity extends Activity {
	
	private EditText etId;
	private EditText etName;
	private EditText etSex;
	private EditText etAddress;
	private Button btnConfirm;
	
	private PersonBean person;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		init();
	}
	
	private void init(){
		etId = (EditText) findViewById(R.id.et_id);
		etName = (EditText) findViewById(R.id.et_name);
		etSex = (EditText) findViewById(R.id.et_sex);
		etAddress = (EditText) findViewById(R.id.et_address);
		btnConfirm = (Button) findViewById(R.id.btn_confrim);
		
		person = (PersonBean) getIntent().getSerializableExtra("data");
		etId.setText(person.getId()+"");
		etName.setText(person.getName());
		etSex.setText(person.getSex()+"");
		etAddress.setText(person.getAddress());
		
		btnConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				person.setName(etName.getText().toString());
				person.setAddress(etAddress.getText().toString());
				RuntimeExceptionDao<PersonBean, Integer> simpleDao = DataHelperUtil.getHelper(EditActivity.this)
						.getSimplePersonDao();
				int row = simpleDao.update(person);
				//update success
				if(row!=0){
					setResult(RESULT_OK);
					EditActivity.this.finish();
				}
			}
		});
	}
}
