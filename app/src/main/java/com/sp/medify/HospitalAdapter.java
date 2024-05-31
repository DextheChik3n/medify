package com.sp.medify;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {

    private List<Hospital> hospitals;
    private Context context;;

    public HospitalAdapter(Context context, List lst) {
        this.hospitals = lst;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospitalrow, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder (@NonNull ViewHolder holder, final int position) {
        final Hospital hospital = hospitals.get(position);

        holder.hospitalname.setText(hospital.getHospitalname());
        holder.hospitaladdress.setText(hospital.getAddress());
        holder.hospitaltel.setText("General Tel: " + hospital.getGeneraltelephone());
        Picasso.with(context).load(hospital.getImage_url()).into(holder.hospitallogo); //load image

        holder.view_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, HospitalDetail.class);

                i.putExtra("hospital_name", hospitals.get(position).getHospitalname());
                i.putExtra("hospital_address", hospitals.get(position).getAddress());
                i.putExtra("hospital_general_telephone", hospitals.get(position).getGeneraltelephone());
                i.putExtra("hospital_emergency_telephone", hospitals.get(position).getEmergencytelephone());
                i.putExtra("hospital_email", hospitals.get(position).getEmail());
                i.putExtra("hospital_website", hospitals.get(position).getWebsite());
                i.putExtra("hospital_visiting_hours", hospitals.get(position).getVisitinghours());
                i.putExtra("hospital_logo", hospitals.get(position).getImage_url());

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hospitals.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView hospitalname;
        TextView hospitaladdress;
        TextView hospitaltel;
        ImageView hospitallogo;
        LinearLayout view_container;

        public ViewHolder(View itemView) {
            super(itemView);

            hospitalname= (TextView) itemView.findViewById(R.id.hosname);
            hospitaladdress = (TextView) itemView.findViewById(R.id.hosadd);
            hospitaltel = (TextView) itemView.findViewById(R.id.hosgeneraltel);
            hospitallogo = (ImageView) itemView.findViewById(R.id.thumbnail);
            view_container = (LinearLayout) itemView.findViewById(R.id.container);
        }
    }
}
