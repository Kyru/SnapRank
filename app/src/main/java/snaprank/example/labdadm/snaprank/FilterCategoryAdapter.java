package snaprank.example.labdadm.snaprank;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FilterCategoryAdapter extends ArrayAdapter {

    private int layout;

    public FilterCategoryAdapter(Context context, int layout, List<FilterCategory> filterCategoryList){
        super(context, layout, filterCategoryList);
        this.layout = layout;

    }

    static class ViewHolder {

        private TextView category;

        public ViewHolder(TextView category) {
            this.category = category;
        }

        public TextView getCategory() {
            return category;
        }

        public void setCategory(TextView category) {
            this.category = category;
        }
    }


    @Override
    public View getView(int position, View view, ViewGroup parent){

        if(view == null){
            LayoutInflater layoutInflater = ((Activity) getContext()).getLayoutInflater();
            view = (layoutInflater.inflate(this.layout, null));

            FilterCategoryAdapter.ViewHolder viewHolder = new FilterCategoryAdapter.ViewHolder((TextView) view.findViewById(R.id.tv_filter_category));

            view.setTag(viewHolder);
        }

        FilterCategoryAdapter.ViewHolder resViewHolder = (FilterCategoryAdapter.ViewHolder) view.getTag();
        TextView tv_categoria = resViewHolder.getCategory();

        FilterCategory filterCategory = (FilterCategory) getItem(position);
        tv_categoria.setText(filterCategory.getCategory());

        return view;
    }
}
