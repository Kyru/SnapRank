package snaprank.example.labdadm.snaprank.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.activities.LogrosActivity;
import snaprank.example.labdadm.snaprank.activities.MainActivity;
import snaprank.example.labdadm.snaprank.fragments.PhotoRankingFragment;
import snaprank.example.labdadm.snaprank.fragments.ProfileFragment;
import snaprank.example.labdadm.snaprank.fragments.SearchFragment;
import snaprank.example.labdadm.snaprank.fragments.UserRankingFragment;
import snaprank.example.labdadm.snaprank.models.Usuario;

public class CategoryAdapter extends ArrayAdapter{
    ArrayList<Usuario> categories = new ArrayList<>();
    ArrayList<View> items=new ArrayList<>();
    private FirebaseStorage storage;
    private Bitmap bitmap;

    public CategoryAdapter(Context context, int textViewResourceId, ArrayList<Usuario> objects, FirebaseStorage storage) {
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
        v = inflater.inflate(R.layout.search_element, null);
        if (v!=null){
            TextView textView = (TextView) v.findViewById(R.id.name_textview);
            TextView textView2 = (TextView) v.findViewById(R.id.location_textview);
            textView.setText(categories.get(position).getUsername());
            textView2.setText(categories.get(position).getLocation());
        StorageReference storageRef = storage.getReference();

        final StorageReference imageRef = storageRef.child(categories.get(position).getProfilePicUrl());
        final ImageButton imageView = (ImageButton) v.findViewById(R.id.avatar_ImageButton);
        final LinearLayout element=(LinearLayout)v.findViewById(R.id.elementclick);
        final long ONE_MEGABYTE = 2048 * 2048;
        imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // Para que tenga un pequeño delay, si no peta porque ocurre antes de que se genere el grid
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
        element.setOnClickListener(new View.OnClickListener() {
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
