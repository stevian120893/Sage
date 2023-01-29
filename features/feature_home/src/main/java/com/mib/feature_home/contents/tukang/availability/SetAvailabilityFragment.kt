package com.mib.feature_home.contents.tukang.availability

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mib.feature_home.contents.tukang.availability.SetAvailabilityViewModel.Companion.EVENT_UPDATE_FORM
import com.mib.feature_home.databinding.FragmentSetAvailabilityBinding
import com.mib.feature_home.domain.model.AvailabilityDay
import com.mib.feature_home.utils.TimeDialogListener
import com.mib.feature_home.utils.openTimePicker
import com.mib.lib.mvvm.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SetAvailabilityFragment : BaseFragment<SetAvailabilityViewModel>(0) {

    private var _binding: FragmentSetAvailabilityBinding? = null
    private val binding get() = _binding!!

    override fun initViewModel(firstInit: Boolean) {
        setViewModel(SetAvailabilityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.fetchAvailabilityDays(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetAvailabilityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadingDialogNavigation.subscribe(this, true)
        lifecycleScope.launch {
            initListener(view.context)
            observeLiveData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListener(context: Context) {
        setTimeEditTextListener(context, binding.etOpenHourSunday)
        setTimeEditTextListener(context, binding.etOpenHourMonday)
        setTimeEditTextListener(context, binding.etOpenHourTuesday)
        setTimeEditTextListener(context, binding.etOpenHourWednesday)
        setTimeEditTextListener(context, binding.etOpenHourThursday)
        setTimeEditTextListener(context, binding.etOpenHourFriday)
        setTimeEditTextListener(context, binding.etOpenHourSaturday)

        setTimeEditTextListener(context, binding.etClosedHourSunday)
        setTimeEditTextListener(context, binding.etClosedHourMonday)
        setTimeEditTextListener(context, binding.etClosedHourTuesday)
        setTimeEditTextListener(context, binding.etClosedHourWednesday)
        setTimeEditTextListener(context, binding.etClosedHourThursday)
        setTimeEditTextListener(context, binding.etClosedHourFriday)
        setTimeEditTextListener(context, binding.etClosedHourSaturday)

        binding.btSave.setOnClickListener {
            viewModel.save(
                fragment = this,
                listOf(
                    AvailabilityDay(SUNDAY, binding.etOpenHourSunday.text.toString(), binding.etClosedHourSunday.text.toString(), binding.cbSunday.isChecked),
                    AvailabilityDay(MONDAY, binding.etOpenHourMonday.text.toString(), binding.etClosedHourMonday.text.toString(), binding.cbMonday.isChecked),
                    AvailabilityDay(TUESDAY, binding.etOpenHourTuesday.text.toString(), binding.etClosedHourTuesday.text.toString(), binding.cbTuesday.isChecked),
                    AvailabilityDay(WEDNESDAY, binding.etOpenHourWednesday.text.toString(), binding.etClosedHourWednesday.text.toString(), binding.cbWednesday.isChecked),
                    AvailabilityDay(THURSDAY, binding.etOpenHourThursday.text.toString(), binding.etClosedHourThursday.text.toString(), binding.cbThursday.isChecked),
                    AvailabilityDay(FRIDAY, binding.etOpenHourFriday.text.toString(), binding.etClosedHourFriday.text.toString(), binding.cbFriday.isChecked),
                    AvailabilityDay(SATURDAY, binding.etOpenHourSaturday.text.toString(), binding.etClosedHourSaturday.text.toString(), binding.cbSaturday.isChecked)
                )
            )
        }

        binding.ivBack.setOnClickListener {
            viewModel.goToTukangMenuScreen(findNavController())
        }
    }

    private fun observeLiveData() {
        viewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            when (state.event) {
                EVENT_UPDATE_FORM -> {
                    state.availabilityDays?.forEach {
                        when(it.dayName) {
                            SUNDAY -> {
                                binding.etOpenHourSunday.setText(it.openHour)
                                binding.etClosedHourSunday.setText(it.closedHour)
                                binding.cbSunday.isChecked = it.isOpen
                            }
                            MONDAY -> {
                                binding.etOpenHourMonday.setText(it.openHour)
                                binding.etClosedHourMonday.setText(it.closedHour)
                                binding.cbMonday.isChecked = it.isOpen
                            }
                            TUESDAY -> {
                                binding.etOpenHourTuesday.setText(it.openHour)
                                binding.etClosedHourTuesday.setText(it.closedHour)
                                binding.cbTuesday.isChecked = it.isOpen
                            }
                            WEDNESDAY -> {
                                binding.etOpenHourWednesday.setText(it.openHour)
                                binding.etClosedHourWednesday.setText(it.closedHour)
                                binding.cbWednesday.isChecked = it.isOpen
                            }
                            THURSDAY -> {
                                binding.etOpenHourThursday.setText(it.openHour)
                                binding.etClosedHourThursday.setText(it.closedHour)
                                binding.cbThursday.isChecked = it.isOpen
                            }
                            FRIDAY -> {
                                binding.etOpenHourFriday.setText(it.openHour)
                                binding.etClosedHourFriday.setText(it.closedHour)
                                binding.cbFriday.isChecked = it.isOpen
                            }
                            SATURDAY -> {
                                binding.etOpenHourSaturday.setText(it.openHour)
                                binding.etClosedHourSaturday.setText(it.closedHour)
                                binding.cbSaturday.isChecked = it.isOpen
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setTimeEditTextListener(context: Context, editText: EditText) {
        editText.let { et ->
            et.setOnClickListener {
                et.openTimePicker(context, object: TimeDialogListener {
                    override fun onFinishSelectTime(result: String) {
                        editText.setText(result)
                    }
                })
            }
        }
    }

    companion object {
        const val SUNDAY = "Minggu"
        const val MONDAY = "Senin"
        const val TUESDAY = "Selasa"
        const val WEDNESDAY = "Rabu"
        const val THURSDAY = "Kamis"
        const val FRIDAY = "Jumat"
        const val SATURDAY = "Sabtu"
    }
}