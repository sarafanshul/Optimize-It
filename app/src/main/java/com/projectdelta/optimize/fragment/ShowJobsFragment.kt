package com.projectdelta.optimize.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.projectdelta.optimize.R
import com.projectdelta.optimize.adapter.RecyclerViewShowJobsAdapter
import com.projectdelta.optimize.data.ProjectDatabase
import com.projectdelta.optimize.data.entities.Worker
import com.projectdelta.optimize.databinding.ShowJobsFragmentBinding
import com.projectdelta.optimize.util.StatesRecyclerViewAdapter
import com.projectdelta.optimize.viewModel.ShowJobsViewModel

class ShowJobsFragment : Fragment() {

	companion object {
		fun newInstance() = ShowJobsFragment()
	}

	private lateinit var viewModel: ShowJobsViewModel
	private var _binding : ShowJobsFragmentBinding? = null
	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!

	private lateinit var worker: Worker
	private lateinit var adapter : RecyclerViewShowJobsAdapter

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		worker = arguments?.getSerializable("WORKER") as Worker
		_binding = ShowJobsFragmentBinding.inflate(inflater , container , false)
		return binding.root
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel = ViewModelProvider(this).get(ShowJobsViewModel::class.java)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.showJobsTwBack.setOnClickListener {
			requireActivity().supportFragmentManager.popBackStack()
		}

		binding.showJobsTwName.text = worker.workerId.toString()

		adapter = RecyclerViewShowJobsAdapter()
		binding.showJobsRv.layoutManager = LinearLayoutManager(activity)
		val emptyView : View = layoutInflater.inflate( R.layout.layout_empty_view , binding.showJobsRv , false )
		val statesRecyclerViewAdapter = StatesRecyclerViewAdapter( adapter , emptyView , emptyView , emptyView )
		binding.showJobsRv.adapter = statesRecyclerViewAdapter
		binding.showJobsRv.addItemDecoration( DividerItemDecoration( binding.showJobsRv.context , DividerItemDecoration.VERTICAL ) )

		statesRecyclerViewAdapter.state = StatesRecyclerViewAdapter.STATE_EMPTY
		val DB = ProjectDatabase.getInstance(context?.applicationContext!!).projectDao()

		DB.getworkerLive(worker.projectName , worker.workerId).observe(viewLifecycleOwner , { W ->
			DB.getProjectWithContainersLive( worker.projectName  ).observe( viewLifecycleOwner , {XC ->
				if( XC.isNullOrEmpty() || W == null || XC.first().containers.isNullOrEmpty() ) return@observe
				adapter.set(W.jobs.map { XC.first().containers[ it ].containerName })
				statesRecyclerViewAdapter.state = StatesRecyclerViewAdapter.STATE_NORMAL
			})
		})

	}

}