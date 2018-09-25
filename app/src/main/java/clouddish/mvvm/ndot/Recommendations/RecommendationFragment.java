package clouddish.mvvm.ndot.Recommendations;

import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observer;

import clouddish.mvvm.ndot.Constants;
import clouddish.mvvm.ndot.R;
import clouddish.mvvm.ndot.Recommendations.DataModel.RecommendedDishes;
import clouddish.mvvm.ndot.SnackbarUtils;
import clouddish.mvvm.ndot.databinding.FragmentRecommendationBinding;
import clouddish.mvvm.ndot.databinding.ItemDishesBinding;

public class RecommendationFragment extends Fragment implements Observer {

    RecommendationViewModel mViewModel;
    FragmentRecommendationBinding viewDataBinding;
    private Observable.OnPropertyChangedCallback mSnackbarCallback;

    public static RecommendationFragment newInstance() {
        return new RecommendationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommendation, container, false);
        viewDataBinding = FragmentRecommendationBinding.bind(view);
        viewDataBinding.setViewmodel(mViewModel);
        setHasOptionsMenu(true);
        return view;
    }

    public void setViewModel(RecommendationViewModel taskViewModel) {
        mViewModel = taskViewModel;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSnackbar();
        mViewModel.mobileNumber.set("1234567890");
        setUpListOfUsersView(viewDataBinding.rvList);
        setUpObserver(mViewModel);
        viewDataBinding.btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.BASE_URL.equals(""))
                    Toast.makeText(getActivity(), getResources().getString(R.string.txt_addserverurl), Toast.LENGTH_SHORT).show();
                else
                    mViewModel.startTask();
            }
        });
    }

    public void setUpObserver(java.util.Observable observable) {
        observable.addObserver(this);
    }

    // set up the list of user with recycler view
    private void setUpListOfUsersView(RecyclerView listUser) {
        UserAdapter userAdapter = new UserAdapter(mViewModel.getUserList());
        listUser.setAdapter(userAdapter);
        listUser.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onDestroy() {
        if (mSnackbarCallback != null) {
            mViewModel.snackbarText.removeOnPropertyChangedCallback(mSnackbarCallback);
        }
        super.onDestroy();
    }

    private void setupSnackbar() {
        mSnackbarCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                SnackbarUtils.showSnackbar(getView(), mViewModel.getSnackbarText());
            }
        };
        mViewModel.snackbarText.addOnPropertyChangedCallback(mSnackbarCallback);
    }

    @Override
    public void update(java.util.Observable observable, Object o) {
        if (observable instanceof RecommendationViewModel) {
            UserAdapter userAdapter = (UserAdapter) viewDataBinding.rvList.getAdapter();
            RecommendationViewModel userViewModel = (RecommendationViewModel) observable;
            userAdapter.setUserList(userViewModel.getUserList());
        }
    }

    public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserAdapterViewHolder> {

        private ArrayList<RecommendedDishes.RecommendedDish> userList = new ArrayList<>();

        public UserAdapter(ArrayList<RecommendedDishes.RecommendedDish> userList) {
            this.userList = userList;
        }

        @Override
        public UserAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemDishesBinding itemUserBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_dishes, parent, false);
            return new UserAdapterViewHolder(itemUserBinding);
        }

        @Override
        public void onBindViewHolder(UserAdapterViewHolder holder, int position) {
            holder.bindUser(userList.get(position));
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }

        public void setUserList(ArrayList<RecommendedDishes.RecommendedDish> userList) {
            this.userList = userList;
            notifyDataSetChanged();
        }

        public class UserAdapterViewHolder extends RecyclerView.ViewHolder {

            ItemDishesBinding mItemUserBinding;

            public UserAdapterViewHolder(ItemDishesBinding itemUserBinding) {
                super(itemUserBinding.getRoot());
                this.mItemUserBinding = itemUserBinding;
            }

            void bindUser(RecommendedDishes.RecommendedDish dishes) {
                mItemUserBinding.setDishesmodel(dishes);
            }
        }
    }
}
