package com.app.missednotificationsreminder.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.missednotificationsreminder.R;
import com.app.missednotificationsreminder.binding.model.SoundViewModel;
import com.app.missednotificationsreminder.databinding.SoundViewBinding;
import com.app.missednotificationsreminder.ui.fragment.common.CommonFragmentWithViewModel;
import com.app.missednotificationsreminder.ui.view.SoundView;

import javax.inject.Inject;

/**
 * Fragment which displays sound settings view
 *
 * @author Eugene Popovich
 */
public class SoundFragment extends CommonFragmentWithViewModel<SoundViewModel> implements SoundView {
    /**
     * The request code used to run ringtone picker
     */
    static final int SELECT_RINGTONE_REQUEST_CODE = 0;

    @Inject SoundViewModel model;
    SoundViewBinding mBinding;

    @Override public SoundViewModel getModel() {
        return model;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = SoundViewBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view, savedInstanceState);
    }

    private void init(View view, Bundle savedInstanceState) {
        mBinding.setModel(model);
    }

    @Override public void selectRingtone(String currentRingtoneUri) {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, getString(R.string.sound_select_ringtone_dialog_title));
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
                TextUtils.isEmpty(currentRingtoneUri) ? (Uri) null : Uri.parse(currentRingtoneUri));
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        startActivityForResult(intent, SELECT_RINGTONE_REQUEST_CODE);
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_RINGTONE_REQUEST_CODE) {
            // if received successful response from the ringtone picker
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            model.onRingtoneSelected(uri);
        }
    }
}
