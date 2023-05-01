package narasimhaa.com.mitraservice.Adapater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;




import java.util.ArrayList;
import java.util.HashSet;

import narasimhaa.com.mitraservice.Listener.GenericSelectListner;
import narasimhaa.com.mitraservice.Model.FormFieldOptionT;
import narasimhaa.com.mitraservice.R;
import narasimhaa.com.mitraservice.Utility.MyUtilities;


public class ADPTFormsPopup extends BaseAdapter {

	private Context context;
	private ArrayList<FormFieldOptionT> list;
	private String type;
	private HashSet<String> selectedList;
	private GenericSelectListner projectChangeListener;

	public ADPTFormsPopup(Context context, ArrayList<FormFieldOptionT> list, String type, String selected, GenericSelectListner projectChangeListener) {
		this.context = context;
		this.list = list;
		this.type=type;
		this.projectChangeListener=projectChangeListener;
		selectedList=new HashSet<String>();
		if(MyUtilities.isNull(selected)){

			String data[]=selected.split(",");
			for(String content:data){
				selectedList.add(content.trim());
			}
			
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(type.equals("dropdown") || type.equals("radiobutton")){
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.form_cell_textview, null);

		}
		TextView name = (TextView) convertView
				.findViewById(R.id.uiTV_name);
		name.setText(list.get(position).getOptionValue());
		
		//convertView.setTag(list.get(position) + "");
		}else if(type.equals("checkbox")){
			
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.form_cell_checkbox, null);

			}
			
			CheckBox cb = (CheckBox) convertView.findViewById(R.id.ui_CB);
			
			if(selectedList!=null){
			
			boolean checked=selectedList.contains(list.get(position).getOptionValue().trim());
			cb.setChecked(checked);
			}
			
			cb.setText(" "+list.get(position).getOptionValue());
			cb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				   CheckBox cb = (CheckBox) v ;
					if(cb.isChecked())
					{
						if(!selectedList.contains(cb.getText().toString().trim())){
							selectedList.add(cb.getText().toString().trim());
							projectChangeListener.setItem("");
							projectChangeListener.setItem(selectedList.toString().replace("[", "").replace("]", ""));
						}
						
					}else{
						
						if(!selectedList.contains(cb.getText().toString().trim())){
							selectedList.remove(cb.getText().toString().trim());
							projectChangeListener.setItem("");
							projectChangeListener.setItem(selectedList.toString().replace("[", "").replace("]", ""));
						}
					}
			}
		});
			
		}

		return convertView;
	}

}
