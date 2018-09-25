package clouddish.mvvm.ndot.Recommendations;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import clouddish.mvvm.ndot.ActivityUtils;
import clouddish.mvvm.ndot.R;
import clouddish.mvvm.ndot.ViewModelHolder;

public class RecommendationActivity extends AppCompatActivity implements RecommendationNavigator {

    private RecommendationViewModel mRecommendationViewModel;

    public static final String TASKDETAIL_VIEWMODEL_TAG = "TASKDETAIL_VIEWMODEL_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cont_layout);

        RecommendationFragment taskDetailFragment = findOrCreateViewFragment();
        mRecommendationViewModel = findOrCreateViewModel();
        mRecommendationViewModel.setNavigator(this);
        // Link View and ViewModel
        taskDetailFragment.setViewModel(mRecommendationViewModel);
    }


    @NonNull
    private RecommendationFragment findOrCreateViewFragment() {
        // Get the requested task id
        RecommendationFragment taskDetailFragment = (RecommendationFragment) getSupportFragmentManager()
                .findFragmentById(R.id.lay_fr_container);

        if (taskDetailFragment == null) {
            taskDetailFragment = RecommendationFragment.newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    taskDetailFragment, R.id.lay_fr_container);
        }
        return taskDetailFragment;
    }

    @NonNull
    private RecommendationViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<RecommendationViewModel> retainedViewModel =
                (ViewModelHolder<RecommendationViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(TASKDETAIL_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            RecommendationViewModel viewModel = new RecommendationViewModel(
                    getApplicationContext());

            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    TASKDETAIL_VIEWMODEL_TAG);
            return viewModel;
        }
    }
}
