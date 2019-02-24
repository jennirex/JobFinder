package jennirex.com.jobfinder.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

import jennirex.com.jobfinder.R;
import jennirex.com.jobfinder.WebActivity;


/**
 * Created by Tupelo on 2/18/2019.
 */

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

    private Context mContext;
    private List<Jobs> jobsList;

    public JobsAdapter(Context mContext, List<Jobs> jobsList) {
        this.mContext = mContext;
        this.jobsList = jobsList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public int currentItem;
        public ImageView itemImage;
        public TextView itemTitle;
        public CardView cardView;
        public TextView itemDetail;
        public TextView itemDetail2;
        public TextView itemDetail3;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.company_logo);
            itemTitle = (TextView)itemView.findViewById(R.id.job_title);
            itemDetail = (TextView)itemView.findViewById(R.id.company_name);
            itemDetail2 = (TextView)itemView.findViewById(R.id.job_location);
            itemDetail3 = (TextView)itemView.findViewById(R.id.posting_date);
            cardView = (CardView)itemView.findViewById(R.id.card_view_logs);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();
                    Jobs jobs = jobsList.get(position);
                    Intent intent = new Intent(mContext, WebActivity.class);
                    intent.putExtra("job_url",jobs.getJob_details_url());
                    mContext.startActivity(intent);

                }
            });

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.jobs_list_layout, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Jobs jobs = jobsList.get(i);
        viewHolder.itemTitle.setText("Job Title : "+jobs.getJob_title());
        viewHolder.itemDetail.setText("Company : "+jobs.getCompany_name());
        viewHolder.itemDetail2.setText("Location : "+jobs.getLocation());
        viewHolder.itemDetail3.setText("Posting date : "+jobs.getPosting_date());
        try {
            if (jobs.getCompany_Logo_url().equalsIgnoreCase("")) {
                viewHolder.itemImage.setImageResource(R.drawable.common_google_signin_btn_icon_dark_normal);
            }
            else{
                Picasso.with(mContext).load(jobs.getCompany_Logo_url()).into( viewHolder.itemImage);
            }
        }
        catch (Exception e){

        }


    }

    @Override
    public int getItemCount() {
        return jobsList.size();
    }
}
