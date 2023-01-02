package com.aprouxdev.arcencielplanning.extensions

import androidx.fragment.app.FragmentManager
import com.aprouxdev.arcencielplanning.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


fun BottomSheetDialogFragment.present(fm: FragmentManager, tag: String) {
    this.setStyle(
        BottomSheetDialogFragment.STYLE_NORMAL,
        R.style.BottomSheetDialogTheme
    )
    this.show(fm, tag)
}