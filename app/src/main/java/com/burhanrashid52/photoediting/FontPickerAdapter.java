package com.burhanrashid52.photoediting;

import android.content.Context;
import android.graphics.Typeface;
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
    private List<FontInfo> fontList;

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
        Typeface tf = ResourcesCompat.getFont(holder.itemView.getContext(), fontList.get(position).id);
        holder.fontTv.setTypeface(tf);
        holder.fontTv.setText(fontList.get(position).name);
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

    public static List<FontInfo> getDefaultFonts() {
        ArrayList<FontInfo> fonts = new ArrayList<>();
        fonts.add(new FontInfo(R.font.krona_one, "Krona one"));
        fonts.add(new FontInfo(R.font.viaoda_libre, "Viaoda Libre"));
        fonts.add(new FontInfo(R.font.abel, "Able"));
        fonts.add(new FontInfo(R.font.belleza, "Belleza"));
        fonts.add(new FontInfo(R.font.cherry_swash, "Cherry Swash"));
        fonts.add(new FontInfo(R.font.asul, "Asul"));
        fonts.add(new FontInfo(R.font.coming_soon, "Coming Soon"));
        fonts.add(new FontInfo(R.font.roboto_medium, "Roboto Medium"));
        fonts.add(new FontInfo(R.font.slabo_27px, "Slabo"));
        return fonts;
    }
}
