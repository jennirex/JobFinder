package jennirex.com.jobfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jennirex.com.jobfinder.Adapters.Jobs;
import jennirex.com.jobfinder.Adapters.JobsAdapter;

public class JobsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    private List<Jobs> jobsList;
    private String jobs,key_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);

        Intent intent = getIntent();
        jobs = intent.getStringExtra("job_results");
        key_map = intent.getStringExtra("key_map");



        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_logs);
        jobsList = new ArrayList<>();
        adapter = new JobsAdapter(this,jobsList);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        prepareJobs();
    }

    private void prepareJobs() {
        try {
            JSONArray jsonArray = new JSONArray(jobs);
            JSONObject objectKeys = new JSONObject(key_map).optJSONObject("key_mapping");
            String company_logo_url;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (objectKeys.getString("company_logo").equalsIgnoreCase("null")){
                    company_logo_url="";
                }
                else {
                    company_logo_url= jsonObject.getString(objectKeys.getString("company_logo"));
                }
                Jobs a = new Jobs(
                        company_logo_url,
                        jsonObject.getString(objectKeys.getString("job_title")),
                        jsonObject.getString(objectKeys.getString("company_name")),
                        jsonObject.getString(objectKeys.getString("location")),
                        jsonObject.getString(objectKeys.getString("post_date")),
                        jsonObject.getString(objectKeys.getString("job_details_url"))
                );
                jobsList.add(a);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

}
