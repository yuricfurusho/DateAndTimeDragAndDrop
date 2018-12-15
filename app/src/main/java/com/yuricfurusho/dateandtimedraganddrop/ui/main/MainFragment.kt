package com.yuricfurusho.dateandtimedraganddrop.ui.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.yuricfurusho.dateandtimedraganddrop.R
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        swipe_dateAndTime.setOnRefreshListener { viewModel.loadDateAndTime() }

        viewModel.getEnvironment().observe(this, Observer<MainViewModel.ENVIRONMENT> { environment ->
            environment?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    when (environment) {
                        MainViewModel.ENVIRONMENT.LOCAL -> view?.foreground =
                                resources.getDrawable(R.drawable.local_overlay_tiled)
                        MainViewModel.ENVIRONMENT.REMOTE -> view?.foreground = null
                    }
                }
            }
        })

        viewModel.getLoading().observe(this, Observer<Boolean> { loading ->
            loading?.let {
                swipe_dateAndTime.isRefreshing = loading
            }
        })

        viewModel.getDateTime().observe(this, Observer<String> { datetime ->
            datetime?.let {
                textView_dateAndTime.text = datetime
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.startRepeatingTask()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopRepeatingTask()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.main_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        menu?.setGroupVisible(R.id.environmentGroup, resources.getBoolean(R.bool.environmentOptions))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.dummyMode -> {
                viewModel.setEnvironment(MainViewModel.ENVIRONMENT.LOCAL)
                true
            }
            R.id.remoteMode -> {
                viewModel.setEnvironment(MainViewModel.ENVIRONMENT.REMOTE)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
