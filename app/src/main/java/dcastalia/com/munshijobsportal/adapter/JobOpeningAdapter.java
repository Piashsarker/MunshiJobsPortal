package dcastalia.com.munshijobsportal.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import dcastalia.com.munshijobsportal.activity.JobDetailsActivity;
import dcastalia.com.munshijobsportal.Model.Jobs;
import dcastalia.com.munshijobsportal.R;

/**
 * Created by PT on 3/1/2017.
 */

public class JobOpeningAdapter extends RecyclerView.Adapter<JobOpeningAdapter.ViewHolder> {

    private Context context ;
    private ArrayList<Jobs> jobsArrayList;
    public JobOpeningAdapter(Context context , ArrayList<Jobs> jobsArrayList){
        this.jobsArrayList = jobsArrayList ;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_job_open_list,parent,false);
        return  new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.jobTitle.setText(jobsArrayList.get(position).getJob_title());
            holder.jobPosition.setText(jobsArrayList.get(position).getJob_position());
            holder.country.setText(jobsArrayList.get(position).getCountry());
            holder.date.setText(jobsArrayList.get(position).getDate());
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, JobDetailsActivity.class);
                    intent.putExtra("job_id",jobsArrayList.get(position).getJobId());
                    intent.putExtra("job_title", jobsArrayList.get(position).getJob_title());
                    intent.putExtra("job_position", jobsArrayList.get(position).getJob_position());
                    intent.putExtra("country", jobsArrayList.get(position).getCountry());
                    intent.putExtra("company",jobsArrayList.get(position).getCompany());
                    intent.putExtra("vacancy",jobsArrayList.get(position).getVacancy());
                    intent.putExtra("experience",jobsArrayList.get(position).getExperince());
                    intent.putExtra("salary",jobsArrayList.get(position).getSalary());
                    intent.putExtra("expire_date",jobsArrayList.get(position).getDate());
                    intent.putExtra("age","20");
                    intent.putExtra("gender","Male");
                    intent.putExtra("job_nature",jobsArrayList.get(position).getJobNature());
                    intent.putExtra("job_description","Here Goes A Job Description");
                    intent.putExtra("job_requirement",jobsArrayList.get(position).getJobRequirement());
                    context.startActivity(intent);
                }
            });

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
