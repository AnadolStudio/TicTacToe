package com.touchin.tictactoe.base.fragment

import androidx.annotation.LayoutRes
import androidx.navigation.fragment.findNavController
import com.anadolstudio.core.presentation.Eventable
import com.anadolstudio.core.presentation.Navigatable
import com.anadolstudio.core.presentation.fragment.CoreActionBaseFragment
import com.anadolstudio.core.viewmodel.BaseController
import com.anadolstudio.core.viewmodel.CoreActionViewModel
import com.touchin.tictactoe.navigation.NavigatableDelegate
import com.touchin.tictactoe.navigation.NavigateData

abstract class BaseActionFragment<ViewModel : CoreActionViewModel<NavigateData>, Controller : BaseController>(
    @LayoutRes layoutId: Int
) : CoreActionBaseFragment<Controller, NavigateData, ViewModel>(layoutId) {

    override val eventableDelegate: Eventable get() = Eventable.Delegate(uiEntity = this)
    override val navigatableDelegate: Navigatable<NavigateData> get() = NavigatableDelegate(findNavController())

}
