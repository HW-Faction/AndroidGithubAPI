package app.githubapi.repository;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.githubapi.model.RepoModel;

public class MainRepository {

    private static final String TAG = "MainRepository.java";

    private final Application application;
    private final FirebaseAuth firebaseAuth;
    private final RequestQueue requestQueue;
    private final MutableLiveData<AuthResult> userMutableLiveData;
    private final MutableLiveData<ArrayList<RepoModel>> repoModelArrayList;

    public MainRepository(Application application) {
        this.application = application;
        firebaseAuth = FirebaseAuth.getInstance();

        userMutableLiveData = new MutableLiveData<>();
        requestQueue = Volley.newRequestQueue(application.getApplicationContext());
        repoModelArrayList = new MutableLiveData<>();
    }

    public MutableLiveData<AuthResult> login(Activity activity, String username) {

        Log.d(TAG, "login():: username: " + username);
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("github.com");
        provider.addCustomParameter("login", username);
        List<String> scopes =
                new ArrayList<String>() {
                    {
                        add("user:email");
                    }
                };
        provider.setScopes(scopes);

        Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            Log.d(TAG, "pendingResultTask != null");
            pendingResultTask
                    .addOnSuccessListener(
                            authResult -> {
                                Log.d(TAG, "pendingResultTask successful: ");
                                userMutableLiveData.setValue(authResult);
                            })
                    .addOnFailureListener(
                            e -> {
                                // Handle failure.
                                Log.d(TAG, "pendingResultTask failed: " + e.getMessage());
                            });
        } else {
            firebaseAuth
                    .startActivityForSignInWithProvider(/* activity= */ activity, provider.build())
                    .addOnSuccessListener(
                            authResult -> {
                                Log.d(TAG, "authResult:: name: " + authResult.getUser().getDisplayName());
                                userMutableLiveData.setValue(authResult);
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                    Log.d(TAG, "authResult:: failure: " + e.getMessage());
                                }
                            });
        }

        return userMutableLiveData;
    }

    public MutableLiveData<ArrayList<RepoModel>> getTrendingRepos(String language) {
        Log.d(TAG, "getTrendingRepos() :: language: " + language);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                "https://github-trending-api.now.sh/repositories?language=" + language,
                null,
                response -> {
                    try {
                        ArrayList<RepoModel> arrayList = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);

                            RepoModel repoModel = new RepoModel();
                            repoModel.setName(jsonObject.getString("name"));
                            repoModel.setLogin(jsonObject.getString("author"));
                            repoModel.setHtml_url(jsonObject.getString("url"));
                            repoModel.setDescription(jsonObject.getString("description"));
                            repoModel.setStars(jsonObject.getString("stars"));
                            arrayList.add(repoModel);
                            Log.d(TAG, "lang: " + language + " repo_name: " + repoModel.getName());
                        }
                        repoModelArrayList.setValue(arrayList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, e.getMessage());
                    }
                },
                error -> Log.d(TAG, "error :: " + error.getMessage()));

        requestQueue.add(request);
        return repoModelArrayList;
    }
}