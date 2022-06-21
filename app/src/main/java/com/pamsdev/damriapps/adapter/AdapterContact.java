package com.pamsdev.damriapps.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.pamsdev.damriapps.R;
import com.pamsdev.damriapps.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class AdapterContact extends RecyclerView.Adapter<AdapterContact.ListVewHolder> {

    Context context;
    List<Contact> listData;
    List<Contact> listDataSearch;

    public AdapterContact(Context context, List<Contact> listData){
        this.context = context;
        this.listData = listData;
        this.listDataSearch = listData;
        notifyDataSetChanged();
    }


    public interface OnItemClickCallback {
        void onItemClicked(Contact data);
    }

    private OnItemClickCallback onItemClickCallback;
    public void setOnItemClickCallback(AdapterContact.OnItemClickCallback onItemClickCallback){
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ListVewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_contact, parent, false);
        return new ListVewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListVewHolder holder, int position) {
        Contact dataMember = listData.get(position);

        holder.tvName.setText(dataMember.getNama());
        holder.tvPhone.setText(dataMember.getNomor());

        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(listData.get(holder.getAdapterPosition())));

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public static class ListVewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPhone;

        public ListVewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvPhone = itemView.findViewById(R.id.tv_phone);

        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listData = listDataSearch;
                } else {
                    List<Contact> filteredList = new ArrayList<>();
                    for (Contact contact : listDataSearch) {
                        if (contact.getNama().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(contact);
                        }
                    }

                    listData = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listData;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listData = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Contact dataMember);
    }

}
