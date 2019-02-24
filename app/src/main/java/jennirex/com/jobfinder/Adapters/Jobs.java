package jennirex.com.jobfinder.Adapters;

/**
 * Created by Tupelo on 2/18/2019.
 */

public class Jobs {

    private String company_Logo_url,job_title,company_name,location,posting_date,job_details_url;
    public Jobs(){
    }

    public Jobs(
            String company_Logo_url,
            String job_title,
            String company_name,
            String location,
            String posting_date,
            String job_details_url

    ) {
        this.company_Logo_url = company_Logo_url;
        this.job_title = job_title;
        this.company_name = company_name;
        this.location = location;
        this.posting_date = posting_date;
        this.job_details_url = job_details_url;

    }

    public String getJob_title(){
        return job_title;
    }
    public String getCompany_Logo_url(){
        return company_Logo_url;
    }
    public String getCompany_name(){
        return company_name;
    }
    public String getLocation(){
        return location;
    }
    public String getPosting_date(){
        return posting_date;
    }
    public String getJob_details_url(){
        return job_details_url;
    }
}
