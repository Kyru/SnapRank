package snaprank.example.labdadm.snaprank.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import snaprank.example.labdadm.snaprank.models.ImageRanking;
import snaprank.example.labdadm.snaprank.R;
import java.util.List;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.models.ImagenRanking;

public class ImageRankingAdapter extends ArrayAdapter {
    private int layout;

    public ImageRankingAdapter(Context context, int layout, List<ImageRanking> imageRankingList){
        super(context, layout, imageRankingList);
        this.layout = layout;
    }

    static class ViewHolder {
        private ImageView iv_imageRanking;
        public ViewHolder(ImageView imageRanking) {
            this.iv_imageRanking = imageRanking;
        }
        public ImageView getImageRanking() {
            return iv_imageRanking;
        }
        public void setImageRanking(ImageView imageRanking) {
            this.iv_imageRanking = imageRanking;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){

        if(view == null){
            LayoutInflater layoutInflater = ((Activity) getContext()).getLayoutInflater();
            view = (layoutInflater.inflate(this.layout, null));

            ImageRankingAdapter.ViewHolder viewHolder = new ImageRankingAdapter.ViewHolder((ImageView) view.findViewById(R.id.image_ranking));

            view.setTag(viewHolder);
        }

        ImageRankingAdapter.ViewHolder resViewHolder = (ImageRankingAdapter.ViewHolder) view.getTag();
        ImageView iv_imageRanking = resViewHolder.getImageRanking();

        ImageRanking imageRanking = (ImageRanking) getItem(position);
        iv_imageRanking.setImageResource(imageRanking.getImageId());

        return view;
    }
}
