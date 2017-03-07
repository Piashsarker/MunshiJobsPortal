package dcastalia.com.munshijobsportal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import dcastalia.com.munshijobsportal.Model.Jobs;
import dcastalia.com.munshijobsportal.R;

/**
 * Created by PT on 3/2/2017.
 */

public class MyJobAdapter extends RecyclerView.Adapter<MyJobAdapter.ViewHolder> {

    private Context context ;
    private ArrayList<Jobs> jobsArrayList;
    public MyJobAdapter(Context context , ArrayList<Jobs> jobsArrayList){
        this.jobsArrayList = jobsArrayList ;
        this.context = context;
    }
    @Override
    public MyJobAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_job_open_list,parent,false);
        return  new MyJobAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.jobTitle.setText(jobsArrayList.get(position).getJob_title());
        holder.jobPosition.setText(jobsArrayList.get(position).getJob_position());
        holder.country.setText(jobsArrayList.get(position).getCountry());
        holder.date.setText(jobsArrayList.get(position).getDate());
    }


    @Override
    public int getItemCount() {
        return jobsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle , jobPosition , country , date ;
        RelativeLayout container;
        public ViewHolder(View itemView) {
            super(itemView);
            container = (RelativeLayout) itemView.findViewById(R.id.container);
            jobTitle = (TextView) itemView.findViewById(R.id.job_title);
            jobPosition = (TextView) itemView.findViewById(R.id.job_position);
            country = (TextView) itemView.findViewById(R.id.country);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }
}
