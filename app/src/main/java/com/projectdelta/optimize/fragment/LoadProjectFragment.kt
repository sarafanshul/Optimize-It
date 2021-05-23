package com.projectdelta.optimize.fragment

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.projectdelta.optimize.R
import com.projectdelta.optimize.activity.EditProjectActivity
import com.projectdelta.optimize.adapter.RecyclerViewLoadProjectAdapter
import com.projectdelta.optimize.data.ProjectDatabase
import com.projectdelta.optimize.databinding.LayoutEmptyViewBinding
import com.projectdelta.optimize.databinding.LoadProjectFragmentBinding
import com.projectdelta.optimize.util.RecyclerItemClickListenr
import com.projectdelta.optimize.util.StatesRecyclerViewAdapter
import com.projectdelta.optimize.viewModel.LoadProjectViewModel

class LoadProjectFragment : Fragment() {

	companion object {
		fun newInstance() = LoadProjectFragment()
	}

	private lateinit var viewModel: LoadProjectViewModel
	lateinit var adapter : RecyclerViewLoadProjectAdapter
	private var _binding : LoadProjectFragmentBinding? = null
	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = LoadProjectFragmentBinding.inflate(inflater , container , false)
		return binding.root
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		viewModel = ViewModelProvider(this).get(LoadProjectViewModel::class.java)

	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val DB = ProjectDatabase.getInstance(context?.applicationContext!!).projectDao()

		binding.loadProjectRvMain.layoutManager = LinearLayoutManager(activity)
		val emptyView : View = layoutInflater.inflate( R.layout.layout_empty_view , binding.loadProjectRvMain , false )
		adapter = RecyclerViewLoadProjectAdapter()
		val statesRecyclerViewAdapter = StatesRecyclerViewAdapter( adapter , emptyView , emptyView , emptyView )
		binding.loadProjectRvMain.adapter = statesRecyclerViewAdapter

		statesRecyclerViewAdapter.state = StatesRecyclerViewAdapter.STATE_EMPTY

		DB.getAllProjects().observe(viewLifecycleOwner , { data ->
			if( data == null || data.isEmpty() )
				statesRecyclerViewAdapter.state = StatesRecyclerViewAdapter.STATE_EMPTY
			else{
				adapter.set( data )
				statesRecyclerViewAdapter.state = StatesRecyclerViewAdapter.STATE_NORMAL
			}
		})

		binding.loadProjectRvMain.addOnItemTouchListener(
			RecyclerItemClickListenr( context?.applicationContext!! , binding.loadProjectRvMain ,
				object : RecyclerItemClickListenr.OnItemClickListener{
					override fun onItemClick(view: View, position: Int) {
						launchProject( position )
					}
					override fun onItemLongClick(view: View?, position: Int) { }
				}
			)
		)

		binding.loadProjectTwCancel.setOnClickListener {
			requireActivity().supportFragmentManager.popBackStack()
		}

		// not needed
		binding.loadProjectTwLoad.visibility = View.GONE
	}

	private fun launchProject( position : Int ){
		MaterialAlertDialogBuilder(requireContext()).apply {
			setTitle("Continue with ${ adapter.data[position].projectName } ?")
			setPositiveButton("YES"){_ , _ ->
				Intent( requireContext() , EditProjectActivity::class.java ).apply {
					putExtra("PROJECT_NAME" , adapter.data[position].projectName )
				}.also {
					startActivity(it)
				}
			}
			setNegativeButton("CANCEL"){_ , _ -> }
			create()
		}.show()
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

}