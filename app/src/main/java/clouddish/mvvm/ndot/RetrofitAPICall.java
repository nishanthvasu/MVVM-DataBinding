package clouddish.mvvm.ndot;

import clouddish.mvvm.ndot.Recommendations.DataModel.RecommendedDishes;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Url;

/**
 * Created by Nishanth on 4/6/16.
 */
@SuppressWarnings("ALL")
public interface RetrofitAPICall {

    /*
     * Retrofit get annotation with our URL
     */

    @GET()
    Call<RecommendedDishes> getRecommendedDishes(@Url String url, @Header("Authorization") String basic);

}