package snaprank.example.labdadm.snaprank.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import snaprank.example.labdadm.snaprank.models.ImagenSubida;
import snaprank.example.labdadm.snaprank.R;

public class ImagenSubidaAdapter extends ArrayAdapter {

    private int layout;
    private FirebaseStorage storage;
    private Bitmap bitmap;

    public ImagenSubidaAdapter(Context context, int layout, List<ImagenSubida> imagenSubidaList, FirebaseStorage storage){
        super(context, layout, imagenSubidaList);
        this.layout = layout;
        this.storage = storage;

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

            ViewHolder viewHolder = new ViewHolder((ImageView) view.findViewById(R.id.imagen_subida));

            view.setTag(viewHolder);
        }

        ViewHolder resViewHolder = (ViewHolder) view.getTag();
        final ImageView iv_imagenSubida = resViewHolder.getImagenSubida();

        ImagenSubida imagenSubida = (ImagenSubida) getItem(position);

        StorageReference storageRef = storage.getReference();
        final StorageReference imageRef = storageRef.child(imagenSubida.getUrl());

        final long ONE_MEGABYTE = 1024 * 1024;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                iv_imagenSubida.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 350,
                        250, false));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        return view;
    }
}
