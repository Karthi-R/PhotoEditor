package com.burhanrashid52.photoediting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karthi on 15/05/2022.
 */
public class FontPickerAdapter extends RecyclerView.Adapter<FontPickerAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Integer> fontList;

    public FontPickerAdapter(@NonNull Context context) {
        this.inflater = LayoutInflater.from(context);
        this.fontList = getDefaultFonts();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.font_picker_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.fontTv.setTypeface(ResourcesCompat.getFont(holder.itemView.getContext(), fontList.get(position)));
    }

    @Override
    public int getItemCount() {
        return fontList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView fontTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fontTv = itemView.findViewById(R.id.tv_font_name);
        }
    }

    public interface OnFontPickerClickListener {
        void onFontPickerClickListener(int font);
    }

    public static List<Integer> getDefaultFonts() {
        ArrayList<Integer> fonts = new ArrayList<>();
        fonts.add(R.font.krona_one);
        fonts.add(R.font.viaoda_libre);
        fonts.add(R.font.abel);
        fonts.add(R.font.belleza);
        fonts.add(R.font.cherry_swash);
        fonts.add(R.font.asul);
        fonts.add(R.font.coming_soon);
        fonts.add(R.font.roboto_medium);
        fonts.add(R.font.slabo_27px);
        return fonts;
    }
}
