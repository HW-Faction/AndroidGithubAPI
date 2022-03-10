package app.githubapi.viewmodel;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;

import app.githubapi.model.RepoModel;
import app.githubapi.repository.MainRepository;

public class MainViewModel extends AndroidViewModel {

    private final MainRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);

        repository = new MainRepository(application);
    }

    public MutableLiveData<AuthResult> login(Activity activity, String username) {
        return repository.login(activity, username);
    }

    public MutableLiveData<ArrayList<RepoModel>> getTrendingRepos(String language) {
        return repository.getTrendingRepos(language);
    }
}
