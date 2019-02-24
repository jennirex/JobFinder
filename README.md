# JobFinder test project
code for Android JobFinder

http://ptsv2.com/t/kofub-1550385380#

visit the above url to configure/add providers.

Let's assume that this is a web service implemented to fetch providers configured in the backend.
It returns a json string format response.
You can add multiple providers and will be automatically fetch by the app

Implemented a key mapper and query fileter to handle different key values accross multiple providers

{  
   "providers":[  
      {  
         "name":"github",
         "api_url":"https://jobs.github.com/positions.json?",
         "key_mapping":{  
            "company_logo":"company_logo",
            "job_title":"title",
            "company_name":"company",
            "location":"location",
            "post_date":"created_at",
            "job_details_url":"url"
         },
         "query_filter":{  
            "is_set":"true",
            "concatenator":"&",
            "location":"location",
            "position":"description"
         }
      },
      {  
         "name":"search.gov",
         "api_url":"https://jobs.search.gov/jobs/search.json?query=jobs",
         "key_mapping":{  
            "company_logo":"null",
            "job_title":"position_title",
            "company_name":"organization_name",
            "location":"locations",
            "post_date":"start_date",
            "job_details_url":"url"
         },
         "query_filter":{  
            "is_set":"false",
            "concatenator":"+",
            "location":"in",
            "position":""
         }
      }
   ]
}

