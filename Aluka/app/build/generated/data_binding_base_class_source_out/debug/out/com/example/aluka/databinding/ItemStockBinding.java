// Generated by view binder compiler. Do not edit!
package com.example.aluka.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.example.aluka.R;
import java.lang.NullPointerException;
import java.lang.Override;

public final class ItemStockBinding implements ViewBinding {
  @NonNull
  private final TextView rootView;

  @NonNull
  public final TextView stockNameTextView;

  private ItemStockBinding(@NonNull TextView rootView, @NonNull TextView stockNameTextView) {
    this.rootView = rootView;
    this.stockNameTextView = stockNameTextView;
  }

  @Override
  @NonNull
  public TextView getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemStockBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemStockBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_stock, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemStockBinding bind(@NonNull View rootView) {
    if (rootView == null) {
      throw new NullPointerException("rootView");
    }

    TextView stockNameTextView = (TextView) rootView;

    return new ItemStockBinding((TextView) rootView, stockNameTextView);
  }
}
