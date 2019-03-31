package snaprank.example.labdadm.snaprank.adapters;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import snaprank.example.labdadm.snaprank.R;
import snaprank.example.labdadm.snaprank.fragments.PhotoRankingFragment;
import snaprank.example.labdadm.snaprank.fragments.SearchFragment;
import snaprank.example.labdadm.snaprank.fragments.UserRankingFragment;

public class CategoryAdapter extends ArrayAdapter{
    ArrayList<snaprank.example.labdadm.snaprank.adapters.CategoryData> categories = new ArrayList<>();
    ArrayList<View> items=new ArrayList<>();

    public CategoryAdapter(Context context, int textViewResourceId, ArrayList<snaprank.example.labdadm.snaprank.adapters.CategoryData> objects) {
        super(context, textViewResourceId, objects);
        categories=objects;
    }
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
        v = inflater.inflate(R.layout.elementlayout, null);
        if (v!=null){
        TextView textView = (TextView) v.findViewById(R.id.textView3);
        ImageButton imageView = (ImageButton) v.findViewById(R.id.imageButton);
        ImageButton imageView2 = (ImageButton) v.findViewById(R.id.imageButton2);
        textView.setText(categories.get(position).getNombre());
        SearchFragment sf=new SearchFragment();
        imageView.setImageResource(categories.get(position).getImage());
        imageView2.setImageResource(categories.get(position).getImage());
            imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new UserRankingFragment();
                    FragmentTransaction transaction = ((FragmentActivity)group).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });
        imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new PhotoRankingFragment();
                    FragmentTransaction transaction = ((FragmentActivity)group).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragmentContainer, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            });}


        return v;


}
}
