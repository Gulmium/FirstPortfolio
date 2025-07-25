// Generated by view binder compiler. Do not edit!
package com.example.aluka.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.aluka.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityLoginsuccessBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button loginButton;

  @NonNull
  public final TextView loginSuccessMessage;

  @NonNull
  public final Button myPageButton;

  private ActivityLoginsuccessBinding(@NonNull LinearLayout rootView, @NonNull Button loginButton,
      @NonNull TextView loginSuccessMessage, @NonNull Button myPageButton) {
    this.rootView = rootView;
    this.loginButton = loginButton;
    this.loginSuccessMessage = loginSuccessMessage;
    this.myPageButton = myPageButton;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityLoginsuccessBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityLoginsuccessBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_loginsuccess, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityLoginsuccessBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.loginButton;
      Button loginButton = ViewBindings.findChildViewById(rootView, id);
      if (loginButton == null) {
        break missingId;
      }

      id = R.id.login_success_message;
      TextView loginSuccessMessage = ViewBindings.findChildViewById(rootView, id);
      if (loginSuccessMessage == null) {
        break missingId;
      }

      id = R.id.myPageButton;
      Button myPageButton = ViewBindings.findChildViewById(rootView, id);
      if (myPageButton == null) {
        break missingId;
      }

      return new ActivityLoginsuccessBinding((LinearLayout) rootView, loginButton,
          loginSuccessMessage, myPageButton);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
