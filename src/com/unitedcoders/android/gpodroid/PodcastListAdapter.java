package com.unitedcoders.android.gpodroid;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class PodcastListAdapter extends BaseAdapter {
    
    private boolean showCheckbox = false;

	public boolean isShowCheckbox() {
        return showCheckbox;
    }


    public void setShowCheckbox(boolean showCheckbox) {
        this.showCheckbox = showCheckbox;
    }



    private Context context;
	private List<PodcastElement> podcasts = new ArrayList<PodcastElement>();
	
	public PodcastListAdapter(Context context) {
		this.context = context;
	}
	
	
	@Override
	public int getCount() {
		return podcasts.size();
	}

	@Override
	public Object getItem(int i) {
		return podcasts.get(i);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	public void addItem(PodcastElement podcast){
		podcasts.add(podcast);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PodcastView podcast;
		if(convertView == null){
			podcast = new PodcastView(context, podcasts.get(position), showCheckbox);
		} else {
			podcast = (PodcastView) convertView;
			
			
		}
		
		return podcast;
	
	}
	
	
	
	public List<PodcastElement> getCheckedItems(){
		List<PodcastElement> result =  new ArrayList<PodcastElement>();
		for(PodcastElement pe : podcasts){
			if(pe.isChecked()){
				result.add(pe);
			}
		}
		
		return result;
	}
	
	

}
