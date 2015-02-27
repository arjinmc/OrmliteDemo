package com.arjinmc.ormlitedemo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.arjinmc.ormlitedemo.model.PersonBean;
import com.arjinmc.ormlitedemo.utils.DataHelperUtil;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.misc.TransactionManager;

public class MainActivity extends Activity implements OnClickListener{

	private Button btnInsert;
	private ListView lvResult;
	
	private List<PersonBean> persons;
	private ListViewAdapter adapter;
	
	private EditText etContent;
	private Button btnSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}
	
	private void init(){
		btnInsert = (Button) findViewById(R.id.btn_insert);
		lvResult = (ListView) findViewById(R.id.lv_result);
		etContent = (EditText) findViewById(R.id.et_content);
		btnSearch = (Button) findViewById(R.id.btn_search);
		
		btnInsert.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
		
		persons = new ArrayList<PersonBean>();
		adapter = new ListViewAdapter();
		lvResult.setAdapter(adapter);
		showData();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id){
			case R.id.btn_insert:
				insert();
				//using transaction
				//insertMore();
				break;
			case R.id.btn_search:
				search();
				break;
		}
		
	}
	
	private void insert(){
		RuntimeExceptionDao<PersonBean, Integer> simpleDao = DataHelperUtil.getHelper(MainActivity.this)
				.getSimplePersonDao();
		Random random = new Random();
		PersonBean person = new PersonBean();
		person.setName("p"+random.nextLong());
		int key = random.nextInt(2);
		String address = "shenzhen";
		int sex = 1;
		if(key==1){
			address = "shanghai";
			sex = 0;
		}
		person.setSex(sex);
		person.setAddress(address);
		simpleDao.create(person);
		showData();
	}
	
	private void search(){
		String key = etContent.getText().toString();
		if(TextUtils.isEmpty(key)){
			showData();
		}else{
			String likeKey = "%"+key+"%";
			RuntimeExceptionDao<PersonBean, Integer> simpleDao = DataHelperUtil.getHelper(MainActivity.this)
					.getSimplePersonDao();
			try {
				List<PersonBean> list = simpleDao.queryBuilder().where().like("name", likeKey)
						.or().like("address", likeKey).query();
				persons.clear();
				persons.addAll(list);
				adapter.notifyDataSetChanged();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * @desciption show data
	 * @author Eminem Lu
	 * @email arjinmc@hicsg.com
	 * @create 2015/2/26
	 */
	private void showData(){
		persons.clear();
		RuntimeExceptionDao<PersonBean, Integer> simpleDao = DataHelperUtil.getHelper(MainActivity.this)
				.getSimplePersonDao();
		// query for all of the data objects in the database
		List<PersonBean> list = simpleDao.queryForAll();
		persons.addAll(list);
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * @desciption a sample with using transaction
	 * @author Eminem Lu
	 * @email arjinmc@hicsg.com
	 * @create 2015/2/26
	 */
	private void insertMore(){
		 try {
			TransactionManager.callInTransaction(DataHelperUtil
					 .getHelper(MainActivity.this).getConnectionSource(), new Callable<Void>() {
			       
				public Void call() throws Exception {
			        	insert();
			        	insert();
			            return null;
			    }
			 });
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @desciption listview adapter
	 * @author Eminem Lu
	 * @email arjinmc@hicsg.com
	 * @create 2015/2/26
	 */
	private class ListViewAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return persons.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return persons.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final int currentPosition = position;
			
			ViewHolder viewHolder = null;
			if(convertView == null){
				convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_record, null);
				viewHolder = new ViewHolder();
				viewHolder.tvId = (TextView) convertView.findViewById(R.id.tv_id);
				viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
				viewHolder.tvSex = (TextView) convertView.findViewById(R.id.tv_sex);
				//for upgrade database test
				//viewHolder.tvAge = (TextView) convertView.findViewById(R.id.tv_age);
				viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
				viewHolder.btnEdit = (Button) convertView.findViewById(R.id.item_btn_edit);
				viewHolder.btnDel = (Button) convertView.findViewById(R.id.item_btn_del);
				
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			viewHolder.tvId.setText("id: "+persons.get(position).getId());
			viewHolder.tvName.setText("name: "+persons.get(position).getName());
			viewHolder.tvSex.setText("sex: "+persons.get(position).getSex());
			//for upgrade database test
			//viewHolder.tvAge.setText("age:" +persons.get(position).getAge());
			viewHolder.tvAddress.setText("address:"+persons.get(position).getAddress());
			
			viewHolder.btnDel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					RuntimeExceptionDao<PersonBean, Integer> simpleDao = DataHelperUtil.getHelper(MainActivity.this)
							.getSimplePersonDao();
					simpleDao.delete(persons.get(currentPosition));
					showData();
				}
			});
			
			viewHolder.btnEdit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra("data", persons.get(currentPosition));
					intent.setClass(MainActivity.this, EditActivity.class);
					startActivityForResult(intent, 1);
					
				}
			});
			
			return convertView;
		}
		
		
		private class ViewHolder{
			private TextView tvId;
			private TextView tvName;
			private TextView tvSex;
			private TextView tvAge;
			private TextView tvAddress;
			private Button btnEdit;
			private Button btnDel;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			showData();
		}
	}
}
