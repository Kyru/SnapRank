package snaprank.example.labdadm.snaprank.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.activities.MainActivity;
import snaprank.example.labdadm.snaprank.models.Usuario;

public class RankingAdapter extends ArrayAdapter{
    ArrayList<Usuario> categories = new ArrayList<>();
    ArrayList<View> items=new ArrayList<>();
    private FirebaseStorage storage;
    private Bitmap bitmap;

    public RankingAdapter(Context context, int textViewResourceId, ArrayList<Usuario> objects, FirebaseStorage storage) {
        super(context, textViewResourceId, objects);
        categories=objects;
        this.storage=storage;
    }
    public ArrayList<Usuario> getUsuarios(){return categories;}
    public View getItem(int index){return items.get(index);}

    @Override
    public int getCount() {
        return super.getCount();
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.user_ranking_element, null);
        if (v!=null){
            TextView textView = (TextView) v.findViewById(R.id.name_textview);
            TextView textView1 = (TextView) v.findViewById(R.id.pos_textview);
            TextView textView2 = (TextView) v.findViewById(R.id.location_textview);

            textView.setText(categories.get(position).getUsername());
            textView1.setText("#" + (position+1));
            textView2.setText(categories.get(position).getLocation());
        StorageReference storageRef = storage.getReference();

        final StorageReference imageRef = storageRef.child(categories.get(position).getProfilePicUrl());
        final ImageButton imageView = (ImageButton) v.findViewById(R.id.avatar_ImageButton);
        final long ONE_MEGABYTE = 2048 * 2048;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 350,
                                250, false));
                        //iv_imagenSubida.setImageBitmap(bitmap);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        final Context group=parent.getContext();

        imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(group, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("username", categories.get(position).getUsername());
                    bundle.putBoolean("goToProfile", true);
                    intent.putExtras(bundle);
                    group.startActivity(intent);
                }
            });
    }
        return v;
    }
}
