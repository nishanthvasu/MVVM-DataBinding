package clouddish.mvvm.ndot.Recommendations.DataModel;

import java.util.List;

public class RecommendedDishes {
    public int id;
    public String name, mobile;
    public List<RecommendedDish> recommended_dishes;

    public class RecommendedDish {
        public String slot, dish_id, score;
    }
}
