package com.burhanrashid52.photoediting;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import ja.burhanrashid52.photoeditor.TextStyleBuilder;

/**
 * Created by Burhanuddin Rashid on 1/16/2018.
 */

public class TextEditorDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = TextEditorDialogFragment.class.getSimpleName();
    public static final String EXTRA_TEXT_STYLE = "extra_text_style";
    public static final String EXTRA_INPUT_TEXT = "extra_input_text";
    private EditText mAddTextEditText;
    private TextView mAddTextDoneTextView;
    private InputMethodManager mInputMethodManager;

    private ImageButton colorPickerBtn, boldBtn, alignBtn, bgBtn, underlineBtn;
    private int mTextColor, mBgColor, mGravity = Gravity.CENTER;
    private boolean isBold, isUnderline;

    private TextEditor mTextEditor;

    RecyclerView addTextColorPickerRecyclerView;

    public interface TextEditor {
        void onDone(String inputText, int textColor, int bgColor, boolean isBold, boolean isUnderline, int gravity);
    }


    //Show dialog with provide text and text color
    public static TextEditorDialogFragment show(@NonNull AppCompatActivity appCompatActivity,
                                                TextStyleBuilder styleBuilder, String inputText) {
        Bundle args = new Bundle();
        if (styleBuilder != null) {
            args.putSerializable(EXTRA_TEXT_STYLE, new HashMap<>(styleBuilder.getValues()));
            args.putString(EXTRA_INPUT_TEXT, inputText);
        }

        TextEditorDialogFragment fragment = new TextEditorDialogFragment();
        fragment.setArguments(args);
        fragment.show(appCompatActivity.getSupportFragmentManager(), TAG);
        return fragment;
    }

    //Show dialog with default text input as empty and text color white
    public static TextEditorDialogFragment show(@NonNull AppCompatActivity appCompatActivity) {
        return show(appCompatActivity, null, "");
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        //Make dialog full screen with transparent background
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_text_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAddTextEditText = view.findViewById(R.id.add_text_edit_text);
        colorPickerBtn = view.findViewById(R.id.btn_color_picker);
        boldBtn = view.findViewById(R.id.btn_bold);
        bgBtn = view.findViewById(R.id.btn_bg);
        alignBtn = view.findViewById(R.id.btn_align);
        underlineBtn = view.findViewById(R.id.btn_underline);

        TextStyleBuilder styleBuilder = new TextStyleBuilder();
        if (getArguments().getSerializable(EXTRA_TEXT_STYLE) != null) {
            styleBuilder.setValues((HashMap<TextStyleBuilder.TextStyle, Object>) getArguments().getSerializable(EXTRA_TEXT_STYLE));
            styleBuilder.applyStyle(mAddTextEditText);

            mTextColor = (int) styleBuilder.getKey(TextStyleBuilder.TextStyle.COLOR);
            mBgColor = (int) styleBuilder.getKey(TextStyleBuilder.TextStyle.BACKGROUND);
            mGravity = (int) styleBuilder.getKey(TextStyleBuilder.TextStyle.GRAVITY);
            isBold = (int) styleBuilder.getKey(TextStyleBuilder.TextStyle.TEXT_STYLE) == Typeface.BOLD;
            isUnderline = (int) styleBuilder.getKey(TextStyleBuilder.TextStyle.TEXT_FLAG) == Paint.UNDERLINE_TEXT_FLAG;

            colorPickerBtn.setColorFilter(mTextColor, android.graphics.PorterDuff.Mode.SRC_IN);
            bgBtn.setColorFilter(mBgColor, android.graphics.PorterDuff.Mode.SRC_IN);
            updateGravityBtn();
            updateBoldBtn();
            updateUnderlineBtn();

        } else {
            mAddTextEditText.setTextColor(getResources().getColor(R.color.white));
        }

        String text = getArguments().getString(EXTRA_INPUT_TEXT);
        mAddTextEditText.setText(text);

        mInputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mAddTextDoneTextView = view.findViewById(R.id.add_text_done_tv);

        //Setup the color picker for text color
        addTextColorPickerRecyclerView = view.findViewById(R.id.add_text_color_picker_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        addTextColorPickerRecyclerView.setLayoutManager(layoutManager);
        addTextColorPickerRecyclerView.setHasFixedSize(true);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(getActivity());

        //This listener will change the text color when clicked on any color from picker
        colorPickerAdapter.setOnColorPickerClickListener(colorCode -> {
            if (colorPickerBtn.isActivated()) {
                mTextColor = colorCode;
                mAddTextEditText.setTextColor(colorCode);
                colorPickerBtn.setColorFilter(mTextColor, android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                mBgColor = colorCode;
                mAddTextEditText.setBackgroundColor(colorCode);
                bgBtn.setColorFilter(mBgColor, android.graphics.PorterDuff.Mode.SRC_IN);
            }

        });

        addTextColorPickerRecyclerView.setAdapter(colorPickerAdapter);

        //mAddTextEditText.setTextColor(mTextColor);
        mInputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        //Make a callback on activity when user is done with text editing
        mAddTextDoneTextView.setOnClickListener(view1 -> {
            mInputMethodManager.hideSoftInputFromWindow(view1.getWindowToken(), 0);
            dismiss();
            String inputText = mAddTextEditText.getText().toString();
            if (!TextUtils.isEmpty(inputText) && mTextEditor != null) {
                mTextEditor.onDone(inputText, mTextColor, mBgColor, isBold, isUnderline, mGravity);
            }
        });

        colorPickerBtn.setOnClickListener(this);
        bgBtn.setOnClickListener(this);
        underlineBtn.setOnClickListener(this);
        boldBtn.setOnClickListener(this);
        alignBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_color_picker:
                boolean isFontColorPickerVisible = addTextColorPickerRecyclerView.getVisibility() == View.VISIBLE;
                bgBtn.setActivated(false);
                colorPickerBtn.setActivated(!isFontColorPickerVisible);
                addTextColorPickerRecyclerView.setVisibility(isFontColorPickerVisible ? View.GONE : View.VISIBLE);
                break;

            case R.id.btn_bg:
                boolean isFontBgPickerVisible = addTextColorPickerRecyclerView.getVisibility() == View.VISIBLE;
                colorPickerBtn.setActivated(false);
                bgBtn.setActivated(!isFontBgPickerVisible);
                addTextColorPickerRecyclerView.setVisibility(isFontBgPickerVisible ? View.GONE : View.VISIBLE);
                break;

            case R.id.btn_bold:
                isBold = !isBold;
                updateBoldBtn();
                break;

            case R.id.btn_underline:
                isUnderline = !isUnderline;
                updateUnderlineBtn();
                break;

            case R.id.btn_align:
                mGravity = mGravity == Gravity.LEFT ? Gravity.RIGHT : (mGravity == Gravity.RIGHT) ? Gravity.CENTER : Gravity.LEFT;
                mAddTextEditText.setGravity(mGravity);
                updateGravityBtn();
                break;
        }

    }

    private void updateGravityBtn() {
        alignBtn.setImageDrawable(getResources().getDrawable(mGravity == Gravity.LEFT ? R.drawable.ic_baseline_format_align_left_24 :
                mGravity == Gravity.RIGHT ? R.drawable.ic_baseline_format_align_right_24 : R.drawable.ic_baseline_format_align_center_24));
    }

    private void updateBoldBtn() {
        int color = isBold ? R.color.colorPrimary : R.color.black;
        boldBtn.setColorFilter(ContextCompat.getColor(getContext(), color), android.graphics.PorterDuff.Mode.SRC_IN);
        mAddTextEditText.setTypeface(isBold ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);
    }

    private void updateUnderlineBtn() {
        int underlineColor = isUnderline ? R.color.colorPrimary : R.color.black;
        underlineBtn.setColorFilter(ContextCompat.getColor(getContext(), underlineColor), android.graphics.PorterDuff.Mode.SRC_IN);
        if (isUnderline) {
            mAddTextEditText.setPaintFlags(mAddTextEditText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        } else {
            mAddTextEditText.setPaintFlags(0);
        }
    }


    //Callback to listener if user is done with text editing
    public void setOnTextEditorListener(TextEditor textEditor) {
        mTextEditor = textEditor;
    }
}
