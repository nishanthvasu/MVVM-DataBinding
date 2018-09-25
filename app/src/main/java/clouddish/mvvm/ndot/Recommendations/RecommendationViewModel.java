package clouddish.mvvm.ndot.Recommendations;

import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Observable;

import clouddish.mvvm.ndot.Constants;
import clouddish.mvvm.ndot.R;
import clouddish.mvvm.ndot.Recommendations.DataModel.RecommendedDishes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecommendationViewModel extends Observable {

    RecommendationNavigator mTaskDetailNavigator;
    private Context mContext;
    private ArrayList<RecommendedDishes.RecommendedDish> dishesList = new ArrayList<>();

    public RecommendationViewModel(Context context) {
        mContext = context;
    }

    public void setNavigator(RecommendationNavigator taskDetailNavigator) {
        mTaskDetailNavigator = taskDetailNavigator;
    }

// Observable field classes may be used instead of creating an Observable object.
// Fields of this type should be declared final because bindings only detect changes in the field's value, not of the field itself.

    public final ObservableField<String> snackbarText = new ObservableField<>();
    public final ObservableField<String> mobileNumber = new ObservableField<>("");
    public final ObservableField<String> getdata = new ObservableField<>("");

    public String getSnackbarText() {
        return snackbarText.get();
    }

    public void startTask() {
        if (mTaskDetailNavigator != null) {
            if (mobileNumber.get().equalsIgnoreCase("")) {
                snackbarText.set(mContext.getResources().getString(R.string.entermobilenum));
            } else {
                getRecommendedDishes(mobileNumber.get());
            }
        }
    }

    public ArrayList<RecommendedDishes.RecommendedDish> getUserList() {
        return dishesList;
    }

    public void getRecommendedDishes(String phone) {
        Call<RecommendedDishes> call = Constants.getClient().getRecommendedDishes(phone, "XMLHttpRequest");
        call.enqueue(new Callback<RecommendedDishes>() {
            @Override
            public void onResponse(@NonNull Call<RecommendedDishes> call, @NonNull Response<RecommendedDishes> response) {
                if (response.body() != null) {
                    dishesList.addAll(response.body().recommended_dishes);
                    setChanged();
                    notifyObservers();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RecommendedDishes> call, @NonNull Throwable t) {
                snackbarText.set(mContext.getResources().getString(R.string.txt_servernotreach));
            }
        });
    }
}
