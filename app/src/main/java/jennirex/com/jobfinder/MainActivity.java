package jennirex.com.jobfinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jennirex.com.jobfinder.Helpers.ApiRequest;
import jennirex.com.jobfinder.Helpers.Constants;
import jennirex.com.jobfinder.Helpers.ErrorHandler;
import jennirex.com.jobfinder.Interfaces.GetJobsInterface;
import jennirex.com.jobfinder.Interfaces.GetProvidersInterface;

public class MainActivity extends AppCompatActivity implements GetProvidersInterface, GetJobsInterface {
    ErrorHandler errorHandler = new ErrorHandler();
    Spinner spinnerProvider;
    List<String> provider_name= new ArrayList<String>();
    List<JSONObject> providers= new ArrayList<JSONObject>();
    List<Integer> provider_value= new ArrayList<Integer>();
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    Button buttonFind;
    EditText editPosition;
    TextView editLocation;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ApiRequest.GetProviderRequest(MainActivity.this);
        showProgressDialog(true);
        spinnerProvider = (Spinner) findViewById(R.id.spinner1);
        editLocation = (TextView) findViewById(R.id.editLocation);
        editPosition = (EditText) findViewById(R.id.editPosition);
        buttonFind = (Button) findViewById(R.id.btnFind);

        // Initialize Places.
        Places.initialize(getApplicationContext(), Constants.GEO_API_KEY);


        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int AUTOCOMPLETE_REQUEST_CODE = 1;

                // Set the fields to specify which types of place data to
                // return after the user has made a selection.
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(getApplicationContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        buttonFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = provider_value.get(spinnerProvider.getSelectedItemPosition());
                if (pos < 0) {
                    Toast.makeText(getApplicationContext(),"Please Choose Provider",Toast.LENGTH_LONG).show();
                    return;
                }
                String location = editLocation.getText().toString();
                String position = editPosition.getText().toString();
                try {
                    String api_url = providers.get(pos).getString("api_url");
                    JSONObject jobject = new JSONObject(providers.get(pos).getString("query_filter"));
                    String concat_string = jobject.getString("concatenator");
                    if(jobject.getBoolean("is_set")){
                        if (!location.equalsIgnoreCase("")){
                            api_url += jobject.getString("location")+"="+ URLEncoder.encode(location);
                        }
                        if (!position.equalsIgnoreCase("")){
                            api_url += concat_string+jobject.getString("position")+"="+URLEncoder.encode(position);
                        }
                    }
                    else {
                        if (!position.equalsIgnoreCase("")){
                            api_url += concat_string+URLEncoder.encode(position);
                        }
                        if (!location.equalsIgnoreCase("")){
                            api_url += concat_string+jobject.getString("location")+concat_string+URLEncoder.encode(location);
                        }
                    }
                    showProgressDialog(true);
                    ApiRequest.GetJobsRequest(MainActivity.this,api_url);

                } catch (JSONException e){
                    e.printStackTrace();
                }

            }
        });

    }

    AdapterView.OnItemSelectedListener onItemSelectedListener =
            new AdapterView.OnItemSelectedListener(){

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    ((TextView) parent.getChildAt(0)).setTextSize((int)getResources().getDimension(R.dimen.mySpinnerTextSize));
                    ((TextView) parent.getChildAt(0)).setGravity(Gravity.CENTER);
                    if(position == 0){
                        // Set the hint text color gray
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.GRAY);
                    }
                    else {
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}

            };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Geocoder geocoder = new Geocoder(this);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    List<android.location.Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude,place.getLatLng().longitude, 1);
                    editLocation.setText(addresses.get(0).getAdminArea());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("Place", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onGetResponse(String response) {
       if(errorHandler.getErrorMessage(response)){
           Toast.makeText(getApplicationContext(),response, Toast.LENGTH_LONG).show();
       } else {
           provider_name.add("Select provider");
           provider_value.add(-1);
           try {
               JSONObject jsonObject = new JSONObject(response);
               JSONArray result = jsonObject.getJSONArray("providers");
               for (int i=0;i<result.length();i++){
                   JSONObject jobject = new JSONObject(result.get(i).toString());
                   provider_name.add(jobject.getString("name"));
                   provider_value.add(i);
                   providers.add(jobject);
               }

               Log.d("MainActivity",result.toString());

               ArrayAdapter<String> providerAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, provider_name) {
                   @Override
                   public boolean isEnabled(int position) {
                       if (position == 0) {
                           // Disable the first item from Spinner
                           // First item will be use for hint
                           return false;
                       } else {
                           return true;
                       }
                   }

                   @Override
                   public View getDropDownView(int position, View convertView,
                                               ViewGroup parent) {
                       View view = super.getDropDownView(position, convertView, parent);
                       TextView tv = (TextView) view;
                       if (position == 0) {
                           // Set the hint text color gray
                           tv.setTextColor(Color.GRAY);
                       } else {
                           tv.setTextColor(Color.BLACK);
                       }
                       return view;
                   }

               };
               spinnerProvider.setAdapter(providerAdapter);
               spinnerProvider.setOnItemSelectedListener(onItemSelectedListener);
               showProgressDialog(false);

           } catch (JSONException e) {
               e.printStackTrace();
           }
       }

    }

    @Override
    public void onGetJobsResponse(String response) {
        Log.d("Jobs", response);
        try {
            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() > 0) {

                int pos = provider_value.get(spinnerProvider.getSelectedItemPosition());
                Intent intent = new Intent(getApplicationContext(), JobsActivity.class);
                intent.putExtra("key_map", providers.get(pos).toString());
                intent.putExtra("job_results", response);
                startActivity(intent);
                showProgressDialog(false);
            } else {
                showProgressDialog(false);
                Toast.makeText(this,"No Jobs found.",Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void showProgressDialog(boolean show){
        if (show) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage(this.getResources().getString(R.string.please_wait));
            pDialog.setCancelable(false);
            pDialog.show();
        } else {
            pDialog.dismiss();
        }
    }
}
