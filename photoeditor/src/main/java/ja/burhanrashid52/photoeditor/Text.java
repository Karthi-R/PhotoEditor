package ja.burhanrashid52.photoeditor;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Burhanuddin Rashid on 14/05/21.
 *
 * @author <https://github.com/burhanrashid52>
 */
class Text extends Graphic {

    private final MultiTouchListener mMultiTouchListener;
    private final Typeface mDefaultTextTypeface;
    private final GraphicManager mGraphicManager;
    private final ViewGroup mPhotoEditorView;
    private final PhotoEditorViewState mViewState;
    private TextView mTextView;

    public Text(ViewGroup photoEditorView,
                MultiTouchListener multiTouchListener,
                PhotoEditorViewState viewState,
                Typeface defaultTextTypeface,
                GraphicManager graphicManager
    ) {
        super(photoEditorView.getContext(), graphicManager);
        mPhotoEditorView = photoEditorView;
        mViewState = viewState;
        mMultiTouchListener = multiTouchListener;
        mDefaultTextTypeface = defaultTextTypeface;
        mGraphicManager = graphicManager;
        setupGesture();
    }

    void buildView(String text, TextStyleBuilder styleBuilder) {
        mTextView.setText(text);
        if (styleBuilder != null)
            styleBuilder.applyStyle(mTextView);
    }

    private void setupGesture() {
        MultiTouchListener.OnGestureControl onGestureControl = buildGestureController(mPhotoEditorView, mViewState);
        mMultiTouchListener.setOnGestureControl(onGestureControl);
        View rootView = getRootView();
        rootView.setOnTouchListener(mMultiTouchListener);
    }


    @Override
    ViewType getViewType() {
        return ViewType.TEXT;
    }

    @Override
    int getLayoutId() {
        return R.layout.view_photo_editor_text;
    }

    @Override
    void setupView(View rootView) {
        mTextView = rootView.findViewById(R.id.tvPhotoEditorText);
        if (mTextView != null && mDefaultTextTypeface != null) {
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setTypeface(mDefaultTextTypeface);
        }

        final ImageView imgEdit = rootView.findViewById(R.id.imgPhotoEditorEdit);
        if (imgEdit != null) {
            imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateView(getRootView());
                }
            });
        }
    }

    @Override
    void updateView(View view) {
        String textInput = mTextView.getText().toString();
        int currentTextColor = mTextView.getCurrentTextColor();
        OnPhotoEditorListener photoEditorListener = mGraphicManager.getOnPhotoEditorListener();
        if (photoEditorListener != null) {
            final TextStyleBuilder styleBuilder = new TextStyleBuilder();
            styleBuilder.withTextColor(currentTextColor);

            if(mTextView.getBackground() instanceof ColorDrawable) {
                ColorDrawable cd = (ColorDrawable) mTextView.getBackground();
                styleBuilder.withBackgroundColor(cd.getColor());
            }
            boolean isUnderline = false;

            styleBuilder.withTextStyle(mTextView.getTypeface().getStyle() == Typeface.BOLD ? 1 : 0);
            styleBuilder.withGravity(mTextView.getGravity());
            styleBuilder.withTextFlag(isUnderline ? Paint.UNDERLINE_TEXT_FLAG : 0);
            photoEditorListener.onEditTextChangeListener(view, textInput, styleBuilder);
        }
    }
}
