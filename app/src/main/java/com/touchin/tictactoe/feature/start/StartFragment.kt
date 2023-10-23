package com.touchin.tictactoe.feature.start

import com.anadolstudio.core.view.animation.AnimateUtil.scaleAnimationOnClick
import com.anadolstudio.core.viewbinding.viewBinding
import com.anadolstudio.core.viewmodel.obtainViewModel
import com.touchin.tictactoe.R
import com.touchin.tictactoe.base.fragment.BaseActionFragment
import com.touchin.tictactoe.databinding.FragmentStartBinding

class StartFragment : BaseActionFragment<StartViewModel, StartController>(R.layout.fragment_start) {

    private val binding: FragmentStartBinding by viewBinding { FragmentStartBinding.bind(requireView()) }

    override fun createViewModel(): StartViewModel = obtainViewModel(StartViewModel.Factory())

    override fun initView(controller: StartController) {
        binding.duoButton.scaleAnimationOnClick(action = controller::onDuoClicked)
        binding.trioButton.scaleAnimationOnClick(action = controller::onTrioClicked)
    }
}
