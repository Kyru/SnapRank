package snaprank.example.labdadm.snaprank.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.activities.UploadImageActivity;
import snaprank.example.labdadm.snaprank.activities.ViewPicActivity;

public class HomeFragment extends Fragment {

    ImageButton ib_filter;
    ImageButton ib_upload_image;
    int imageID;
    FirebaseStorage storage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container, false);

        ImageView image = view.findViewById(R.id.main_pic);
        storage = FirebaseStorage.getInstance();
        StorageReference images = storage.getReference().child("images");

        // Esto cuando este la lógica del firebase, habrá que sacar la imagen de ahí (con el id
        // que este en el firebase


        imageID = R.drawable.taylor;
        image.setImageResource(imageID);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoViewPic(v);
            }
        });


        ib_filter = ((AppCompatActivity)getActivity()).findViewById(R.id.custom_bar_filter);
        ib_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUpMenuFilter(v, ib_filter);
            }
        });

        ib_upload_image = ((AppCompatActivity)getActivity()).findViewById(R.id.custom_bar_add);
        ib_upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        return view;

    }

    public void gotoViewPic(View view){
        Intent intent = new Intent(getContext(), ViewPicActivity.class);
        intent.putExtra("imageID", imageID);
        startActivity(intent);
    }

    public void showPopUpMenuFilter(View view, ImageButton filter){
        PopupMenu popupMenu = new PopupMenu(getActivity(), filter);
        popupMenu.getMenuInflater().inflate(R.menu.category_filter_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getActivity(),"You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popupMenu.show();
    }

    public void uploadImage() {
        Intent intent = new Intent(getContext(), UploadImageActivity.class);
        startActivity(intent);
    }
}
