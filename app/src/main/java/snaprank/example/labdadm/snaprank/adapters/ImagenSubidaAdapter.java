package snaprank.example.labdadm.snaprank.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import snaprank.example.labdadm.snaprank.models.ImagenSubida;
import snaprank.example.labdadm.snaprank.R;

public class ImagenSubidaAdapter extends ArrayAdapter {

    private int layout;

    public ImagenSubidaAdapter(Context context, int layout, List<ImagenSubida> imagenSubidaList){
        super(context, layout, imagenSubidaList);
        this.layout = layout;

    }

    static class ViewHolder {

        private ImageView iv_imagenSubida;

        public ViewHolder(ImageView imagenSubida) {
            this.iv_imagenSubida = imagenSubida;
        }

        public ImageView getImagenSubida() {
            return iv_imagenSubida;
        }

        public void setImagenSubida(ImageView imagenSubida) {
            this.iv_imagenSubida = imagenSubida;
        }
    }


    @Override
    public View getView(int position, View view, ViewGroup parent){

        if(view == null){
            LayoutInflater layoutInflater = ((Activity) getContext()).getLayoutInflater();
            view = (layoutInflater.inflate(this.layout, null));

            ViewHolder viewHolder = new ViewHolder((ImageView) view.findViewById(R.id.image_ranking));

            view.setTag(viewHolder);
        }

        ViewHolder resViewHolder = (ViewHolder) view.getTag();
        ImageView iv_imagenSubida = resViewHolder.getImagenSubida();

        ImagenSubida imagenSubida = (ImagenSubida) getItem(position);
        iv_imagenSubida.setImageResource(imagenSubida.getImageId());

        return view;
    }
}
