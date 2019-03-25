package snaprank.example.labdadm.snaprank;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

public class ImagenRankingAdapter extends ArrayAdapter {
    private int layout;

    public ImagenRankingAdapter(Context context, int layout, List<ImagenRanking> imagenRankingList){
        super(context, layout, imagenRankingList);
        this.layout = layout;
    }

    static class ViewHolder {
        private ImageView iv_imagenRanking;
        public ViewHolder(ImageView imagenRanking) {
            this.iv_imagenRanking = imagenRanking;
        }
        public ImageView getImagenRanking() {
            return iv_imagenRanking;
        }
        public void setImagenRanking(ImageView imagenRanking) {
            this.iv_imagenRanking = imagenRanking;
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){

        if(view == null){
            LayoutInflater layoutInflater = ((Activity) getContext()).getLayoutInflater();
            view = (layoutInflater.inflate(this.layout, null));

            ImagenRankingAdapter.ViewHolder viewHolder = new ImagenRankingAdapter.ViewHolder((ImageView) view.findViewById(R.id.imagen_ranking));

            view.setTag(viewHolder);
        }

        ImagenRankingAdapter.ViewHolder resViewHolder = (ImagenRankingAdapter.ViewHolder) view.getTag();
        ImageView iv_imagenRanking = resViewHolder.getImagenRanking();

        ImagenRanking imagenRanking = (ImagenRanking) getItem(position);
        iv_imagenRanking.setImageResource(imagenRanking.getImageId());

        return view;
    }
}
