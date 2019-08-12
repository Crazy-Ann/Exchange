package com.hynet.heebit.components.widget.radiogroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.view.autofill.AutofillValue;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.hynet.heebit.components.R;
import com.hynet.heebit.components.widget.radiogroup.listener.AddNestedRadioButtonListener;
import com.hynet.heebit.components.widget.radiogroup.listener.OnCheckedChangeListener;

import androidx.annotation.NonNull;

public class AddNestedFrameRadioButton extends FrameLayout implements AddNestedRadioButtonListener {

    @NonNull
    private NestedRadioGroupManager nestedRadioGroupManager;

    public AddNestedFrameRadioButton(@NonNull Context context) {
        super(context);
        initialize();
    }

    public AddNestedFrameRadioButton(@NonNull Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
        initialize();
        initializeAttrs(context, attrs);
    }

    public AddNestedFrameRadioButton(@NonNull Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
        initializeAttrs(context, attrs);
    }

    private void initialize() {
        nestedRadioGroupManager = new NestedRadioGroupManager();
    }

    private void initializeAttrs(@NonNull Context context, @NonNull AttributeSet attrs) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && getImportantForAutofill() == IMPORTANT_FOR_AUTOFILL_AUTO) {
            setImportantForAutofill(IMPORTANT_FOR_AUTOFILL_YES);
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NestedRadioGroup, R.attr.radioButtonStyle, 0);
        int resourceId = typedArray.getResourceId(R.styleable.NestedRadioGroup_nrg_checkedButton, View.NO_ID);
        if (resourceId != View.NO_ID) {
            nestedRadioGroupManager.initializeCheckedId(resourceId);
        }
        typedArray.recycle();
    }

    @Override
    public void addNestedRadioButton(NestedRadioButton nestedRadioButton) {
        nestedRadioGroupManager.addNestedRadioButton(nestedRadioButton);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        nestedRadioGroupManager.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AddNestedFrameRadioButton.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new AddNestedFrameRadioButton.LayoutParams(getContext(), attrs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof AddNestedFrameRadioButton.LayoutParams;
    }

    @Override
    protected AddNestedFrameRadioButton.LayoutParams generateDefaultLayoutParams() {
        return new AddNestedFrameRadioButton.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return RadioGroup.class.getName();
    }

    public static class LayoutParams extends FrameLayout.LayoutParams {

        /**
         * {@inheritDoc}
         */
        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(int w, int h) {
            super(w, h);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        @Override
        protected void setBaseAttributes(TypedArray typedArray, int widthAttr, int heightAttr) {
            if (typedArray.hasValue(widthAttr)) {
                width = typedArray.getLayoutDimension(widthAttr, "layout_width");
            } else {
                width = WRAP_CONTENT;
            }
            if (typedArray.hasValue(heightAttr)) {
                height = typedArray.getLayoutDimension(heightAttr, "layout_height");
            } else {
                height = WRAP_CONTENT;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public void onProvideAutofillStructure(ViewStructure structure, int flags) {
        super.onProvideAutofillStructure(structure, flags);
        nestedRadioGroupManager.onProvideAutofillStructure(structure);
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public void autofill(AutofillValue value) {
        if (!isEnabled()) return;
        if (!value.isList()) {
            Log.w(getClass().getSimpleName(), value + " could not be autofilled into " + this);
            return;
        }
        final int index = value.getListValue();
        final View child = getChildAt(index);
        if (child == null) {
            Log.w(VIEW_LOG_TAG, "RadioGroup.autoFill(): no child with index " + index);
            return;
        }
        nestedRadioGroupManager.check(child.getId());
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public int getAutofillType() {
        return isEnabled() ? AUTOFILL_TYPE_LIST : AUTOFILL_TYPE_NONE;
    }

    @TargetApi(Build.VERSION_CODES.O)
    @Override
    public AutofillValue getAutofillValue() {
        if (!isEnabled()) return null;
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getId() == nestedRadioGroupManager.getCheckedId()) {
                return AutofillValue.forList(i);
            }
        }
        return null;
    }
}
