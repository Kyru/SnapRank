package snaprank.example.labdadm.snaprank;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class LogroAdapter extends  RecyclerView.Adapter<LogroAdapter.LogroViewHolder> {

    private LayoutInflater inflater;
    private Context context;
    private List<Logro> logroList;

    public LogroAdapter(Context context, List<Logro> logroList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.logroList = logroList;
    }

    @Override
    public LogroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.profile_logros_item, parent, false);
        LogroViewHolder holder = new LogroViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(LogroViewHolder holder, int position) {

        // Estos strings pillan el TextView para que quede de la siguiente manera:
        // Category: ejemploCategory
        // Month: ejemploMonth
        String categoria = context.getResources().getString(R.string.award_category) + " ";
        String fechaDesbloqueo = context.getResources().getString(R.string.award_date) + " ";

        holder.tv_categoriaLogro.setText(categoria +  logroList.get(position).getCategory());
        holder.tv_fechaDesbloqueoLogro.setText(fechaDesbloqueo + logroList.get(position).getFechaDesbloqueo());
        holder.iv_imagenLogro.setImageResource(logroList.get(position).getImageLogro());
    }

    @Override
    public int getItemCount() {
        return logroList.size();
    }

    class LogroViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_categoriaLogro;
        TextView tv_fechaDesbloqueoLogro;
        ImageView iv_imagenLogro;

        public LogroViewHolder(View itemView) {
            super(itemView);
            tv_categoriaLogro = (TextView)itemView.findViewById(R.id.tv_logro_category);
            tv_fechaDesbloqueoLogro = (TextView)itemView.findViewById(R.id.tv_logro_fecha_desbloqueo);
            iv_imagenLogro = (ImageView) itemView.findViewById(R.id.iv_imagen_logro);
        }
    }
}
