package snaprank.example.labdadm.snaprank.adapters;


import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.activities.LogrosActivity;
import snaprank.example.labdadm.snaprank.fragments.PhotoRankingFragment;
import snaprank.example.labdadm.snaprank.fragments.ProfileFragment;
import snaprank.example.labdadm.snaprank.fragments.SearchFragment;
import snaprank.example.labdadm.snaprank.fragments.UserRankingFragment;
import snaprank.example.labdadm.snaprank.models.Usuario;

public class CategoryAdapter extends ArrayAdapter{
    ArrayList<Usuario> categories = new ArrayList<>();
    ArrayList<View> items=new ArrayList<>();

    public CategoryAdapter(Context context, int textViewResourceId, ArrayList<Usuario> objects) {
        super(context, textViewResourceId, objects);
        categories=objects;
    }
    public ArrayList<Usuario> getUsuarios(){return categories;}
    public View getItem(int index){return items.get(index);}

    @Override
    public int getCount() {
        return super.getCount();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context group=parent.getContext();
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.search_element, null);
        if (v!=null){
        TextView textView = (TextView) v.findViewById(R.id.name_textview);
        TextView textView2 = (TextView) v.findViewById(R.id.location_textview);
        ImageButton imageView = (ImageButton) v.findViewById(R.id.avatar_ImageButton);
        textView.setText(categories.get(position).getNombre());
        textView2.setText(categories.get(position).getLocalizacion());
        imageView.setImageResource(categories.get(position).getImage());
            Button bt_logros=v.findViewById(R.id.ib_profile_logros);
            bt_logros.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), LogrosActivity.class);
                    group.startActivity(intent);                }
            });

        imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new ProfileFragment();
                    FragmentTransaction transaction = ((FragmentActivity)group).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
    }


        return v;


}

}
